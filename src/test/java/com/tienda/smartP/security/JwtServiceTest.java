package com.tienda.smartP.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final String SECRET = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

    @Test
    void generatesAndValidatesTokenWithConfiguredSecret() {
        JwtService jwtService = new JwtService(SECRET);

        String token = jwtService.generateToken("admin");

        assertThat(jwtService.extractUsername(token)).isEqualTo("admin");
        assertThat(jwtService.isTokenValid(token, "admin")).isTrue();
    }

    @Test
    void rejectsTokenSignedWithRetiredSecret() {
        JwtService jwtService = new JwtService(SECRET);
        String retiredSecret = "retired-secret-that-is-long-enough-for-hs256-signing-key";
        String token = Jwts.builder()
                .setSubject("admin")
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(Keys.hmacShaKeyFor(retiredSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        assertThatThrownBy(() -> jwtService.extractUsername(token)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void rejectsMissingOrInvalidSecretAtStartup() {
        assertThatIllegalStateException().isThrownBy(() -> new JwtService(null));
        assertThatIllegalStateException().isThrownBy(() -> new JwtService(Base64.getEncoder().encodeToString(new byte[16])));
    }
}
