package com.orrange.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class TokenUtils {
    public static String generateToken(String subject) {
        String raw = subject + ":" + UUID.randomUUID() + ":" + System.currentTimeMillis();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }
}
