package br.com.ntt.bank.domain.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DepositRequest {

    @NotNull(message = "O valor do deposito e obrigatorio")
    @DecimalMin(value = "0.01", message = "O deposito deve ser maior que zero")
    private BigDecimal amount;

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
