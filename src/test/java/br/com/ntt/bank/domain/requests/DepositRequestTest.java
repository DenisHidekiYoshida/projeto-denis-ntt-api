package br.com.ntt.bank.domain.requests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DepositRequestTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldAcceptValidAmount() {
        DepositRequest request = new DepositRequest();
        request.setAmount(new BigDecimal("100.50"));

        Set<ConstraintViolation<DepositRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Nao deve haver violacoes para valor valido");
    }

    @Test
    void shouldFailWhenAmountIsNull() {
        DepositRequest request = new DepositRequest(); // amount null

        Set<ConstraintViolation<DepositRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O valor do deposito e obrigatorio", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailWhenAmountIsLessThanMinimum() {
        DepositRequest request = new DepositRequest();
        request.setAmount(new BigDecimal("0.00"));

        Set<ConstraintViolation<DepositRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O deposito deve ser maior que zero", violations.iterator().next().getMessage());
    }
}
