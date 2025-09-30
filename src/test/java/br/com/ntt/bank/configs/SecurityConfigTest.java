package br.com.ntt.bank.configs;

import br.com.ntt.bank.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void userDetailsServiceShouldLoadUser() {
        var user = new br.com.ntt.bank.domain.model.User();
        user.setLogin("joao");
        user.setPasswordHash("123");

        when(userRepository.findByLogin("joao")).thenReturn(Optional.of(user));

        var userDetails = securityConfig.userDetailsService().loadUserByUsername("joao");

        assertEquals("joao", userDetails.getUsername());
        assertEquals("123", userDetails.getPassword());
    }
}
