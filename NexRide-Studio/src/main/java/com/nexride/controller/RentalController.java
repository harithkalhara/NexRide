package com.nexride.controller;

import com.nexride.model.User;
import com.nexride.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RentalController {

    private final UserService users;
    private final RentalService rentals;

    public RentalController(
            UserService users,
            RentalService rentals) {
        this.users = users;
        this.rentals = rentals;
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

    public record RentalRequest(
            String vehicleId,
            String startDate,
            String endDate,
            String note) {}

    public record Action(
            String action,
            String note) {}

    @GetMapping("/rentals")
    public Object rentalList(HttpServletRequest req) {
        return rentals.list(require(req));
    }

    @PostMapping("/rentals")
    public Object rentalCreate(
            @RequestBody RentalRequest x,
            HttpServletRequest req) {

        return rentals.request(
                require(req),
                x.vehicleId(),
                x.startDate(),
                x.endDate(),
                x.note());
    }

    @PostMapping("/rentals/{id}/action")
    public Object rentalAction(
            @PathVariable String id,
            @RequestBody Action a,
            HttpServletRequest req) {

        return rentals.action(
                require(req),
                id,
                a.action(),
                a.note());
    }
}