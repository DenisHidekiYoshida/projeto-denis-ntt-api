package br.com.ntt.bank.domain.requests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestTest {

    @Test
    void shouldSetAndGetLoginAndPassword() {
        AuthRequest request = new AuthRequest();
        request.setLogin("joao123");
        request.setPassword("senha123");

        assertEquals("joao123", request.getLogin());
        assertEquals("senha123", request.getPassword());
    }

    @Test
    void shouldHaveNullByDefault() {
        AuthRequest request = new AuthRequest();

        assertNull(request.getLogin());
        assertNull(request.getPassword());
    }

    @Test
    void shouldTestToString() {
        AuthRequest request = new AuthRequest();
        request.setLogin("user");
        request.setPassword("pwd");

        String str = request.toString();

        assertNotNull(str);
    }
}
