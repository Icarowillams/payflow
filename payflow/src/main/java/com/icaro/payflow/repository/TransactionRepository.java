package com.icaro.payflow.repository;

import com.icaro.payflow.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findBySenderIdOrReceiverIdOrderByCreatedAtDesc(String senderId, String receiverId);
    Page<Transaction> findBySenderIdOrReceiverId(String senderId, String receiverId, Pageable pageable);
}