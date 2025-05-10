package org.study.system.deepdivestudy.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CookieUtils {



    /** Сереалізувати й покласти об’єкт у cookie */
    public static void serialize(HttpServletResponse resp,
                                 String name,
                                 Object value,
                                 int maxAgeSeconds,
                                 boolean httpOnly,
                                 boolean secure) {

        String encoded = encode(value);
        Cookie cookie = new Cookie(name, encoded);
        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setMaxAge(maxAgeSeconds);
        resp.addCookie(cookie);
    }

    /** Десереалізувати cookie у зазначений тип */
    public static <T> T deserialize(HttpServletRequest req,
                                    String name,
                                    Class<T> targetType) {

        Cookie cookie = fetchCookie(req, name)
                .orElseThrow(() -> new IllegalArgumentException("Cookie %s not found".formatted(name)));

        return decode(cookie.getValue(), targetType);
    }

    /** Видалити cookie (у відповідь назад клієнту) */
    public static void delete(HttpServletRequest req,
                              HttpServletResponse resp,
                              String name) {

        fetchCookie(req, name).ifPresent(c -> {
            c.setValue("");
            c.setPath("/");
            c.setMaxAge(0);
            resp.addCookie(c);
        });
    }



    private static Optional<Cookie> fetchCookie(HttpServletRequest req, String name) {
        if (req.getCookies() == null) return Optional.empty();
        return Arrays.stream(req.getCookies())
                .filter(c -> name.equals(c.getName()))
                .findFirst();
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** → Base64-URL-safe JSON */
    private static String encode(Object obj) {
        try {
            String json = MAPPER.writeValueAsString(obj);
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize cookie value", e);
        }
    }

    /** ← Base64-URL-safe JSON */
    private static <T> T decode(String encoded, Class<T> targetType) {
        try {
            byte[] jsonBytes = Base64.getUrlDecoder().decode(encoded);
            return MAPPER.readValue(jsonBytes, targetType);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to deserialize cookie value", e);
        }
    }
}
