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

class PaymentRequestTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldAcceptValidPayment() {
        PaymentRequest request = new PaymentRequest();
        request.setDescription("Conta de luz");
        request.setAmount(new BigDecimal("150.50"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Não deve haver violacoes para valor valido");
    }

    @Test
    void shouldFailWhenDescriptionIsBlank() {
        PaymentRequest request = new PaymentRequest();
        request.setDescription(""); // em branco
        request.setAmount(new BigDecimal("100.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("A descricao da conta e obrigatoria", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailWhenAmountIsNull() {
        PaymentRequest request = new PaymentRequest();
        request.setDescription("Conta de agua");

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O valor do pagamento e obrigatorio", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailWhenAmountIsLessThanMinimum() {
        PaymentRequest request = new PaymentRequest();
        request.setDescription("Conta de internet");
        request.setAmount(new BigDecimal("0.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O pagamento deve ser maior que zero", violations.iterator().next().getMessage());
    }
}
