package com.banking.service;

public interface TransactionReportWorker {
    void generateReport(String taskId, String email);
}
