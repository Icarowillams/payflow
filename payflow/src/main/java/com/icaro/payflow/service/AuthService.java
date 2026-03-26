package com.icaro.payflow.service;

import com.icaro.payflow.dto.LoginRequest;
import com.icaro.payflow.dto.LoginResponse;
import com.icaro.payflow.entity.User;
import com.icaro.payflow.exception.BusinessException;
import com.icaro.payflow.repository.UserRepository;
import com.icaro.payflow.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("E-mail ou senha inválidos."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("E-mail ou senha inválidos.");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token);
    }
}