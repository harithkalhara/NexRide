package com.nexride.Service;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    // Instance Variable
    private final StateRepository repo;

    // Constructor Injection
    public FeedbackService(StateRepository r) {
        repo = r;
    }

    // Retrieve all reviews
    public List<Review> reviews() {
        return repo.read(AppState::getReviews);
    }

    // Create a new vehicle review with validation
    public Review review(User u, String vehicleId, int stars, String comment) {
        if (stars < 1 || stars > 5 || comment == null || comment.isBlank() || comment.length() > 1200)
            throw ApiException.bad("Enter a rating and a comment under 1,200 characters.");
        boolean eligible =
                repo.read(
                        s ->
                                s.getRentals().stream()
                                        .anyMatch(
                                                r ->
                                                        r.getUserId().equals(u.getId())
                                                                && r.getVehicleId().equals(vehicleId)
                                                                && "COMPLETED".equals(r.getStatus())));
        if (!eligible)
            throw ApiException.forbidden("A completed rental is required to review this bike.");
        return repo.update(
                s -> {
                    Review x = new Review();
                    x.setUserId(u.getId());
                    x.setVehicleId(vehicleId);
                    x.setRating(stars);
                    x.setComment(comment.strip());
                    s.getReviews().add(x);
                    return x;
                });
    }

    // Get issues based on user role (Admin views all, user views own)
    public List<Issue> issues(User u) {
        return repo.read(
                s ->
                        s.getIssues().stream()
                                .filter(i -> "ADMIN".equals(u.getRole()) || i.getUserId().equals(u.getId()))
                                .toList());
    }

    // Create a new support issue ticket
    public Issue create(User u, String category, String subject, String description) {
        Issue i = new Issue();
        i.setUserId(u.getId());
        i.setCategory(UserService.clean(category, 40));
        i.setSubject(UserService.clean(subject, 120));
        i.setDescription(UserService.clean(description, 1800));
        return repo.update(
                s -> {
                    s.getIssues().add(i);
                    return i;
                });
    }

    // Resolve or update a support issue (Admin action)
    public Issue resolve(User admin, String id, String reply, String status) {
        UserService.requireAdmin(admin);
        if (!Set.of("OPEN", "IN_PROGRESS", "RESOLVED").contains(status))
            throw ApiException.bad("Choose a valid issue state.");
        return repo.update(
                s -> {
                    Issue i = UserService.find(s.getIssues(), id);
                    i.setReply(reply == null ? "" : reply.strip());
                    i.setStatus(status);
                    return i;
                });
    }
}
