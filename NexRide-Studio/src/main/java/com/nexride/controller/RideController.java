package com.nexride.controller;

import com.nexride.model.RideOffer;
import com.nexride.model.User;
import com.nexride.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RideController {

    private final UserService users;
    private final RideService rides;

    public RideController(
            UserService users,
            RideService rides) {
        this.users = users;
        this.rides = rides;
    }

    private User require(HttpServletRequest req) {
        Object id = req.getSession(false) == null
                ? null
                : req.getSession(false).getAttribute("userId");

        if (id == null) {
            throw ApiException.unauthorized();
        }

        return users.byId(id.toString());
    }

    public record RideJoin(String rideId) {}

    public record RideDecision(String decision) {}

    @GetMapping("/rides")
    public Object rideList() {
        return rides.list();
    }

    @PostMapping("/rides")
    public Object rideCreate(
            @RequestBody RideOffer ride,
            HttpServletRequest req) {

        return rides.create(require(req), ride);
    }

    @GetMapping("/ride-bookings")
    public Object bookingList(HttpServletRequest req) {
        return rides.bookings(require(req));
    }

    @PostMapping("/ride-bookings")
    public Object joinRide(
            @RequestBody RideJoin x,
            HttpServletRequest req) {

        return rides.join(require(req), x.rideId());
    }

    @PostMapping("/ride-bookings/{id}/decision")
    public Object decide(
            @PathVariable String id,
            @RequestBody RideDecision d,
            HttpServletRequest req) {

        return rides.decision(
                require(req),
                id,
                d.decision());
    }
}