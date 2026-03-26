package com.icaro.payflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private String id;
    private String senderName;
    private String receiverName;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}