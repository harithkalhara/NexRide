package com.nexride.config;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import com.nexride.service.PasswordService;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** Fresh local install seeds reference fleet and creates a private first admin credential. */
@Component
public class Bootstrap implements ApplicationRunner {
  private final StateRepository repo;
  private final PasswordService passwords;

  @Value("${nexride.admin-email:admin@nexride.local}")
  private String email;

  @Value("${nexride.admin-password:}")
  private String configuredPassword;

  @Value("${nexride.data-dir:./data}")
  private String dataDir;

  public Bootstrap(StateRepository repo, PasswordService passwords) {
    this.repo = repo;
    this.passwords = passwords;
  }

  public void run(ApplicationArguments args) {
    if (repo.read(s -> s.getUsers().stream().anyMatch(u -> "ADMIN".equals(u.getRole())))) {
      seedFleet();
      return;
    }
    String password = configuredPassword.isBlank() ? "Nx!" + UUID.randomUUID() : configuredPassword;
    User admin = new User();
    admin.setName("NexRide Administrator");
    admin.setEmail(email.toLowerCase());
    admin.setPasswordHash(passwords.hash(password));
    admin.setRole("ADMIN");
    admin.setPhone("");
    repo.update(
        s -> {
          s.getUsers().add(admin);
          return null;
        });
    if (configuredPassword.isBlank())
      try {
        Path note = Path.of(dataDir).toAbsolutePath().resolve("admin-access.txt");
        Files.writeString(
            note,
            "NexRide administrator setup\nPortal: double-click ADMIN-LOGIN.bat or add /admin to the current website URL\nEmail: "
                + email
                + "\nPassword: "
                + password
                + "\nChange this password after signing in; keep this file private.\n");
        try {
          Files.setPosixFilePermissions(note, PosixFilePermissions.fromString("rw-------"));
        } catch (UnsupportedOperationException ignored) {
        }
      } catch (Exception e) {
        throw new IllegalStateException("Unable to write private first-admin access note.", e);
      }
    seedFleet();
  }

  private void seedFleet() {
    repo.update(
        s -> {
          if (!s.getVehicles().isEmpty()) return null;
          add(
              s,
              new Motorcycle(),
              "Royal Enfield Classic 350",
              "Galle",
              4800,
              "motorcycle.jpg",
              350,
              "A relaxed touring classic for coastal roads.");
          add(
              s,
              new Motorcycle(),
              "Yamaha FZ-S FI",
              "Colombo",
              2800,
              "sport.jpg",
              149,
              "A nimble city street bike.");
          add(
              s,
              new Motorcycle(),
              "Honda Dio",
              "Colombo",
              1800,
              "scooter.jpg",
              110,
              "An easy scooter for getting around town.");
          add(
              s,
              new Motorcycle(),
              "Bajaj Pulsar NS200",
              "Kandy",
              3500,
              "sport.jpg",
              200,
              "A responsive motorcycle for winding hill roads.");
          add(
              s,
              new Motorcycle(),
              "TVS Ntorq 125",
              "Kalmunai",
              2200,
              "scooter.jpg",
              125,
              "A compact scooter for everyday trips.");
          add(
              s,
              new Bicycle(),
              "City Explorer",
              "Colombo",
              900,
              "bicycle.jpg",
              0,
              "A simple pedal bike for discovering the city.");
          add(
              s,
              new Bicycle(),
              "Hill Country Hybrid",
              "Kandy",
              1300,
              "cycling.jpg",
              0,
              "A versatile bicycle for scenic day rides.");
          return null;
        });
  }

  private void add(
      AppState s,
      Vehicle b,
      String name,
      String city,
      int rate,
      String image,
      int cc,
      String description) {
    b.setName(name);
    b.setLocation(city);
    b.setDailyRate(rate);
    b.setStatus("AVAILABLE");
    b.setPhotoUrl("/assets/" + image);
    b.setPhotoCredit("Pexels reference photograph · see docs/ASSET_CREDITS.md");
    b.setSamplePhoto(true);
    b.setDescription(description);
    if (b instanceof Motorcycle m) m.setEngineCc(cc);
    s.getVehicles().add(b);
  }
}
