package com.icaro.payflow;

import com.icaro.payflow.dto.CreateUserRequest;
import com.icaro.payflow.dto.UserResponse;
import com.icaro.payflow.entity.User;
import com.icaro.payflow.exception.BusinessException;
import com.icaro.payflow.repository.UserRepository;
import com.icaro.payflow.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void deveCriarUsuarioComSucesso() {
        // Arrange — prepara os dados e define o comportamento dos mocks
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Icaro");
        request.setEmail("icaro@email.com");
        request.setPassword("123456");

        User userSalvo = new User();
        userSalvo.setId("uuid-123");
        userSalvo.setName("Icaro");
        userSalvo.setEmail("icaro@email.com");
        userSalvo.setBalance(BigDecimal.ZERO);

        when(userRepository.findByEmail("icaro@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("senha-criptografada");
        when(userRepository.save(any(User.class))).thenReturn(userSalvo);

        // Act — executa o método que estamos testando
        UserResponse response = userService.createUser(request);

        // Assert — verifica se o resultado é o esperado
        assertNotNull(response);
        assertEquals("Icaro", response.getName());
        assertEquals("icaro@email.com", response.getEmail());
        assertEquals(BigDecimal.ZERO, response.getBalance());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaCadastrado() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("icaro@email.com");
        request.setPassword("123456");

        User usuarioExistente = new User();
        usuarioExistente.setEmail("icaro@email.com");

        when(userRepository.findByEmail("icaro@email.com"))
                .thenReturn(Optional.of(usuarioExistente));

        // Act + Assert — espera que uma exceção seja lançada
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.createUser(request));

        assertEquals("E-mail já cadastrado.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}