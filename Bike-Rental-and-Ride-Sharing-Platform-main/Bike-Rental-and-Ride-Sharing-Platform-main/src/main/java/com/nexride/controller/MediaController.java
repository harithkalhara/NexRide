package com.nexride.controller;

import com.nexride.model.User;
import com.nexride.service.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class MediaController {

    private final UserService users;
    private final MediaService media;

    public MediaController(
            UserService users,
            MediaService media) {

        this.users = users;
        this.media = media;
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

    @PostMapping(
            value = "/media",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam(defaultValue = "profile") String purpose,
            HttpServletRequest req) throws IOException {

        User actor = require(req);

        if (!Set.of("profile", "logo", "bike").contains(purpose)) {
            throw ApiException.bad("Unknown image purpose.");
        }

        if (Set.of("logo", "bike").contains(purpose)) {
            UserService.requireAdmin(actor);
        }

        return Map.of("url", media.save(file));
    }
}