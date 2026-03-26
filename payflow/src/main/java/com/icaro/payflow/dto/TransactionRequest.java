package com.icaro.payflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotBlank(message = "O destinatário é obrigatório.")
    private String receiverId;

    @Positive(message = "O valor deve ser maior que zero.")
    private BigDecimal amount;
}