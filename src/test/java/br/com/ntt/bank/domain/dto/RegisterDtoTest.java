package br.com.ntt.bank.domain.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterDtoTest {

    private Validator validator;
    private Random random;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        random = new Random();
    }

    private String randomString() {
        return "str_" + random.nextInt(10000);
    }

    @Test
    void shouldCreateValidRegisterDto() {
        RegisterDto dto = new RegisterDto();
        dto.setFullName(randomString());
        dto.setCpf(randomString());
        dto.setLogin(randomString());
        dto.setPassword(randomString());

        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Nao deve haver violacoes de validacao");
    }

    @Test
    void shouldFailWhenFieldsAreBlank() {
        RegisterDto dto = new RegisterDto();

        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Deve haver violacoes de validacao");
        assertEquals(4, violations.size(), "Todos os 4 campos obrigatorios devem falhar");
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        RegisterDto dto1 = new RegisterDto();
        dto1.setFullName("Nome");
        dto1.setCpf("123");
        dto1.setLogin("login");
        dto1.setPassword("senha");

        RegisterDto dto2 = new RegisterDto();
        dto2.setFullName("Nome");
        dto2.setCpf("123");
        dto2.setLogin("login");
        dto2.setPassword("senha");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldTestToString() {
        RegisterDto dto = new RegisterDto();
        dto.setFullName("Nome Teste");
        dto.setCpf("123456");
        dto.setLogin("user");
        dto.setPassword("pwd");

        String toString = dto.toString();
        assertTrue(toString.contains("Nome Teste"));
        assertTrue(toString.contains("123456"));
    }
}
