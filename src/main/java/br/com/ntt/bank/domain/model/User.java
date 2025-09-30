package br.com.ntt.bank.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String login;

    private String passwordHash;

    @Column(unique = true)
    private String cpf;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
}
