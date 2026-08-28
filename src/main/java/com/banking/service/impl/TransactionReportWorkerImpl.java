package com.banking.service.impl;

import com.banking.dto.report.TransactionReportRow;
import com.banking.repository.jdbc.TransactionJdbcRepository;
import com.banking.service.TransactionReportWorker;
import com.banking.service.ReportTaskStore;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionReportWorkerImpl implements TransactionReportWorker {

    private final TransactionJdbcRepository jdbcRepository;
    private final ReportTaskStore taskStore;

    public TransactionReportWorkerImpl(
            TransactionJdbcRepository jdbcRepository,
            ReportTaskStore taskStore) {
        this.jdbcRepository = jdbcRepository;
        this.taskStore = taskStore;
    }

    @Override
    @Async("reportTaskExecutor")
    public void generateReport(String taskId, String email) {
        taskStore.tasks().computeIfPresent(taskId, (id, task) -> task.running());

        try {
            List<TransactionReportRow> result = jdbcRepository.findUserTransactions(email);
            taskStore.tasks().computeIfPresent(taskId, (id, task) -> task.completed(result));
        } catch (RuntimeException ex) {
            taskStore.tasks().computeIfPresent(taskId, (id, task) -> task.failed(ex.getMessage()));
        }
    }
}
