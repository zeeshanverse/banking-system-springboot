package com.banking.service;

import com.banking.dto.report.ReportTaskResponse;
import com.banking.dto.report.TransactionReportRow;

import java.util.List;

public interface TransactionReportService {
    String startReport(String email);

    ReportTaskResponse getStatus(String taskId, String email);

    List<TransactionReportRow> getResult(String taskId, String email);
}
