package br.com.ntt.bank.services.command;

import br.com.ntt.bank.domain.dto.RegisterDto;
import br.com.ntt.bank.domain.model.User;
import br.com.ntt.bank.domain.repository.UserRepository;
import br.com.ntt.bank.util.CpfValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(RegisterDto dto) {
        if (!CpfValidator.isValid(dto.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }
        if (userRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Login já existe");
        }
        if (userRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        User u = new User();
        u.setFullName(dto.getFullName());
        u.setCpf(dto.getCpf());
        u.setLogin(dto.getLogin());
        u.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        u.setBalance(java.math.BigDecimal.ZERO);
        return userRepository.save(u);
    }
}
