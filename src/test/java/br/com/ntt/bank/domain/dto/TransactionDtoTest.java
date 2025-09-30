package br.com.ntt.bank.domain.dto;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDtoTest {

    private final Random random = new Random();

    private String randomString() {
        return "str_" + random.nextInt(10000);
    }

    @Test
    void shouldCreateTransactionDtoWithRandomValues() {
        TransactionDto dto = new TransactionDto();
        dto.setType(randomString());
        dto.setValor(String.valueOf(random.nextInt(1000)));
        dto.setData("2025-09-30");

        assertNotNull(dto.getType());
        assertNotNull(dto.getValor());
        assertEquals("2025-09-30", dto.getData());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        TransactionDto dto1 = new TransactionDto();
        dto1.setType("DEBITO");
        dto1.setValor("100");
        dto1.setData("2025-09-30");

        TransactionDto dto2 = new TransactionDto();
        dto2.setType("DEBITO");
        dto2.setValor("100");
        dto2.setData("2025-09-30");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldTestToStringContainsFields() {
        TransactionDto dto = new TransactionDto();
        dto.setType("CREDITO");
        dto.setValor("250");
        dto.setData("2025-09-30");

        String toString = dto.toString();

        assertTrue(toString.contains("CREDITO"));
        assertTrue(toString.contains("250"));
        assertTrue(toString.contains("2025-09-30"));
    }
}
