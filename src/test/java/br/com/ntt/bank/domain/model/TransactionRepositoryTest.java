package br.com.ntt.bank.domain.model;

import br.com.ntt.bank.domain.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    void shouldPersistAndRetrieveTransaction() {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal("500.00"));
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setDescription("Compra no mercado");

        Transaction saved = repository.save(transaction);

        assertThat(saved.getId()).isNotNull();

        Transaction found = repository.findById(saved.getId()).orElseThrow();

        assertEquals(TransactionType.DEPOSIT, found.getType());
        assertThat(found.getAmount()).isEqualByComparingTo("500.00");
        assertThat(found.getDescription()).isEqualTo("Compra no mercado");
    }
}
