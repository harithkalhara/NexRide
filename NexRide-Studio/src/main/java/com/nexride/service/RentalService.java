package com.nexride.service;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class RentalService {
  private final StateRepository repo;

  public RentalService(StateRepository repo) {
    this.repo = repo;
  }

  public List<Rental> list(User u) {
    return repo.read(
        s ->
            s.getRentals().stream()
                .filter(r -> "ADMIN".equals(u.getRole()) || r.getUserId().equals(u.getId()))
                .toList());
  }

  public Rental request(User u, String vehicleId, String start, String end, String note) {
    if (!"CUSTOMER".equals(u.getRole())) throw ApiException.forbidden("Customer account required.");
    LocalDate a, b;
    try {
      a = LocalDate.parse(start);
      b = LocalDate.parse(end);
    } catch (Exception e) {
      throw ApiException.bad("Choose valid pickup and return dates.");
    }
    long days = ChronoUnit.DAYS.between(a, b) + 1;
    if (a.isBefore(LocalDate.now(ZoneId.of("Asia/Colombo"))) || days < 1 || days > 30)
      throw ApiException.bad("Choose dates from today for 1–30 days.");
    return repo.update(
        s -> {
          Vehicle v = UserService.find(s.getVehicles(), vehicleId);
          if (!"AVAILABLE".equals(v.getStatus()))
            throw ApiException.bad("This bike is not available.");
          boolean overlap =
              s.getRentals().stream()
                  .filter(
                      r ->
                          r.getVehicleId().equals(vehicleId)
                              && Set.of("PENDING", "APPROVED", "ACTIVE").contains(r.getStatus()))
                  .anyMatch(
                      r ->
                          !b.isBefore(LocalDate.parse(r.getStartDate()))
                              && !a.isAfter(LocalDate.parse(r.getEndDate())));
          if (overlap)
            throw ApiException.bad("The bike is already requested for dates that overlap.");
          int subtotal = v.getDailyRate() * (int) days;
          int total = days >= 7 ? (int) Math.round(subtotal * .9) : subtotal;
          Rental r = new Rental();
          r.setUserId(u.getId());
          r.setVehicleId(vehicleId);
          r.setStartDate(start);
          r.setEndDate(end);
          r.setTotal(total);
          r.setNote(note == null ? "" : note.strip());
          s.getRentals().add(r);
          return r;
        });
  }

  public Rental action(User actor, String id, String action, String note) {
    return repo.update(
        s -> {
          Rental r = UserService.find(s.getRentals(), id);
          if (!"ADMIN".equals(actor.getRole()) && !r.getUserId().equals(actor.getId()))
            throw ApiException.forbidden("This rental belongs to another account.");
          switch (action) {
            case "approve" -> {
              UserService.requireAdmin(actor);
              if (!"PENDING".equals(r.getStatus()))
                throw ApiException.bad("Only pending rentals can be approved.");
              r.setStatus("APPROVED");
              Invoice i = new Invoice();
              i.setRentalId(r.getId());
              i.setUserId(r.getUserId());
              i.setAmountLkr(r.getTotal());
              i.setDescription("NexRide rental · " + r.getStartDate() + " to " + r.getEndDate());
              s.getInvoices().add(i);
            }
            case "reject" -> {
              UserService.requireAdmin(actor);
              if (!"PENDING".equals(r.getStatus()))
                throw ApiException.bad("Only pending rentals can be rejected.");
              r.setStatus("REJECTED");
              r.setNote(note == null ? "" : note);
            }
            case "cancel" -> {
              if (!"PENDING".equals(r.getStatus()))
                throw ApiException.bad("Only pending requests can be cancelled in this version.");
              r.setStatus("CANCELLED");
            }
            case "pickup" -> {
              UserService.requireAdmin(actor);
              if (!"APPROVED".equals(r.getStatus())
                  || s.getInvoices().stream()
                      .anyMatch(i -> i.getRentalId().equals(id) && !"PAID".equals(i.getStatus())))
                throw ApiException.bad("Confirm all rental invoices as paid before pickup.");
              r.setStatus("ACTIVE");
            }
            case "return" -> {
              UserService.requireAdmin(actor);
              if (!"ACTIVE".equals(r.getStatus()))
                throw ApiException.bad("Only active rentals can be returned.");
              r.setStatus("COMPLETED");
            }
            default -> throw ApiException.bad("Unknown rental action.");
          }
          return r;
        });
  }
}
