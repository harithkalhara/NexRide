package com.nexride.service;

import com.nexride.model.*;
import java.util.List;
import java.util.Objects;

public final class UserService {
    private UserService() {}

    public static void requireAdmin(User user) {
        if (user == null || !"ADMIN".equals(user.getRole())) {
            throw ApiException.forbidden("Administrator access required.");
        }
    }

    public static <T extends BaseEntity> T find(List<T> items, String id) {
        return items.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElseThrow(() -> ApiException.bad("Record not found."));
    }

    public static ApiException forbidden(String message) {
        return ApiException.forbidden(message);
    }

    public static String clean(String value, int maxLength) {
        if (value == null) return "";
        String cleaned = value.trim();
        return cleaned.length() <= maxLength ? cleaned : cleaned.substring(0, maxLength);
    }
}
