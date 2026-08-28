package com.banking.repository.jdbc;

import com.banking.dto.report.TransactionReportRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TransactionReportRow> findUserTransactions(String email) {
        String sql = """
                SELECT t.id,
                       t.transaction_type,
                       t.amount,
                       t.transaction_time,
                       t.status,
                       sender.account_number AS sender_account_number,
                       receiver.account_number AS receiver_account_number
                FROM transactions t
                LEFT JOIN accounts sender ON sender.id = t.sender_account_id
                LEFT JOIN accounts receiver ON receiver.id = t.receiver_account_id
                LEFT JOIN users sender_user ON sender_user.id = sender.user_id
                LEFT JOIN users receiver_user ON receiver_user.id = receiver.user_id
                WHERE sender_user.email = ? OR receiver_user.email = ?
                ORDER BY t.transaction_time DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new TransactionReportRow(
                rs.getLong("id"),
                rs.getString("transaction_type"),
                rs.getBigDecimal("amount"),
                rs.getTimestamp("transaction_time").toLocalDateTime(),
                rs.getString("status"),
                rs.getString("sender_account_number"),
                rs.getString("receiver_account_number")
        ), email, email);
    }
}
