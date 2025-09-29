package br.com.ntt.bank.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDto {

    @NotBlank
    private String fullName;

    @NotBlank
    private String cpf;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    public String getFullName() {
        return fullName;
    }

    public String getCpf() {
        return cpf;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}

