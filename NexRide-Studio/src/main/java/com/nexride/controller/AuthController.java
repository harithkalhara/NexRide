package com.nexride.controller;

import com.nexride.model.User;
import com.nexride.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService users;

    public AuthController(UserService users) {
        this.users = users;
    }

    private User current(HttpServletRequest req) {
        Object id = req.getSession(false) == null
                ? null
                : req.getSession(false).getAttribute("userId");

        return id == null ? null : users.byId(id.toString());
    }

    private Map<String, Object> view(User u) {
        Map<String, Object> x = new LinkedHashMap<>();
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

    private String token(HttpSession s) {
        String v = (String) s.getAttribute("csrf");

        if (v == null) {
            v = UUID.randomUUID().toString();
            s.setAttribute("csrf", v);
        }

        return v;
    }

    public record Login(String identifier, String password) {}

    public record Register(
            String name,
            String email,
            String phone,
            String password,
            String country) {}

    @GetMapping("/session")
    public Object session(HttpServletRequest req) {
        HttpSession s = req.getSession(true);
        User u = current(req);

        return Map.of(
                "user", u == null ? Map.of() : view(u),
                "csrf", token(s));
    }

    @PostMapping("/register")
    public Object register(@RequestBody Register b) {
        User u = users.register(
                b.name(),
                b.email(),
                b.phone(),
                b.password(),
                b.country() == null ? "Sri Lanka" : b.country());

        return Map.of("user", view(u));
    }

    @PostMapping("/login")
    public Object login(
            @RequestBody Login b,
            HttpServletRequest req) {

        User u = users.login(
                b.identifier(),
                b.password(),
                "CUSTOMER");

        req.getSession(true);
        req.changeSessionId();

        HttpSession s = req.getSession();
        s.setAttribute("userId", u.getId());
        s.removeAttribute("csrf");

        return Map.of(
                "user", view(u),
                "csrf", token(s));
    }

    @PostMapping("/admin/login")
    public Object adminLogin(
            @RequestBody Login b,
            HttpServletRequest req) {

        User u = users.login(
                b.identifier(),
                b.password(),
                "ADMIN");

        req.getSession(true);
        req.changeSessionId();

        HttpSession s = req.getSession();
        s.setAttribute("userId", u.getId());
        s.removeAttribute("csrf");

        return Map.of(
                "user", view(u),
                "csrf", token(s));
    }

    @PostMapping("/logout")
    public Object logout(HttpServletRequest req) {
        if (req.getSession(false) != null) {
            req.getSession(false).invalidate();
        }

        return Map.of("ok", true);
    }
}