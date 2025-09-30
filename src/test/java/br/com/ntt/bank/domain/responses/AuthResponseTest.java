package br.com.ntt.bank.domain.responses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void shouldCreateAuthResponseWithAllArgsConstructor() {
        AuthResponse response = new AuthResponse("my-token-123");

        assertEquals("my-token-123", response.getToken());
    }

    @Test
    void shouldCreateAuthResponseWithNoArgsConstructorAndSetToken() {
        AuthResponse response = new AuthResponse();
        response.setToken("another-token-456");

        assertEquals("another-token-456", response.getToken());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        AuthResponse r1 = new AuthResponse("token1");
        AuthResponse r2 = new AuthResponse("token1");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void shouldTestToStringContainsToken() {
        AuthResponse response = new AuthResponse("token123");

        String str = response.toString();
        assertTrue(str.contains("token123"));
    }
}
