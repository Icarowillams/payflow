package com.icaro.payflow.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DepositRequest {

    @Positive(message = "O valor deve ser maior que zero.")
    private BigDecimal amount;
}