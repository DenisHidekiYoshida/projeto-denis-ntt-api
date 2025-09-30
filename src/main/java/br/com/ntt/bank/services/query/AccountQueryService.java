package br.com.ntt.bank.services.query;

import br.com.ntt.bank.domain.dto.TransactionDto;
import br.com.ntt.bank.domain.model.Transaction;
import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.TransactionRepository;
import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.domain.responses.BalanceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountQueryService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private final DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public AccountQueryService(UserRepository userRepository, TransactionRepository transactionRepository, RedisTemplate<String, Object> redisTemplate) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.redisTemplate = redisTemplate;
    }

    public BalanceResponse getBalanceAndHistory(Long userId) {
        String key = cacheKey(userId);
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) return (BalanceResponse) cached;

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Transaction> txs = transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        BalanceResponse out = new BalanceResponse();
        out.setSaldoTotal(user.getBalance().setScale(2, RoundingMode.HALF_UP).toString());
        List<TransactionDto> hist = txs.stream().map(t -> {
            TransactionDto d = new TransactionDto();
            d.setType(t.getType().name());
            d.setValor(t.getAmount().setScale(2, RoundingMode.HALF_UP).toString());
            d.setData(fmt.format(java.util.Date.from(t.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())));
            return d;
        }).collect(Collectors.toList());
        out.setHistorico(hist);
        log.info("Historico Balanco: " + out);

        redisTemplate.opsForValue().set(key, out, 60, TimeUnit.SECONDS);
        return out;
    }

    public void evictCacheForUser(Long userId) {
        redisTemplate.delete(cacheKey(userId));
    }

    private String cacheKey(Long userId) {
        return "account:balance:" + userId;
    }
}

