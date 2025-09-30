package br.com.ntt.bank.controllers;

import br.com.ntt.bank.configs.JwtUtils;
import br.com.ntt.bank.domain.dto.RegisterDto;
import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.domain.requests.AuthRequest;
import br.com.ntt.bank.domain.responses.AuthResponse;
import br.com.ntt.bank.services.command.UserCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserCommandService userCommandService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(userCommandService, jwtUtils, userRepository, passwordEncoder);
    }

    // ---------- TESTES REGISTER ----------

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterDto dto = new RegisterDto();
        dto.setFullName("Joao");
        dto.setCpf("12345678900");
        dto.setLogin("joao123");
        dto.setPassword("123");

        User user = new User();
        user.setId(1L);

        when(userCommandService.register(dto)).thenReturn(user);

        ResponseEntity<?> response = authController.register(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("User created with id 1"));
    }

    @Test
    void shouldReturnBadRequestWhenRegisterFails() {
        RegisterDto dto = new RegisterDto();
        dto.setLogin("joao123");

        when(userCommandService.register(dto)).thenThrow(new IllegalArgumentException("Usuario ja existe"));

        ResponseEntity<?> response = authController.register(dto);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Usuario ja existe", response.getBody());
    }

    // ---------- TESTES LOGIN ----------

    @Test
    void shouldLoginSuccessfully() {
        AuthRequest req = new AuthRequest();
        req.setLogin("joao123");
        req.setPassword("123");

        User user = new User();
        user.setLogin("joao123");
        user.setPasswordHash("hashed123");

        when(userRepository.findByLogin("joao123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123", "hashed123")).thenReturn(true);
        when(jwtUtils.generateToken("joao123")).thenReturn("fake-jwt");

        ResponseEntity<?> response = authController.login(req);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof AuthResponse);
        assertEquals("fake-jwt", ((AuthResponse) response.getBody()).getToken());
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordIsInvalid() {
        AuthRequest req = new AuthRequest();
        req.setLogin("joao123");
        req.setPassword("wrong");

        User user = new User();
        user.setLogin("joao123");
        user.setPasswordHash("hashed123");

        when(userRepository.findByLogin("joao123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed123")).thenReturn(false);

        ResponseEntity<?> response = authController.login(req);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Credencial invalida, usuario ou senha incorretos", response.getBody());
    }

    @Test
    void shouldReturnUnauthorizedWhenUserNotFound() {
        AuthRequest req = new AuthRequest();
        req.setLogin("notfound");
        req.setPassword("123");

        when(userRepository.findByLogin("notfound")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.login(req);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Credencial invalida, usuario ou senha incorretos", response.getBody());
    }
}
