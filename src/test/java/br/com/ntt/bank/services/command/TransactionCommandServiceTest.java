package br.com.ntt.bank.services.command;

import br.com.ntt.bank.domain.model.Transaction;
import br.com.ntt.bank.domain.model.TransactionType;
import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.TransactionRepository;
import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.services.query.AccountQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionCommandServiceTest {

    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private AccountQueryService accountQueryService;
    private TransactionCommandService service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        accountQueryService = mock(AccountQueryService.class);
        service = new TransactionCommandService(userRepository, transactionRepository, accountQueryService);
    }

    @Test
    void shouldDepositSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setBalance(BigDecimal.ZERO);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        service.deposit(1L, BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100).setScale(2), user.getBalance());
        verify(userRepository).save(user);

        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(txCaptor.capture());
        assertEquals(TransactionType.DEPOSIT, txCaptor.getValue().getType());

        verify(accountQueryService).evictCacheForUser(1L);
    }

    @Test
    void shouldPayBillSuccessfully() {
        User user = new User();
        user.setId(2L);
        user.setBalance(BigDecimal.valueOf(200));

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        service.payBill(2L, BigDecimal.valueOf(50), "Conta de luz");

        assertEquals(BigDecimal.valueOf(150).setScale(2), user.getBalance());
        verify(userRepository).save(user);

        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(txCaptor.capture());
        assertEquals(TransactionType.PAYMENT, txCaptor.getValue().getType());
        assertEquals("Conta de luz", txCaptor.getValue().getDescription());

        verify(accountQueryService).evictCacheForUser(2L);
    }

    @Test
    void shouldDepositWhenUserHasDebt() {
        User user = new User();
        user.setId(3L);
        user.setBalance(BigDecimal.valueOf(-100)); // usuário deve 100

        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        service.deposit(3L, BigDecimal.valueOf(120));

        // divida de 100 com 2% de juros = 102
        // 120 - 102 = 18
        assertEquals(BigDecimal.valueOf(18.00).setScale(2), user.getBalance());

        verify(userRepository).save(user);

        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(txCaptor.capture());
        assertEquals(TransactionType.DEPOSIT, txCaptor.getValue().getType());
        assertEquals(BigDecimal.valueOf(120.00).setScale(2), txCaptor.getValue().getAmount());

        verify(accountQueryService).evictCacheForUser(3L);
    }


    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.deposit(99L, BigDecimal.TEN));

        assertThrows(IllegalArgumentException.class,
                () -> service.payBill(99L, BigDecimal.TEN, "Teste"));
    }
}
