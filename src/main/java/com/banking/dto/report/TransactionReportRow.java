package com.banking.dto.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionReportRow(
        Long transactionId,
        String transactionType,
        BigDecimal amount,
        LocalDateTime transactionTime,
        String status,
        String senderAccountNumber,
        String receiverAccountNumber
) {}
