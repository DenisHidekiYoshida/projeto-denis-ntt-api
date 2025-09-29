package br.com.ntt.bank.domain.dto;

import lombok.Data;

@Data
public class TransactionDto {

    private String type;

    private String valor;

    private String data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
