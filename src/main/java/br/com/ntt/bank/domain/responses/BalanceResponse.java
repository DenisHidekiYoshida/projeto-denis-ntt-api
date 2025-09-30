package br.com.ntt.bank.domain.responses;

import br.com.ntt.bank.domain.dto.TransactionDto;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class BalanceResponse {

    private String saldoTotal;

    private List<TransactionDto> historico;
}
