package br.com.ntt.bank.domain.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {

    @NotNull(message = "O valor do deposito e obrigatorio")
    @DecimalMin(value = "0.01", message = "O deposito deve ser maior que zero")
    private BigDecimal amount;
}
