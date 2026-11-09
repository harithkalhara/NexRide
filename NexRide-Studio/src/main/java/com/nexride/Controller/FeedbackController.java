package com.nexride.Controller;

import com.nexride.model.User;
import com.nexride.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    private final UserService users;
    private final FeedbackService feedback;

    public FeedbackController(
            UserService users,
            FeedbackService feedback) {

        this.users = users;
        this.feedback = feedback;
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

    public record ReviewRequest(
            String vehicleId,
            int rating,
            String comment) {}

    public record IssueRequest(
            String category,
            String subject,
            String description) {}

    public record IssueDecision(
            String reply,
            String status) {}

    @GetMapping("/reviews")
    public Object reviewList() {
        return feedback.reviews();
    }

    @PostMapping("/reviews")
    public Object createReview(
            @RequestBody ReviewRequest r,
            HttpServletRequest req) {

        return feedback.review(
                require(req),
                r.vehicleId(),
                r.rating(),
                r.comment());
    }

    @GetMapping("/issues")
    public Object issueList(HttpServletRequest req) {
        return feedback.issues(require(req));
    }

    @PostMapping("/issues")
    public Object createIssue(
            @RequestBody IssueRequest i,
            HttpServletRequest req) {

        return feedback.create(
                require(req),
                i.category(),
                i.subject(),
                i.description());
    }

    @PutMapping("/issues/{id}/decision")
    public Object issueDecision(
            @PathVariable String id,
            @RequestBody IssueDecision d,
            HttpServletRequest req) {

        return feedback.resolve(
                require(req),
                id,
                d.reply(),
                d.status());
    }
}
