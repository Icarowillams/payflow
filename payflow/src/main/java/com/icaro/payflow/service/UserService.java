package com.icaro.payflow.service;

import com.icaro.payflow.dto.CreateUserRequest;
import com.icaro.payflow.dto.UserResponse;
import com.icaro.payflow.entity.User;
import com.icaro.payflow.exception.BusinessException;
import com.icaro.payflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("E-mail já cadastrado.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(BigDecimal.ZERO);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getBalance()
        );
    }

    public UserResponse findByEmail(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

    return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getBalance()
        );
    }
}