package com.nexride.Controller;

import com.nexride.model.User;
import com.nexride.repository.StateRepository;
import com.nexride.service.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final StateRepository repo;
    private final UserService users;

    // Inject repository and user services
    public AdminController(
            StateRepository repo,
            UserService users) {

        this.repo = repo;
        this.users = users;
    }

    // Validate session and enforce admin access
    private User require(HttpServletRequest req) {
        Object id = req.getSession(false) == null
                ? null
                : req.getSession(false).getAttribute("userId");

        if (id == null) {
            throw ApiException.unauthorized();
        }

        User user = users.byId(id.toString());

        if (user == null) {
            throw ApiException.unauthorized();
        }

        UserService.requireAdmin(user);

        return user;
    }

    // Map user details to a response view
    private Map<String, Object> view(User u) {
        Map<String, Object> x = new java.util.LinkedHashMap<>();

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

    public record UserStatus(boolean active) {}

    // Get administrative summary metrics
    @GetMapping("/summary")
    public Object adminSummary(HttpServletRequest req) {

        require(req);

        return repo.read(s ->
                Map.of(
                        "vehicles", s.getVehicles().size(),

                        "pendingRentals",
                        s.getRentals().stream()
                                .filter(x -> "PENDING".equals(x.getStatus()))
                                .count(),

                        "openIssues",
                        s.getIssues().stream()
                                .filter(x -> !"RESOLVED".equals(x.getStatus()))
                                .count(),

                        "unpaidInvoices",
                        s.getInvoices().stream()
                                .filter(x -> "UNPAID".equals(x.getStatus()))
                                .count(),

                        "users", s.getUsers().size()));
    }

    // List all customer users
    @GetMapping("/users")
    public Object adminUsers(HttpServletRequest req) {

        require(req);

        return users.customers(require(req))
                .stream()
                .map(this::view)
                .toList();
    }

    // Update user active status
    @PutMapping("/users/{id}/status")
    public Object adminUserStatus(
            @PathVariable String id,
            @RequestBody UserStatus status,
            HttpServletRequest req) {

        return view(users.active(
                require(req),
                id,
                status.active()));
    }
}
