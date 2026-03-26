package com.icaro.payflow;

import com.icaro.payflow.dto.LoginRequest;
import com.icaro.payflow.dto.LoginResponse;
import com.icaro.payflow.entity.User;
import com.icaro.payflow.exception.BusinessException;
import com.icaro.payflow.repository.UserRepository;
import com.icaro.payflow.security.JwtService;
import com.icaro.payflow.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveRealizarLoginComSucesso() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("icaro@email.com");
        request.setPassword("123456");

        User user = new User();
        user.setEmail("icaro@email.com");
        user.setPassword("senha-criptografada");

        when(userRepository.findByEmail("icaro@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "senha-criptografada")).thenReturn(true);
        when(jwtService.generateToken("icaro@email.com")).thenReturn("token-fake");

        // Act
        LoginResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("token-fake", response.getToken());
        verify(jwtService, times(1)).generateToken("icaro@email.com");
    }

    @Test
    void deveLancarExcecaoQuandoEmailNaoEncontrado() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("naoexiste@email.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        // Act + Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> authService.login(request));

        assertEquals("E-mail ou senha inválidos.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoSenhaErrada() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("icaro@email.com");
        request.setPassword("senha-errada");

        User user = new User();
        user.setEmail("icaro@email.com");
        user.setPassword("senha-criptografada");

        when(userRepository.findByEmail("icaro@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha-errada", "senha-criptografada")).thenReturn(false);

        // Act + Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> authService.login(request));

        assertEquals("E-mail ou senha inválidos.", exception.getMessage());
    }
}