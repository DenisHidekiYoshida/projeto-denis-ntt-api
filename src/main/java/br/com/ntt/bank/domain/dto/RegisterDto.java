package br.com.ntt.bank.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDto {

    @NotBlank(message = "Nome completo e obrigatorio")
    private String fullName;

    @NotBlank(message = "CPF e obrigatorio")
    private String cpf;

    @NotBlank(message = "Login e obrigatorio")
    private String login;

    @NotBlank(message = "Senha e obrigatoria")
    private String password;
}

