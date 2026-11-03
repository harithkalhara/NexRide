package com.nexride.service;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final StateRepository repo;
  private final PasswordService passwords;

  public UserService(StateRepository repo, PasswordService passwords) {
    this.repo = repo;
    this.passwords = passwords;
  }

  public User register(String name, String email, String phone, String password, String country) {
    String mail = email.trim().toLowerCase(Locale.ROOT);
    if (!mail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
      throw ApiException.bad("Enter a valid email address.");
    if (password == null || password.length() < 8 || password.length() > 128)
      throw ApiException.bad("Password must have 8–128 characters.");
    String tel = normalize(phone);
    if (tel.length() < 8 || tel.length() > 15)
      throw ApiException.bad("Enter a valid phone number with country code.");
    String hash = passwords.hash(password);
    return repo.update(
        s -> {
          if (s.getUsers().stream()
              .anyMatch(
                  x -> x.getEmail().equalsIgnoreCase(mail) || normalize(x.getPhone()).equals(tel)))
            throw ApiException.bad("Email or phone number is already registered.");
          User u = new User();
          u.setName(clean(name, 80));
          u.setEmail(mail);
          u.setPhone(tel);
          u.setPasswordHash(hash);
          u.setCountry(clean(country, 80));
          u.setCurrency(u.getCountry().equalsIgnoreCase("Sri Lanka") ? "LKR" : "USD");
          s.getUsers().add(u);
          return u;
        });
  }

  public User login(String identifier, String password, String requiredRole) {
    String key = identifier.trim();
    User u =
        repo.read(
            s ->
                s.getUsers().stream()
                    .filter(
                        x ->
                            key.contains("@")
                                ? x.getEmail().equalsIgnoreCase(key)
                                : normalize(x.getPhone()).equals(normalize(key)))
                    .findFirst()
                    .orElse(null));
    if (u == null || !u.getActive() || !passwords.matches(password, u.getPasswordHash()))
      throw ApiException.unauthorized();
    if (requiredRole != null && !requiredRole.equals(u.getRole()))
      throw ApiException.forbidden(
          requiredRole.equals("ADMIN")
              ? "Use the administrator portal."
              : "Administrator accounts must use /admin.");
    return u;
  }

  public User byId(String id) {
    return repo.read(
        s ->
            s.getUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(ApiException::unauthorized));
  }

  public User update(
      User actor, String name, String phone, String country, String currency, String avatar) {
    String tel = normalize(phone);
    if (!tel.isBlank() && (tel.length() < 8 || tel.length() > 15))
      throw ApiException.bad("Enter a valid phone number.");
    return repo.update(
        s -> {
          User u = find(s.getUsers(), actor.getId());
          if (s.getUsers().stream()
              .anyMatch(
                  x ->
                      !x.getId().equals(u.getId())
                          && !tel.isBlank()
                          && normalize(x.getPhone()).equals(tel)))
            throw ApiException.bad("Phone number is already registered.");
          u.setName(clean(name, 80));
          u.setPhone(tel);
          u.setCountry(clean(country, 80));
          if (!Set.of("LKR", "USD").contains(currency))
            throw ApiException.bad("Choose LKR or USD.");
          u.setCurrency(currency);
          u.setAvatarUrl(safeMedia(avatar));
          return u;
        });
  }

  public void password(User u, String old, String next) {
    if (!passwords.matches(old, u.getPasswordHash()))
      throw ApiException.bad("Current password is incorrect.");
    if (next == null || next.length() < 8 || next.length() > 128)
      throw ApiException.bad("New password must have at least 8 characters.");
    repo.update(
        s -> {
          find(s.getUsers(), u.getId()).setPasswordHash(passwords.hash(next));
          return null;
        });
  }

  public List<User> customers(User actor) {
    requireAdmin(actor);
    return repo.read(
        s -> s.getUsers().stream().filter(u -> "CUSTOMER".equals(u.getRole())).toList());
  }

  public User active(User actor, String id, boolean enabled) {
    requireAdmin(actor);
    if (actor.getId().equals(id))
      throw ApiException.bad("Use sign out to end your own administrator session.");
    return repo.update(
        s -> {
          User u = find(s.getUsers(), id);
          if (!"CUSTOMER".equals(u.getRole()))
            throw ApiException.bad("Only customer accounts can be changed here.");
          u.setActive(enabled);
          return u;
        });
  }

  public static String normalize(String v) {
    String p = v == null ? "" : v.replaceAll("[\\s()+-]", "");
    if (p.startsWith("00")) p = p.substring(2);
    if (p.matches("0[0-9]{9}")) p = "94" + p.substring(1);
    return p;
  }

  public static String clean(String v, int max) {
    if (v == null) throw ApiException.bad("A required field is missing.");
    String x = v.strip();
    if (x.isBlank() || x.length() > max)
      throw ApiException.bad("Please check the required fields.");
    return x;
  }

  public static String safeMediaOrAsset(String x) {
    if (x != null && x.matches("/assets/[a-zA-Z0-9_-]+\\.jpg")) return x;
    return safeMedia(x);
  }

  public static String safeMedia(String x) {
    if (x == null || x.isBlank()) return "";
    if (!x.matches("/uploads/[a-f0-9-]{36}\\.png"))
      throw ApiException.bad("Upload an image with the provided image control.");
    return x;
  }

  public static void requireAdmin(User u) {
    if (u == null || !"ADMIN".equals(u.getRole()))
      throw ApiException.forbidden("Administrator access is required.");
  }

  public static <T> T find(List<T> list, String id) {
    return list.stream()
        .filter(x -> x instanceof BaseEntity b && b.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> ApiException.missing("Record not found."));
  }
}
