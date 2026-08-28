package com.banking.dto.report;

public record ReportTaskResponse(
        String taskId,
        String status,
        Integer recordCount,
        String message
) {}
