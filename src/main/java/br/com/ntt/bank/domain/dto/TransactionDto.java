package br.com.ntt.bank.domain.dto;

import lombok.Data;

@Data
public class TransactionDto {

    private String type;

    private String valor;

    private String data;
}
