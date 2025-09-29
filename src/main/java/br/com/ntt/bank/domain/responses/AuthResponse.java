package br.com.ntt.bank.domain.responses;

public class AuthResponse {

    public AuthResponse() {
    }

    public AuthResponse(String token) {
        this.token = token;
    }

    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
