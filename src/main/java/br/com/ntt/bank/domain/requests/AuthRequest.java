package br.com.ntt.bank.domain.requests;

import lombok.Data;

@Data
public class AuthRequest {

    private String login;

    private String password;
}
