package br.com.ntt.bank.domain.model;

import br.com.ntt.bank.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void shouldPersistAndRetrieveUser() {
        User user = new User();
        user.setFullName("Joao Silva");
        user.setLogin("joao123");
        user.setPasswordHash("hash123");
        user.setCpf("12345678900");
        user.setBalance(new BigDecimal("500.00"));

        User saved = repository.save(user);

        assertThat(saved.getId()).isNotNull();

        User found = repository.findById(saved.getId()).orElseThrow();

        assertThat(found.getFullName()).isEqualTo("Joao Silva");
        assertThat(found.getLogin()).isEqualTo("joao123");
        assertThat(found.getPasswordHash()).isEqualTo("hash123");
        assertThat(found.getCpf()).isEqualTo("12345678900");
        assertThat(found.getBalance()).isEqualByComparingTo("500.00");
    }
}
