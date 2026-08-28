package com.banking.controller;

import com.banking.dto.transaction.TransactionResponse;
import com.banking.service.TransactionService;
import com.banking.service.TransactionReportService;
import com.banking.dto.report.ReportTaskResponse;
import com.banking.dto.report.TransactionReportRow;
import com.banking.security.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionReportService transactionReportService;

    public TransactionController(TransactionService transactionService,
                                 TransactionReportService transactionReportService) {
        this.transactionService = transactionService;
        this.transactionReportService = transactionReportService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransaction(){
        List<TransactionResponse> responses = transactionService.getAllTransactions();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable Long transactionId){
        TransactionResponse response = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionForSpecificAccount(
            @PathVariable Long accountId){
        List<TransactionResponse> responses = transactionService.getTransactionForAcc(accountId);
        return ResponseEntity.ok(responses);
    }
    @PostMapping("/reports")
    public ResponseEntity<ReportTaskResponse> startTransactionReport() {
        String email = SecurityUtils.getCurrentUserEmail();
        String taskId = transactionReportService.startReport(email);
        return ResponseEntity.accepted().body(transactionReportService.getStatus(taskId, SecurityUtils.getCurrentUserEmail()));
    }

    @GetMapping("/reports/{taskId}")
    public ResponseEntity<ReportTaskResponse> getTransactionReportStatus(
            @PathVariable String taskId) {
        return ResponseEntity.ok(transactionReportService.getStatus(taskId, SecurityUtils.getCurrentUserEmail()));
    }

    @GetMapping("/reports/{taskId}/result")
    public ResponseEntity<List<TransactionReportRow>> getTransactionReportResult(
            @PathVariable String taskId) {
        return ResponseEntity.ok(transactionReportService.getResult(taskId, SecurityUtils.getCurrentUserEmail()));
    }

}
