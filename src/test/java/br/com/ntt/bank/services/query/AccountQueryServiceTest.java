package br.com.ntt.bank.services.query;

import br.com.ntt.bank.domain.dto.TransactionDto;
import br.com.ntt.bank.domain.model.Transaction;
import br.com.ntt.bank.domain.model.TransactionType;
import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.TransactionRepository;
import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.domain.responses.BalanceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private AccountQueryService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void shouldReturnCachedBalanceResponse() {
        BalanceResponse cached = new BalanceResponse();
        cached.setSaldoTotal("100.00");

        when(valueOperations.get("account:balance:1")).thenReturn(cached);

        BalanceResponse result = service.getBalanceAndHistory(1L);

        assertEquals("100.00", result.getSaldoTotal());
        verify(userRepository, never()).findById(anyLong());
        verify(transactionRepository, never()).findByUserIdOrderByCreatedAtDesc(anyLong());
    }

    @Test
    void shouldFetchFromDatabaseWhenCacheIsEmpty() {
        User user = new User();
        user.setId(1L);
        user.setBalance(BigDecimal.valueOf(200));

        Transaction tx = new Transaction();
        tx.setId(10L);
        tx.setUser(user);
        tx.setType(TransactionType.DEPOSIT);
        tx.setAmount(BigDecimal.valueOf(50));
        tx.setCreatedAt(LocalDateTime.now());

        when(valueOperations.get("account:balance:1")).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(transactionRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(tx));

        BalanceResponse result = service.getBalanceAndHistory(1L);

        assertEquals("200.00", result.getSaldoTotal());
        assertEquals(1, result.getHistorico().size());
        TransactionDto dto = result.getHistorico().get(0);
        assertEquals("DEPOSIT", dto.getType());
        assertEquals("50.00", dto.getValor());

        verify(valueOperations).set(eq("account:balance:1"), any(BalanceResponse.class), eq(60L), eq(java.util.concurrent.TimeUnit.SECONDS));
    }

    @Test
    void shouldEvictCacheForUser() {
        service.evictCacheForUser(2L);
        verify(redisTemplate).delete("account:balance:2");
    }
}
