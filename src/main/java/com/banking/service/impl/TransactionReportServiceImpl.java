package com.banking.service.impl;

import com.banking.dto.report.ReportTaskResponse;
import com.banking.dto.report.TransactionReportRow;
import com.banking.exception.ResourceNotFoundException;
import com.banking.service.TransactionReportService;
import com.banking.service.TransactionReportWorker;
import com.banking.service.ReportTaskStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionReportServiceImpl implements TransactionReportService {

    private final TransactionReportWorker reportWorker;
    private final ReportTaskStore taskStore;

    public TransactionReportServiceImpl(TransactionReportWorker reportWorker, ReportTaskStore taskStore) {
        this.reportWorker = reportWorker;
        this.taskStore = taskStore;
    }

    @Override
    public String startReport(String email) {
        String taskId = UUID.randomUUID().toString();
        taskStore.tasks().put(taskId, ReportTask.pending(email));
        reportWorker.generateReport(taskId, email);
        return taskId;
    }

    @Override
    public ReportTaskResponse getStatus(String taskId, String email) {
        ReportTask task = getOwnedTask(taskId, email);
        return new ReportTaskResponse(
                taskId,
                task.status,
                task.result == null ? 0 : task.result.size(),
                task.message
        );
    }

    @Override
    public List<TransactionReportRow> getResult(String taskId, String email) {
        ReportTask task = getOwnedTask(taskId, email);
        if (!"COMPLETED".equals(task.status)) {
            throw new IllegalStateException("Report is not completed. Current status: " + task.status);
        }
        return task.result;
    }

    private ReportTask getOwnedTask(String taskId, String email) {
        ReportTask task = taskStore.tasks().get(taskId);
        if (task == null || !task.ownerEmail.equals(email)) {
            throw new ResourceNotFoundException("Report task not found: " + taskId);
        }
        return task;
    }

    public record ReportTask(
            String ownerEmail,
            String status,
            List<TransactionReportRow> result,
            String message
    ) {
        static ReportTask pending(String email) {
            return new ReportTask(email, "PENDING", null, "Report queued");
        }

        ReportTask running() {
            return new ReportTask(ownerEmail, "RUNNING", null, "Report is being generated");
        }

        ReportTask completed(List<TransactionReportRow> result) {
            return new ReportTask(ownerEmail, "COMPLETED", List.copyOf(result), "Report generated successfully");
        }

        ReportTask failed(String error) {
            String message = error == null ? "Unknown error" : error;
            return new ReportTask(ownerEmail, "FAILED", null, "Report generation failed: " + message);
        }
    }
}
