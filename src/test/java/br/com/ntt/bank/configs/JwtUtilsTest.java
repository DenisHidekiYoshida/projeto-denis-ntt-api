package br.com.ntt.bank.configs;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JwtUtils();

        byte[] keyBytes = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded();
        String secretBase64 = Base64.getEncoder().encodeToString(keyBytes);

        Field secretField = JwtUtils.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, secretBase64);

        Field expField = JwtUtils.class.getDeclaredField("expirationMs");
        expField.setAccessible(true);
        expField.set(jwtUtils, 60_000L);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String token = jwtUtils.generateToken("testUser");

        assertNotNull(token);
        assertTrue(jwtUtils.validate(token));
        assertEquals("testUser", jwtUtils.getUsername(token));
    }

    @Test
    void shouldInvalidateWrongToken() {
        assertFalse(jwtUtils.validate("invalid.token.value"));
    }

    @Test
    void shouldInvalidateExpiredToken() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        Field expField = JwtUtils.class.getDeclaredField("expirationMs");
        expField.setAccessible(true);
        expField.set(jwtUtils, 10L);

        String token = jwtUtils.generateToken("expiredUser");
        Thread.sleep(20);

        assertFalse(jwtUtils.validate(token));
    }
}
