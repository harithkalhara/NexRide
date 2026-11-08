package com.nexride.service;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class FleetService {
  private final StateRepository repo;

  public FleetService(StateRepository repo) {
    this.repo = repo;
  }

  public List<Vehicle> list(String query, String kind, String city) {
    return list(query, kind, city, null, null);
  }

  public List<Vehicle> list(String query, String kind, String city, String start, String end) {
    java.time.LocalDate from = null, to = null;
    if ((start != null && !start.isBlank()) || (end != null && !end.isBlank())) {
      try {
        from = java.time.LocalDate.parse(start);
        to = java.time.LocalDate.parse(end);
      } catch (Exception ex) {
        throw ApiException.bad("Choose valid pickup and return dates.");
      }
      if (to.isBefore(from)) throw ApiException.bad("Return date must be on or after pickup.");
    }
    final java.time.LocalDate pickup = from, dropoff = to;
    return repo.read(
        s ->
            s.getVehicles().stream()
                .filter(b -> "AVAILABLE".equals(b.getStatus()))
                .filter(
                    b ->
                        query == null
                            || query.isBlank()
                            || b.getName().toLowerCase().contains(query.toLowerCase()))
                .filter(b -> kind == null || kind.isBlank() || b.getKind().equalsIgnoreCase(kind))
                .filter(
                    b ->
                        city == null
                            || city.isBlank()
                            || b.getLocation().toLowerCase().contains(city.toLowerCase()))
                .filter(
                    b ->
                        pickup == null
                            || s.getRentals().stream()
                                .filter(
                                    r ->
                                        r.getVehicleId().equals(b.getId())
                                            && Set.of("PENDING", "APPROVED", "ACTIVE")
                                                .contains(r.getStatus()))
                                .noneMatch(
                                    r ->
                                        !dropoff.isBefore(
                                                java.time.LocalDate.parse(r.getStartDate()))
                                            && !pickup.isAfter(
                                                java.time.LocalDate.parse(r.getEndDate()))))
                .toList());
  }

  public List<Vehicle> all(User actor) {
    UserService.requireAdmin(actor);
    return repo.read(AppState::getVehicles);
  }

  public Vehicle save(User actor, Vehicle input, String id) {
    UserService.requireAdmin(actor);
    if (input.getName() == null || input.getName().isBlank() || input.getName().length() > 90)
      throw ApiException.bad("Bike name is required.");
    if (input.getDailyRate() < 100 || input.getDailyRate() > 100000)
      throw ApiException.bad("Enter a daily rate from LKR 100 to 100,000.");
    if (!Set.of("AVAILABLE", "MAINTENANCE", "RETIRED").contains(input.getStatus()))
      throw ApiException.bad("Choose a valid fleet status.");
    input.setLocation(UserService.clean(input.getLocation(), 80));
    input.setPhotoUrl(UserService.safeMediaOrAsset(input.getPhotoUrl()));
    return repo.update(
        s -> {
          if (id == null) {
            input.setSamplePhoto(
                input.getPhotoUrl().isBlank() || input.getPhotoUrl().startsWith("/assets/"));
            input.setPhotoCredit(input.getPhotoUrl().isBlank() ? "" : "NexRide fleet photograph");
            s.getVehicles().add(input);
            return input;
          }
          Vehicle old =
              s.getVehicles().stream()
                  .filter(v -> v.getId().equals(id))
                  .findFirst()
                  .orElseThrow(() -> ApiException.missing("Bike not found."));
          input.setId(id);
          input.setCreatedAt(old.getCreatedAt());
          input.setSamplePhoto(
              input.getPhotoUrl().isBlank() || input.getPhotoUrl().startsWith("/assets/"));
          input.setPhotoCredit(
              input.getSamplePhoto() ? "Stock reference photograph" : "NexRide fleet photograph");
          s.getVehicles().set(s.getVehicles().indexOf(old), input);
          return input;
        });
  }
}
