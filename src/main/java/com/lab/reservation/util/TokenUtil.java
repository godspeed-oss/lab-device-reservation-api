package com.lab.reservation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.reservation.dto.CurrentUser;
import com.lab.reservation.entity.User;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
    private static final String SECRET = "lab-device-reservation-secret-key";
    private static final long EXPIRE_SECONDS = 60 * 60 * 24;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private TokenUtil() {
    }

    public static String generateToken(User user) {
        try {
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", user.getId());
            payload.put("username", user.getUsername());
            payload.put("role", user.getRole());
            payload.put("exp", Instant.now().getEpochSecond() + EXPIRE_SECONDS);

            String encodedHeader = base64UrlEncode(OBJECT_MAPPER.writeValueAsString(header));
            String encodedPayload = base64UrlEncode(OBJECT_MAPPER.writeValueAsString(payload));
            String signature = sign(encodedHeader + "." + encodedPayload);

            return encodedHeader + "." + encodedPayload + "." + signature;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to generate token", exception);
        }
    }

    public static CurrentUser parseToken(String token) {
        try {
            String[] parts = token.split("\\.");

            if (parts.length != 3) {
                return null;
            }

            String unsignedToken = parts[0] + "." + parts[1];
            String expectedSignature = sign(unsignedToken);

            if (!expectedSignature.equals(parts[2])) {
                return null;
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            Map<?, ?> payload = OBJECT_MAPPER.readValue(payloadJson, Map.class);

            long exp = Long.parseLong(payload.get("exp").toString());

            if (Instant.now().getEpochSecond() > exp) {
                return null;
            }

            Integer userId = Integer.valueOf(payload.get("userId").toString());
            String username = payload.get("username").toString();
            String role = payload.get("role").toString();

            return new CurrentUser(userId, username, role);
        } catch (Exception exception) {
            return null;
        }
    }

    private static String sign(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);

        byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
    }

    private static String base64UrlEncode(String value) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}