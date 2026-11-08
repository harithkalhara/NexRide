package com.nexride.Service;

import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    // Instance Variable
    private final StateRepository repo;

    // Constructor Injection
    public SettingsService(StateRepository r) {
        repo = r;
    }

    // Retrieve current site settings
    public SiteSettings get() {
        return repo.read(AppState::getSettings);
    }

    // Save and update site settings with validation (Admin only)
    public SiteSettings save(User actor, SiteSettings input) {
        UserService.requireAdmin(actor);
        if (input.getLkrPerUsd() == null
                || input.getLkrPerUsd().signum() < 0
                || input.getLkrPerUsd().compareTo(new BigDecimal("10000")) > 0)
            throw ApiException.bad("Exchange rate must be 0–10,000 LKR per USD.");
        if (!input.getContactEmail().isBlank()
                && !input.getContactEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            throw ApiException.bad("Enter a valid contact email.");
        return repo.update(
                s -> {
                    SiteSettings x = s.getSettings();
                    x.setTagline(UserService.clean(input.getTagline(), 100));
                    x.setLogoUrl(UserService.safeMediaOrAsset(input.getLogoUrl()));
                    x.setContactEmail(input.getContactEmail().strip());
                    x.setContactPhone(input.getContactPhone().strip());
                    x.setAddress(input.getAddress().strip());
                    x.setHours(input.getHours().strip());
                    x.setInstagramUrl(https(input.getInstagramUrl()));
                    x.setFacebookUrl(https(input.getFacebookUrl()));
                    if (x.getLkrPerUsd().compareTo(input.getLkrPerUsd()) != 0) {
                        x.setLkrPerUsd(input.getLkrPerUsd());
                        x.setRateUpdatedAt(Instant.now().toString());
                        x.setRateSource("Admin published rate");
                    }
                    return x;
                });
    }

    // Helper method to validate HTTPS format for social links
    private String https(String v) {
        if (v == null || v.isBlank()) return "";
        if (!v.matches("https://[A-Za-z0-9][^\\s<>\\\"']*"))
            throw ApiException.bad("Social links must use HTTPS.");
        return v;
    }
}
