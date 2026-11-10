
package com.nexride.service;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.springframework.stereotype.Service;

// @Service tells Spring that this class handles business logic.
@Service
public class RentalService {

  // Repository is used to read and update application data.
  private final StateRepository repo;

  // Constructor injection: Spring automatically provides StateRepository.
  public RentalService(StateRepository repo) {
    this.repo = repo;
  }

  // Returns rental records.
  // Admin can view all rentals, while customers can view only their own rentals.
  public List<Rental> list(User u) {
    return repo.read(
        s ->
            s.getRentals().stream()
                .filter(r -> "ADMIN".equals(u.getRole()) || r.getUserId().equals(u.getId()))
                .toList());
  }

  // Creates a new rental request for a vehicle.
  public Rental request(User u, String vehicleId, String start, String end, String note) {

    // Only customers are allowed to request rentals.
    if (!"CUSTOMER".equals(u.getRole())) throw ApiException.forbidden("Customer account required.");

    // Variables to store pickup and return dates.
    LocalDate a, b;

    // Convert the received date strings into LocalDate objects.
    try {
      a = LocalDate.parse(start);
      b = LocalDate.parse(end);
    } catch (Exception e) {
      // Return an error if the date format is invalid.
      throw ApiException.bad("Choose valid pickup and return dates.");
    }

    // Calculate the rental duration, including both pickup and return dates.
    long days = ChronoUnit.DAYS.between(a, b) + 1;

    // Validate rental dates: dates must start today or later and duration must be 1–30 days.
    if (a.isBefore(LocalDate.now(ZoneId.of("Asia/Colombo"))) || days < 1 || days > 30)
      throw ApiException.bad("Choose dates from today for 1–30 days.");

    // Update the application state to create the rental request.
    return repo.update(
        s -> {

          // Find the selected vehicle using its ID.
          Vehicle v = UserService.find(s.getVehicles(), vehicleId);

          // Check whether the selected vehicle is available.
          if (!"AVAILABLE".equals(v.getStatus()))
            throw ApiException.bad("This bike is not available.");

          // Check whether another pending, approved, or active rental
          // already exists for the same vehicle during overlapping dates.
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

          // Prevent double booking if rental dates overlap.
          if (overlap)
            throw ApiException.bad("The bike is already requested for dates that overlap.");

          // Calculate the rental price before applying any discount.
          int subtotal = v.getDailyRate() * (int) days;

          // Apply a 10% discount for rentals lasting 7 days or more.
          int total = days >= 7 ? (int) Math.round(subtotal * .9) : subtotal;

          // Create a new Rental object.
          Rental r = new Rental();

          // Connect the rental with the customer and selected vehicle.
          r.setUserId(u.getId());
          r.setVehicleId(vehicleId);

          // Store the pickup and return dates.
          r.setStartDate(start);
          r.setEndDate(end);

          // Store the final rental amount.
          r.setTotal(total);

          // Store the customer's note, or an empty string if no note is provided.
          r.setNote(note == null ? "" : note.strip());

          // Add the new rental request to the rental list.
          s.getRentals().add(r);

          // Return the newly created rental.
          return r;
        });
  }

  // Performs rental-related actions such as approve, reject, cancel, pickup, and return.
  public Rental action(User actor, String id, String action, String note) {
    return repo.update(
        s -> {

          // Find the rental using its ID.
          Rental r = UserService.find(s.getRentals(), id);

          // Allow only admins or the customer who owns this rental to access it.
          if (!"ADMIN".equals(actor.getRole()) && !r.getUserId().equals(actor.getId()))
            throw ApiException.forbidden("This rental belongs to another account.");

          // Execute the requested action based on the action name.
          switch (action) {

            // ADMIN approves a pending rental request.
            case "approve" -> {
              UserService.requireAdmin(actor);

              // Only pending rentals can be approved.
              if (!"PENDING".equals(r.getStatus()))
                throw ApiException.bad("Only pending rentals can be approved.");

              // Update rental status to APPROVED.
              r.setStatus("APPROVED");

              // Create an invoice for the approved rental.
              Invoice i = new Invoice();
              i.setRentalId(r.getId());
              i.setUserId(r.getUserId());
              i.setAmountLkr(r.getTotal());
              i.setDescription("NexRide rental · " + r.getStartDate() + " to " + r.getEndDate());

              // Add the invoice to the application's invoice list.
              s.getInvoices().add(i);
            }

            // ADMIN rejects a pending rental request.
            case "reject" -> {
              UserService.requireAdmin(actor);

              // Only pending rentals can be rejected.
              if (!"PENDING".equals(r.getStatus()))
                throw ApiException.bad("Only pending rentals can be rejected.");

              // Change status to REJECTED and save the rejection note.
              r.setStatus("REJECTED");
              r.setNote(note == null ? "" : note);
            }

            // CUSTOMER cancels their own pending rental request.
            case "cancel" -> {

              // Only pending requests can be cancelled in this version.
              if (!"PENDING".equals(r.getStatus()))
                throw ApiException.bad("Only pending requests can be cancelled in this version.");

              // Change rental status to CANCELLED.
              r.setStatus("CANCELLED");
            }

            // ADMIN confirms vehicle pickup after invoice payment.
            case "pickup" -> {
              UserService.requireAdmin(actor);

              // Check whether the rental is approved and all related invoices are paid.
              if (!"APPROVED".equals(r.getStatus())
                  || s.getInvoices().stream()
                      .anyMatch(i -> i.getRentalId().equals(id) && !"PAID".equals(i.getStatus())))
                throw ApiException.bad("Confirm all rental invoices as paid before pickup.");

              // Change rental status to ACTIVE.
              r.setStatus("ACTIVE");
            }

            // ADMIN processes the return of an active rental.
            case "return" -> {
              UserService.requireAdmin(actor);

              // Only active rentals can be returned.
              if (!"ACTIVE".equals(r.getStatus()))
                throw ApiException.bad("Only active rentals can be returned.");

              // Mark the rental as COMPLETED.
              r.setStatus("COMPLETED");
            }

            // Handle unsupported or invalid actions.
            default -> throw ApiException.bad("Unknown rental action.");
          }

          // Return the updated rental object.
          return r;
        });
  }
}
