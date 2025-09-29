package br.com.ntt.bank.services.command;

import br.com.ntt.bank.domain.model.Transaction;
import br.com.ntt.bank.domain.model.TransactionType;
import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.TransactionRepository;
import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.services.query.AccountQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class TransactionCommandService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountQueryService accountQueryService;

    public TransactionCommandService(UserRepository userRepository, TransactionRepository transactionRepository, AccountQueryService accountQueryService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.accountQueryService = accountQueryService;
    }

    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        BigDecimal current = user.getBalance();
        if (current.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal negative = current.abs();
            BigDecimal debtWithFee = negative.multiply(new BigDecimal("1.02")).setScale(2, RoundingMode.HALF_UP);
            if (amount.compareTo(debtWithFee) >= 0) {
                BigDecimal remaining = amount.subtract(debtWithFee).setScale(2, RoundingMode.HALF_UP);
                user.setBalance(remaining);
            } else {
                user.setBalance(current.add(amount).setScale(2, RoundingMode.HALF_UP));
            }
        } else {
            user.setBalance(current.add(amount).setScale(2, RoundingMode.HALF_UP));
        }
        userRepository.save(user);

        Transaction t = new Transaction();
        t.setUser(user);
        t.setType(TransactionType.DEPOSIT);
        t.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
        t.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(t);

        accountQueryService.evictCacheForUser(userId);
    }

    @Transactional
    public void pay(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBalance(user.getBalance().subtract(amount).setScale(2, RoundingMode.HALF_UP));
        userRepository.save(user);

        Transaction t = new Transaction();
        t.setUser(user);
        t.setType(TransactionType.PAYMENT);
        t.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
        t.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(t);

        accountQueryService.evictCacheForUser(userId);
    }
}
