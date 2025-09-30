package br.com.ntt.bank.services.command;

import br.com.ntt.bank.domain.dto.RegisterDto;
import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserCommandService service;

    private RegisterDto validDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validDto = new RegisterDto();
        validDto.setFullName("João Silva");
        validDto.setCpf("52998224725"); // CPF válido
        validDto.setLogin("joao123");
        validDto.setPassword("123");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.findByLogin("joao123")).thenReturn(Optional.empty());
        when(userRepository.findByCpf("52998224725")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123")).thenReturn("hashed123");

        User saved = new User();
        saved.setId(1L);
        saved.setLogin("joao123");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = service.register(validDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("joao123", result.getLogin());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("hashed123", captor.getValue().getPasswordHash());
    }

    @Test
    void shouldThrowWhenCpfInvalid() {
        validDto.setCpf("11111111111"); // CPF inválido

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.register(validDto)
        );

        assertEquals("CPF inválido", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenLoginAlreadyExists() {
        when(userRepository.findByLogin("joao123")).thenReturn(Optional.of(new User()));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.register(validDto)
        );

        assertEquals("Login já existe", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenCpfAlreadyExists() {
        when(userRepository.findByLogin("joao123")).thenReturn(Optional.empty());
        when(userRepository.findByCpf("52998224725")).thenReturn(Optional.of(new User()));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.register(validDto)
        );

        assertEquals("CPF já cadastrado", ex.getMessage());
        verify(userRepository, never()).save(any());
    }
}
