package br.com.ntt.bank.domain.responses;

import br.com.ntt.bank.domain.dto.TransactionDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BalanceResponseTest {

    @Test
    void shouldSetAndGetSaldoTotalAndHistorico() {
        BalanceResponse response = new BalanceResponse();
        response.setSaldoTotal("1500.50");

        List<TransactionDto> historico = new ArrayList<>();
        TransactionDto t1 = new TransactionDto();
        t1.setType("DEBIT");
        t1.setValor("100.00");
        t1.setData("2025-09-30");

        historico.add(t1);

        response.setHistorico(historico);

        assertEquals("1500.50", response.getSaldoTotal());
        assertEquals(1, response.getHistorico().size());
        assertEquals("DEBIT", response.getHistorico().get(0).getType());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        BalanceResponse r1 = new BalanceResponse();
        r1.setSaldoTotal("500");

        BalanceResponse r2 = new BalanceResponse();
        r2.setSaldoTotal("500");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void shouldTestToStringContainsFields() {
        BalanceResponse response = new BalanceResponse();
        response.setSaldoTotal("1000");

        String str = response.toString();
        assertTrue(str.contains("1000"));
    }
}
