package br.com.ntt.bank.services.command;

import br.com.ntt.bank.domain.model.Transaction;
import br.com.ntt.bank.domain.model.TransactionType;
import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.TransactionRepository;
import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.services.query.AccountQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionCommandService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountQueryService accountQueryService;

    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        if (user.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal debt = user.getBalance().abs();
            BigDecimal debtWithInterest = debt.multiply(BigDecimal.valueOf(1.02))
                    .setScale(2, RoundingMode.HALF_UP);

            if (amount.compareTo(debtWithInterest) >= 0) {
                user.setBalance(amount.subtract(debtWithInterest));
            } else {
                user.setBalance(amount.subtract(debtWithInterest));
            }
        } else {
            user.setBalance(user.getBalance().add(amount));
        }

        userRepository.save(user);

        Transaction t = new Transaction();
        t.setUser(user);
        t.setType(TransactionType.DEPOSIT);
        t.setAmount(amount);
        t.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(t);

        log.info("Deposit: {}", t);

        accountQueryService.evictCacheForUser(userId);
    }

    @Transactional
    public void payBill(Long userId, BigDecimal amount, String description) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);

        Transaction t = new Transaction();
        t.setUser(user);
        t.setType(TransactionType.PAYMENT);
        t.setAmount(amount);
        t.setDescription(description);
        t.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(t);

        log.info("Payment: {}", t);

        accountQueryService.evictCacheForUser(userId);
    }
}
