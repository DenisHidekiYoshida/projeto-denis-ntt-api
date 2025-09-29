package br.com.ntt.bank.domain.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PaymentRequest {

    @NotBlank(message = "A descricao da conta e obrigatoria")
    private String description;

    @NotNull(message = "O valor do pagamento e obrigatorio")
    @DecimalMin(value = "0.01", message = "O pagamento deve ser maior que zero")
    private BigDecimal amount;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
