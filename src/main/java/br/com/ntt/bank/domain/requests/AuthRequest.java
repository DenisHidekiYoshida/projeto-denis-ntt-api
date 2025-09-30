package br.com.ntt.bank.domain.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    private String login;

    private String password;
}
