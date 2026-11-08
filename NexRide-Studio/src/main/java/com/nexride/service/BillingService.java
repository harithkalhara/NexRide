package com.nexride.service;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class BillingService {
  private final StateRepository repo;

  public BillingService(StateRepository r) {
    repo = r;
  }

  public List<Invoice> invoices(User u) {
    return repo.read(
        s ->
            s.getInvoices().stream()
                .filter(i -> "ADMIN".equals(u.getRole()) || i.getUserId().equals(u.getId()))
                .toList());
  }

  public List<Payment> payments(User u) {
    return repo.read(
        s ->
            s.getPayments().stream()
                .filter(p -> "ADMIN".equals(u.getRole()) || p.getUserId().equals(u.getId()))
                .toList());
  }

  public Payment cash(User u, String invoiceId) {
    UserService.requireAdmin(u);
    return repo.update(
        s -> {
          Invoice i = UserService.find(s.getInvoices(), invoiceId);
          if (!"UNPAID".equals(i.getStatus()))
            throw ApiException.bad("Only unpaid invoices can be recorded.");
          if (s.getCheckoutAttempts().stream()
              .anyMatch(a -> a.getInvoiceId().equals(invoiceId) && "PENDING".equals(a.getStatus())))
            throw ApiException.bad(
                "An online payment is pending. Verify it with the payment provider before recording"
                    + " cash.");
          Payment p = new Payment();
          p.setInvoiceId(i.getId());
          p.setUserId(i.getUserId());
          p.setAmountLkr(i.getAmountLkr());
          p.setChargedAmount(String.format(Locale.ROOT, "%.2f", (double) i.getAmountLkr()));
          p.setCurrency("LKR");
          p.setMethod("CASH");
          p.setReference("NX-CASH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
          s.getPayments().add(p);
          i.setStatus("PAID");
          return p;
        });
  }
}
