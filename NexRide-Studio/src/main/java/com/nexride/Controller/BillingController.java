package com.nexride.controller;

import com.nexride.model.SiteSettings;
import com.nexride.model.User;
import com.nexride.service.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

/** Invoices, payments, profile and site settings endpoints used by the frontend. */
@RestController
@RequestMapping("/api")
public class BillingController {

  private final UserService users;
  private final BillingService billing;
  private final OnlinePaymentService online;
  private final SettingsService settings;
  private final ExchangeRateService rates;

  public BillingController(
      UserService users,
      BillingService billing,
      OnlinePaymentService online,
      SettingsService settings,
      ExchangeRateService rates) {
    this.users = users;
    this.billing = billing;
    this.online = online;
    this.settings = settings;
    this.rates = rates;
  }

  private User require(HttpServletRequest req) {
    Object id = req.getSession(false) == null ? null : req.getSession(false).getAttribute("userId");
    if (id == null) throw ApiException.unauthorized();
    return users.byId(id.toString());
  }

  private Map<String, Object> view(User u) {
    Map<String, Object> x = new LinkedHashMap<>();
    x.put("id", u.getId());
    x.put("name", u.getName());
    x.put("email", u.getEmail());
    x.put("phone", u.getPhone());
    x.put("role", u.getRole());
    x.put("country", u.getCountry());
    x.put("currency", u.getCurrency());
    x.put("avatarUrl", u.getAvatarUrl());
    x.put("active", u.getActive());
    return x;
  }

  public record ProfileBody(
      String name, String phone, String country, String currency, String avatarUrl) {}

  public record PasswordBody(String currentPassword, String newPassword) {}

  public record CheckoutBody(String currency, String address, String city, String country) {}

  @GetMapping("/invoices")
  public Object invoices(HttpServletRequest req) {
    return billing.invoices(require(req));
  }

  @GetMapping("/payments")
  public Object payments(HttpServletRequest req) {
    return billing.payments(require(req));
  }

  @PostMapping("/invoices/{id}/cash")
  public Object cash(@PathVariable String id, HttpServletRequest req) {
    return billing.cash(require(req), id);
  }

  @PostMapping("/invoices/{id}/checkout")
  public Object checkout(
      @PathVariable String id, @RequestBody CheckoutBody b, HttpServletRequest req) {
    return online.checkout(require(req), id, b.currency(), b.address(), b.city(), b.country());
  }

  /** PayHere server-to-server callback (form encoded, no session, signature verified inside). */
  @PostMapping("/payments/payhere/notify")
  public Object notifyPayHere(@RequestParam Map<String, String> form) {
    online.notify(form);
    return Map.of("ok", true);
  }

  @PutMapping("/profile")
  public Object profile(@RequestBody ProfileBody b, HttpServletRequest req) {
    return view(
        users.update(require(req), b.name(), b.phone(), b.country(), b.currency(), b.avatarUrl()));
  }

  @PutMapping("/profile/password")
  public Object password(@RequestBody PasswordBody b, HttpServletRequest req) {
    users.password(require(req), b.currentPassword(), b.newPassword());
    return Map.of("ok", true);
  }

  @PutMapping("/settings")
  public Object saveSettings(@RequestBody SiteSettings input, HttpServletRequest req) {
    return settings.save(require(req), input);
  }

  @PostMapping("/settings/exchange-rate")
  public Object refreshRate(HttpServletRequest req) {
    return rates.refresh(require(req));
  }
}
