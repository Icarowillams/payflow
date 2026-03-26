package com.icaro.payflow;

import com.icaro.payflow.dto.TransactionRequest;
import com.icaro.payflow.dto.TransactionResponse;
import com.icaro.payflow.entity.Transaction;
import com.icaro.payflow.entity.User;
import com.icaro.payflow.exception.BusinessException;
import com.icaro.payflow.repository.TransactionRepository;
import com.icaro.payflow.repository.UserRepository;
import com.icaro.payflow.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId("sender-uuid");
        sender.setName("Ícaro");
        sender.setEmail("icaro@email.com");
        sender.setBalance(new BigDecimal("1000.00"));

        receiver = new User();
        receiver.setId("receiver-uuid");
        receiver.setName("João");
        receiver.setEmail("joao@email.com");
        receiver.setBalance(new BigDecimal("500.00"));
    }

    @Test
    void deveRealizarTransferenciaComSucesso() {
        // Arrange
        TransactionRequest request = new TransactionRequest();
        request.setReceiverId("receiver-uuid");
        request.setAmount(new BigDecimal("200.00"));

        Transaction saved = new Transaction();
        saved.setId("tx-uuid");
        saved.setSender(sender);
        saved.setReceiver(receiver);
        saved.setAmount(new BigDecimal("200.00"));
        saved.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByEmail("icaro@email.com")).thenReturn(Optional.of(sender));
        when(userRepository.findById("receiver-uuid")).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);

        // Act
        TransactionResponse response = transactionService.transfer("icaro@email.com", request);

        // Assert
        assertNotNull(response);
        assertEquals("Ícaro", response.getSenderName());
        assertEquals("João", response.getReceiverName());
        assertEquals(new BigDecimal("200.00"), response.getAmount());
        assertEquals(new BigDecimal("800.00"), sender.getBalance());
        assertEquals(new BigDecimal("700.00"), receiver.getBalance());
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        // Arrange
        sender.setBalance(new BigDecimal("50.00"));

        TransactionRequest request = new TransactionRequest();
        request.setReceiverId("receiver-uuid");
        request.setAmount(new BigDecimal("200.00"));

        when(userRepository.findByEmail("icaro@email.com")).thenReturn(Optional.of(sender));
        when(userRepository.findById("receiver-uuid")).thenReturn(Optional.of(receiver));

        // Act + Assert
        BusinessException ex = assertThrows(BusinessException.class,
                () -> transactionService.transfer("icaro@email.com", request));

        assertEquals("Saldo insuficiente.", ex.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoTransferirParaSiMesmo() {
        // Arrange
        TransactionRequest request = new TransactionRequest();
        request.setReceiverId("sender-uuid");
        request.setAmount(new BigDecimal("100.00"));

        when(userRepository.findByEmail("icaro@email.com")).thenReturn(Optional.of(sender));
        when(userRepository.findById("sender-uuid")).thenReturn(Optional.of(sender));

        // Act + Assert
        BusinessException ex = assertThrows(BusinessException.class,
                () -> transactionService.transfer("icaro@email.com", request));

        assertEquals("Não é possível transferir para si mesmo.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoDestinatarioNaoEncontrado() {
        // Arrange
        TransactionRequest request = new TransactionRequest();
        request.setReceiverId("inexistente-uuid");
        request.setAmount(new BigDecimal("100.00"));

        when(userRepository.findByEmail("icaro@email.com")).thenReturn(Optional.of(sender));
        when(userRepository.findById("inexistente-uuid")).thenReturn(Optional.empty());

        // Act + Assert
        BusinessException ex = assertThrows(BusinessException.class,
                () -> transactionService.transfer("icaro@email.com", request));

        assertEquals("Destinatário não encontrado.", ex.getMessage());
    }

    @Test
    void deveRetornarHistoricoDeTransacoes() {
        // Arrange
        Transaction tx = new Transaction();
        tx.setId("tx-uuid");
        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setAmount(new BigDecimal("150.00"));
        tx.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByEmail("icaro@email.com")).thenReturn(Optional.of(sender));
        when(transactionRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc("sender-uuid", "sender-uuid"))
                .thenReturn(List.of(tx));

        // Act
        List<TransactionResponse> history = transactionService.getHistory("icaro@email.com");

        // Assert
        assertEquals(1, history.size());
        assertEquals("tx-uuid", history.get(0).getId());
        assertEquals(new BigDecimal("150.00"), history.get(0).getAmount());
    }
}