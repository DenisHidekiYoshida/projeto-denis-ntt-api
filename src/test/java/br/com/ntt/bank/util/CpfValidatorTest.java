package br.com.ntt.bank.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfValidatorTest {

    @Test
    void shouldReturnTrueForValidCpf() {
        // CPF valido
        assertTrue(CpfValidator.isValid("52998224725"));
        // CPF valido com pontos e traco
        assertTrue(CpfValidator.isValid("529.982.247-25"));
    }

    @Test
    void shouldReturnFalseForInvalidCpf() {
        // CPF invalido (digitos errados)
        assertFalse(CpfValidator.isValid("52998224724"));
        // CPF com tamanho errado
        assertFalse(CpfValidator.isValid("123456789"));
        // CPF com todos os digitos iguais
        assertFalse(CpfValidator.isValid("11111111111"));
    }

    @Test
    void shouldReturnFalseForNullCpf() {
        assertFalse(CpfValidator.isValid(null));
    }

    @Test
    void shouldReturnFalseForNonNumericCharacters() {
        assertFalse(CpfValidator.isValid("52998abc725"));
    }
}
