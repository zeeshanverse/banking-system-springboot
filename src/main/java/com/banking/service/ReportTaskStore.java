package com.banking.service;

import com.banking.service.impl.TransactionReportServiceImpl.ReportTask;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ReportTaskStore {

    private final ConcurrentMap<String, ReportTask> tasks = new ConcurrentHashMap<>();

    public ConcurrentMap<String, ReportTask> tasks() {
        return tasks;
    }
}
