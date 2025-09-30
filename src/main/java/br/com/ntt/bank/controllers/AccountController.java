package br.com.ntt.bank.controllers;

import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.domain.requests.DepositRequest;
import br.com.ntt.bank.domain.requests.PaymentRequest;
import br.com.ntt.bank.domain.responses.BalanceResponse;
import br.com.ntt.bank.services.command.TransactionCommandService;
import br.com.ntt.bank.services.query.AccountQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AccountController {

    private final TransactionCommandService transactionCommandService;
    private final AccountQueryService accountQueryService;
    private final UserRepository userRepository;

    private Long resolveUserId(UserDetails userDetails) {
        return userRepository.findByLogin(userDetails.getUsername()).orElseThrow().getId();
    }

    @PostMapping("/deposit")
    @Operation(summary = "Depositar dinheiro na conta")
    @ApiResponse(responseCode = "200", description = "Deposito realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validacao ou regra de negocio",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<String> deposit(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated @RequestBody DepositRequest request) {

        Long userId = resolveUserId(userDetails);
        transactionCommandService.deposit(userId, request.getAmount());
        return ResponseEntity.ok("Deposito realizado com sucesso");
    }

    @PostMapping("/payment")
    @Operation(summary = "Pagar uma conta (saldo pode ficar negativo)")
    @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validacao ou regra de negocio",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<String> payment(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated @RequestBody PaymentRequest request) {

        Long userId = resolveUserId(userDetails);
        transactionCommandService.payBill(userId, request.getAmount(), request.getDescription());
        return ResponseEntity.ok("Pagamento realizado com sucesso");
    }

    @GetMapping("/balance")
    @Operation(summary = "Consultar historico de uma conta")
    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validacao ou regra de negocio",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public BalanceResponse balance(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = resolveUserId(userDetails);
        return accountQueryService.getBalanceAndHistory(userId);
    }

}
