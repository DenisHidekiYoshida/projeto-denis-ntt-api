package br.com.ntt.bank.controllers;

import br.com.ntt.bank.configs.JwtUtils;
import br.com.ntt.bank.domain.dto.RegisterDto;
import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.domain.requests.AuthRequest;
import br.com.ntt.bank.domain.responses.AuthResponse;
import br.com.ntt.bank.services.command.UserCommandService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    private final UserCommandService userCommandService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserCommandService userCommandService, AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userCommandService = userCommandService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterDto dto) {
        try {
            User u = userCommandService.register(dto);
            return ResponseEntity.status(201).body("User created with id " + u.getId());
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        String login = req.getLogin();
        String password = req.getPassword();
        try {
            User user = userRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(password, user.getPasswordHash())) {
                throw new RuntimeException("Invalid password");
            }
            String token = jwtUtils.generateToken(req.getLogin());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Credencial invalida, usuario ou senha incorretos");
        }
    }
}
