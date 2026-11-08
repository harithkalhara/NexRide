package com.nexride.controller;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import com.nexride.service.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BikeController {

    private final StateRepository repo;
    private final UserService users;
    private final FleetService fleet;
    private final RideService rides;
    private final FeedbackService feedback;
    private final SettingsService settings;
    private final OnlinePaymentService online;

    public BikeController(
            StateRepository repo,
            UserService users,
            FleetService fleet,
            RideService rides,
            FeedbackService feedback,
            SettingsService settings,
            OnlinePaymentService online) {

        this.repo = repo;
        this.users = users;
        this.fleet = fleet;
        this.rides = rides;
        this.feedback = feedback;
        this.settings = settings;
        this.online = online;
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

    @GetMapping("/public")
    public Object publicData() {
        return Map.of(
                "bikes", fleet.list(null, null, null),
                "rides", rides.list(),
                "reviews", feedback.reviews(),
                "settings", settings.get(),
                "paymentOptions", online.options());
    }

    @GetMapping("/bikes")
    public Object bikes(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String kind,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(defaultValue = "false") boolean all,
            HttpServletRequest req) {

        return all
                ? fleet.all(require(req))
                : fleet.list(q, kind, city, start, end);
    }

    @PostMapping("/bikes")
    public Object addBike(
            @RequestBody Vehicle bike,
            HttpServletRequest req) {

        return fleet.save(require(req), bike, null);
    }

    @PutMapping("/bikes/{id}")
    public Object editBike(
            @PathVariable String id,
            @RequestBody Vehicle bike,
            HttpServletRequest req) {

        return fleet.save(require(req), bike, id);
    }

    @DeleteMapping("/bikes/{id}")
    public Object deleteBike(
            @PathVariable String id,
            HttpServletRequest req) {

        UserService.requireAdmin(require(req));

        repo.update(s -> {
            Vehicle v = UserService.find(s.getVehicles(), id);

            if (s.getRentals().stream().anyMatch(x ->
                    x.getVehicleId().equals(id)
                            && Set.of("PENDING", "APPROVED", "ACTIVE")
                            .contains(x.getStatus()))) {

                throw ApiException.bad(
                        "A bike with active requests cannot be deleted. Retire it instead.");
            }

            s.getVehicles().remove(v);
            return null;
        });

        return Map.of("ok", true);
    }
}