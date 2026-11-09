package com.nexride.service;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import java.math.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OnlinePaymentService {
  private final StateRepository repo;
  private final String merchant, secret, mode, base;

  public OnlinePaymentService(
      StateRepository repo,
      @Value("${nexride.payhere.merchant-id:}") String merchant,
      @Value("${nexride.payhere.secret:}") String secret,
      @Value("${nexride.payhere.mode:disabled}") String mode,
      @Value("${nexride.public-url:}") String base) {
    this.repo = repo;
    this.merchant = merchant;
    this.secret = secret;
    this.mode = mode;
    this.base = base.replaceAll("/+$", "");
  }

  public boolean ready() {
    return Set.of("sandbox", "live").contains(mode)
        && !merchant.isBlank()
        && !secret.isBlank()
        && base.matches("https://[^/]+(?:/[^?#]*)?")
        && !base.contains("localhost")
        && !base.contains("127.0.0.1");
  }

  public Map<String, Object> options() {
    return Map.of(
        "ready",
        ready(),
        "mode",
        mode,
        "provider",
        "PayHere",
        "methods",
        List.of("VISA", "MASTER"));
  }

  public Map<String, Object> checkout(
      User actor, String invoiceId, String currency, String address, String city, String country) {
    if (!ready())
      throw ApiException.bad("Online card payments are not configured. Contact NexRide.");
    if (!Set.of("LKR", "USD").contains(currency)) throw ApiException.bad("Select LKR or USD.");
    if (actor.getPhone().isBlank())
      throw ApiException.bad("Add a phone number in Profile before checkout.");
    String addr = UserService.clean(address, 200),
        town = UserService.clean(city, 80),
        nation = UserService.clean(country, 80);
    return repo.update(
        s -> {
          Invoice invoice = UserService.find(s.getInvoices(), invoiceId);
          if (!"ADMIN".equals(actor.getRole()) && !invoice.getUserId().equals(actor.getId()))
            throw ApiException.forbidden("This invoice belongs to another account.");
          if (!"UNPAID".equals(invoice.getStatus()))
            throw ApiException.bad("Only unpaid invoices can be paid.");
          CheckoutAttempt a =
              s.getCheckoutAttempts().stream()
                  .filter(
                      x ->
                          x.getInvoiceId().equals(invoiceId)
                              && x.getCurrency().equals(currency)
                              && "PENDING".equals(x.getStatus()))
                  .findFirst()
                  .orElse(null);
          if (a == null) {
            BigDecimal rate = s.getSettings().getLkrPerUsd();
            if ("USD".equals(currency) && rate.signum() <= 0)
              throw ApiException.bad(
                  "USD checkout is disabled until an exchange rate is published.");
            BigDecimal amount =
                "USD".equals(currency)
                    ? BigDecimal.valueOf(invoice.getAmountLkr())
                        .divide(rate, 2, RoundingMode.HALF_UP)
                    : BigDecimal.valueOf(invoice.getAmountLkr()).setScale(2);
            a = new CheckoutAttempt();
            a.setInvoiceId(invoiceId);
            a.setUserId(invoice.getUserId());
            a.setCurrency(currency);
            a.setAmount(amount.toPlainString());
            a.setRate(rate.toPlainString());
            a.setLkrAmount(invoice.getAmountLkr());
            s.getCheckoutAttempts().add(a);
          }
          User customer = UserService.find(s.getUsers(), invoice.getUserId());
          String[] n = customer.getName().split("\\s+", 2);
          Map<String, String> f = new LinkedHashMap<>();
          f.put("merchant_id", merchant);
          f.put("return_url", base + "/?payment=return#account");
          f.put("cancel_url", base + "/?payment=cancel#account");
          f.put("notify_url", base + "/api/payments/payhere/notify");
          f.put("first_name", n[0]);
          f.put("last_name", n.length > 1 ? n[1] : n[0]);
          f.put("email", customer.getEmail());
          f.put("phone", customer.getPhone());
          f.put("address", addr);
          f.put("city", town);
          f.put("country", nation);
          f.put("order_id", a.getId());
          f.put("items", "NexRide invoice " + invoice.getId());
          f.put("currency", a.getCurrency());
          f.put("amount", a.getAmount());
          f.put("hash", md5(merchant + a.getId() + a.getAmount() + a.getCurrency() + md5(secret)));
          return Map.of(
              "action",
              "live".equals(mode)
                  ? "https://www.payhere.lk/pay/checkout"
                  : "https://sandbox.payhere.lk/pay/checkout",
              "fields",
              f,
              "mode",
              mode);
        });
  }

  public void notify(Map<String, String> p) {
    if (!ready()) throw ApiException.bad("Payment callback is disabled.");
    String m = p.getOrDefault("merchant_id", ""),
        order = p.getOrDefault("order_id", ""),
        amount = p.getOrDefault("payhere_amount", ""),
        currency = p.getOrDefault("payhere_currency", ""),
        code = p.getOrDefault("status_code", ""),
        signature = p.getOrDefault("md5sig", "");
    String expected = md5(m + order + amount + currency + code + md5(secret));
    if (!merchant.equals(m)
        || !MessageDigest.isEqual(
            expected.getBytes(StandardCharsets.UTF_8),
            signature.toUpperCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8)))
      throw ApiException.forbidden("Invalid payment notification signature.");
    repo.update(
        s -> {
          CheckoutAttempt a = UserService.find(s.getCheckoutAttempts(), order);
          if (!currency.equals(a.getCurrency())
              || new BigDecimal(amount).compareTo(new BigDecimal(a.getAmount())) != 0)
            throw ApiException.bad("Payment amount/currency mismatch.");
          Invoice i = UserService.find(s.getInvoices(), a.getInvoiceId());
          if ("2".equals(code)) {
            if ("PAID".equals(a.getStatus())) {
              if (!Objects.equals(a.getProviderReference(), p.get("payment_id")))
                throw ApiException.bad("Payment reference mismatch.");
              return null;
            }
            if ("DISPUTED".equals(a.getStatus()) || !"UNPAID".equals(i.getStatus()))
              throw ApiException.bad("Invoice needs manual reconciliation.");
            String ref = UserService.clean(p.get("payment_id"), 100);
            if (s.getPayments().stream().anyMatch(x -> x.getReference().equals(ref)))
              throw ApiException.bad("Duplicate payment reference.");
            Payment payment = new Payment();
            payment.setInvoiceId(i.getId());
            payment.setUserId(i.getUserId());
            payment.setAmountLkr(a.getLkrAmount());
            payment.setCurrency(currency);
            payment.setChargedAmount(a.getAmount());
            payment.setMethod(
                ("sandbox".equals(mode) ? "SANDBOX_" : "PAYHERE_")
                    + p.getOrDefault("method", "CARD"));
            payment.setReference(ref);
            s.getPayments().add(payment);
            a.setStatus("PAID");
            a.setProviderReference(ref);
            i.setStatus("PAID");
          } else if ("-3".equals(code)) {
            if ("DISPUTED".equals(a.getStatus())) return null;
            if (!"PAID".equals(a.getStatus()))
              throw ApiException.bad("No settled transaction to dispute.");
            a.setStatus("DISPUTED");
            i.setStatus("DISPUTED");
            s.getPayments().stream()
                .filter(x -> x.getReference().equals(a.getProviderReference()))
                .forEach(x -> x.setGatewayStatus("CHARGEBACK"));
          } else if (!Set.of("PAID", "DISPUTED").contains(a.getStatus())) {
            if ("-1".equals(code)) a.setStatus("CANCELLED");
            else if ("-2".equals(code)) a.setStatus("FAILED");
            else if (!"0".equals(code)) throw ApiException.bad("Unknown payment status.");
          }
          return null;
        });
  }

  public static String md5(String v) {
    try {
      return HexFormat.of()
          .withUpperCase()
          .formatHex(MessageDigest.getInstance("MD5").digest(v.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
