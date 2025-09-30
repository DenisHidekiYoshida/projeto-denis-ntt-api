package br.com.ntt.bank.controllers;

import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.domain.requests.DepositRequest;
import br.com.ntt.bank.domain.requests.PaymentRequest;
import br.com.ntt.bank.domain.responses.BalanceResponse;
import br.com.ntt.bank.services.command.TransactionCommandService;
import br.com.ntt.bank.services.query.AccountQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private TransactionCommandService transactionCommandService;

    @Mock
    private AccountQueryService accountQueryService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountController = new AccountController(transactionCommandService, accountQueryService, userRepository);

        when(userDetails.getUsername()).thenReturn("joao123");

        User user = new User();
        user.setId(1L);
        user.setLogin("joao123");

        when(userRepository.findByLogin("joao123")).thenReturn(Optional.of(user));
    }

    @Test
    void shouldDepositSuccessfully() {
        DepositRequest request = new DepositRequest();
        request.setAmount(BigDecimal.valueOf(100));

        ResponseEntity<String> response = accountController.deposit(userDetails, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deposito realizado com sucesso", response.getBody());
        verify(transactionCommandService).deposit(1L, BigDecimal.valueOf(100));
    }

    @Test
    void shouldPayBillSuccessfully() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(BigDecimal.valueOf(50));
        request.setDescription("Conta de luz");

        ResponseEntity<String> response = accountController.payment(userDetails, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pagamento realizado com sucesso", response.getBody());
        verify(transactionCommandService).payBill(1L, BigDecimal.valueOf(50), "Conta de luz");
    }

    @Test
    void shouldReturnBalanceAndHistory() {
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setSaldoTotal("1000");

        when(accountQueryService.getBalanceAndHistory(1L)).thenReturn(balanceResponse);

        BalanceResponse response = accountController.balance(userDetails);

        assertNotNull(response);
        assertEquals("1000", response.getSaldoTotal());
        verify(accountQueryService).getBalanceAndHistory(1L);
    }
}
