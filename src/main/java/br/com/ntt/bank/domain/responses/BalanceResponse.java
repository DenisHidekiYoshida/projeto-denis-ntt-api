package br.com.ntt.bank.domain.responses;

import br.com.ntt.bank.domain.dto.TransactionDto;
import lombok.Data;

import java.util.List;

@Data
public class BalanceResponse {

    private String saldoTotal;

    public List<TransactionDto> getHistorico() {
        return historico;
    }

    public void setHistorico(List<TransactionDto> historico) {
        this.historico = historico;
    }

    private List<TransactionDto> historico;

    public String getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(String saldoTotal) {
        this.saldoTotal = saldoTotal;
    }
}
