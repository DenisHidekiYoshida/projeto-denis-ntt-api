package br.com.ntt.bank.controllers;

import br.com.ntt.bank.configs.JwtUtils;
import br.com.ntt.bank.domain.dto.RegisterDto;
import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.domain.requests.AuthRequest;
import br.com.ntt.bank.domain.responses.AuthResponse;
import br.com.ntt.bank.services.command.UserCommandService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserCommandService userCommandService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterDto dto) {
        try {
            User u = userCommandService.register(dto);

            log.info("Usuario criado: {}", u );
            return ResponseEntity.status(201).body("User created with id " + u.getId());
        }catch (IllegalArgumentException e) {
            log.error("Ocorreu erro ao registrar usuario: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        String login = req.getLogin();
        String password = req.getPassword();
        var mensagemErro = "Credencial invalida, usuario ou senha incorretos";

        try {
            User user = userRepository.findByLogin(login).orElse(null);

            if ((user == null || !passwordEncoder.matches(password, user.getPasswordHash()))) {
                return ResponseEntity.status(401).body(mensagemErro);
            }
            String token = jwtUtils.generateToken(req.getLogin());
            log.info("Token criado: {}", token);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException ex) {
            log.error("Erro ao gerar token: {}", ex.getMessage());
            return ResponseEntity.status(401).body(mensagemErro);
        }
    }
}
