package com.icaro.payflow.controller;

import com.icaro.payflow.dto.TransactionRequest;
import com.icaro.payflow.dto.TransactionResponse;
import com.icaro.payflow.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse transfer(@AuthenticationPrincipal String email,
                                        @Valid @RequestBody TransactionRequest request) {
        return transactionService.transfer(email, request);
    }

    @GetMapping("/history")
    public List<TransactionResponse> getHistory(@AuthenticationPrincipal String email) {
        return transactionService.getHistory(email);
    }

    @GetMapping("/history/paginated")
    public Page<TransactionResponse> getHistoryPaginated(
            @AuthenticationPrincipal String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return transactionService.getHistoryPaginated(email,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }
}