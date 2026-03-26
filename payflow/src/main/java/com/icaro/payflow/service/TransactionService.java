package com.icaro.payflow.service;

import com.icaro.payflow.dto.TransactionRequest;
import com.icaro.payflow.dto.TransactionResponse;
import com.icaro.payflow.entity.Transaction;
import com.icaro.payflow.entity.User;
import com.icaro.payflow.exception.BusinessException;
import com.icaro.payflow.repository.TransactionRepository;
import com.icaro.payflow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
                               UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TransactionResponse transfer(String senderEmail, TransactionRequest request) {
        log.info("Iniciando transferência de {} para receiverId={} no valor de {}",
                senderEmail, request.getReceiverId(), request.getAmount());

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new BusinessException("Remetente não encontrado."));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new BusinessException("Destinatário não encontrado."));

        if (sender.getId().equals(receiver.getId())) {
            log.warn("Tentativa de transferência para si mesmo: {}", senderEmail);
            throw new BusinessException("Não é possível transferir para si mesmo.");
        }

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            log.warn("Saldo insuficiente para {}: saldo={}, valor={}",
                    senderEmail, sender.getBalance(), request.getAmount());
            throw new BusinessException("Saldo insuficiente.");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(request.getAmount());

        Transaction saved = transactionRepository.save(transaction);

        log.info("Transferência concluída: id={}, {} -> {}, valor={}",
                saved.getId(), sender.getName(), receiver.getName(), saved.getAmount());

        return new TransactionResponse(
                saved.getId(),
                sender.getName(),
                receiver.getName(),
                saved.getAmount(),
                saved.getCreatedAt()
        );
    }

    public List<TransactionResponse> getHistory(String email) {
        log.debug("Buscando histórico de transações para {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        return transactionRepository
                .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(user.getId(), user.getId())
                .stream()
                .map(t -> new TransactionResponse(
                        t.getId(),
                        t.getSender().getName(),
                        t.getReceiver().getName(),
                        t.getAmount(),
                        t.getCreatedAt()
                ))
                .toList();
    }

    public Page<TransactionResponse> getHistoryPaginated(String email, Pageable pageable) {
        log.debug("Buscando histórico paginado para {} - page={}", email, pageable.getPageNumber());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        return transactionRepository
                .findBySenderIdOrReceiverId(user.getId(), user.getId(), pageable)
                .map(t -> new TransactionResponse(
                        t.getId(),
                        t.getSender().getName(),
                        t.getReceiver().getName(),
                        t.getAmount(),
                        t.getCreatedAt()
                ));
    }
}