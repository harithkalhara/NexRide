package com.nexride.service;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class RideService {
  private final StateRepository repo;

  public RideService(StateRepository repo) {
    this.repo = repo;
  }

  public List<RideOffer> list() {
    return repo.read(s -> s.getRides().stream().filter(r -> "OPEN".equals(r.getStatus())).toList());
  }

  public RideOffer create(User u, RideOffer x) {
    if (!"CUSTOMER".equals(u.getRole())) throw ApiException.forbidden("Customer account required.");
    if (x.getSeats() < 1 || x.getSeats() > 4)
      throw ApiException.bad("Passenger seats must be 1–4.");
    try {
      if (LocalDateTime.parse(x.getDeparture()).isBefore(LocalDateTime.now(ZoneId.of("Asia/Colombo"))))
        throw ApiException.bad("Ride departure must be in the future.");
    } catch (ApiException e) {
      throw e;
    } catch (Exception e) {
      throw ApiException.bad("Choose a valid departure time.");
    }
    x.setHostId(u.getId());
    x.setOrigin(UserService.clean(x.getOrigin(), 80));
    x.setDestination(UserService.clean(x.getDestination(), 80));
    return repo.update(
        s -> {
          s.getRides().add(x);
          return x;
        });
  }

  public List<RideBooking> bookings(User u) {
    return repo.read(
        s ->
            s.getRideBookings().stream()
                .filter(
                    b ->
                        "ADMIN".equals(u.getRole())
                            || b.getUserId().equals(u.getId())
                            || s.getRides().stream()
                                .anyMatch(
                                    r ->
                                        r.getId().equals(b.getRideId())
                                            && r.getHostId().equals(u.getId())))
                .toList());
  }

  public RideBooking join(User u, String id) {
    return repo.update(
        s -> {
          RideOffer r = UserService.find(s.getRides(), id);
          if (!"OPEN".equals(r.getStatus()) || r.getHostId().equals(u.getId()))
            throw ApiException.bad("This ride cannot be joined.");
          long used =
              s.getRideBookings().stream()
                  .filter(b -> b.getRideId().equals(id) && "ACCEPTED".equals(b.getStatus()))
                  .count();
          if (used >= r.getSeats()) throw ApiException.bad("No seats remain.");
          if (s.getRideBookings().stream()
              .anyMatch(
                  b ->
                      b.getRideId().equals(id)
                          && b.getUserId().equals(u.getId())
                          && Set.of("PENDING", "ACCEPTED").contains(b.getStatus())))
            throw ApiException.bad("You already requested this ride.");
          RideBooking b = new RideBooking();
          b.setUserId(u.getId());
          b.setRideId(id);
          s.getRideBookings().add(b);
          return b;
        });
  }

  public RideBooking decision(User actor, String id, String decision) {
    return repo.update(
        s -> {
          RideBooking b = UserService.find(s.getRideBookings(), id);
          RideOffer r = UserService.find(s.getRides(), b.getRideId());
          if (!actor.getId().equals(r.getHostId()) && !"ADMIN".equals(actor.getRole()))
            throw ApiException.forbidden("Only the ride host can manage seat requests.");
          if (!"PENDING".equals(b.getStatus()))
            throw ApiException.bad("Request is no longer pending.");
          if ("accept".equals(decision)) {
            long used =
                s.getRideBookings().stream()
                    .filter(
                        x -> x.getRideId().equals(r.getId()) && "ACCEPTED".equals(x.getStatus()))
                    .count();
            if (used >= r.getSeats()) throw ApiException.bad("No seats remain.");
            b.setStatus("ACCEPTED");
          } else if ("reject".equals(decision)) b.setStatus("REJECTED");
          else throw ApiException.bad("Unknown decision.");
          return b;
        });
  }
}
