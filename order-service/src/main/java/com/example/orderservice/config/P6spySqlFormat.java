package com.example.orderservice.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;

public class P6spySqlFormat implements MessageFormattingStrategy {

    private static final Formatter FORMATTER = new BasicFormatterImpl();

    @Override
    public String formatMessage(int connectionId, String now, long elapsed,
                                String category, String prepared, String sql, String url) {

        if (sql.trim().isEmpty()) {
            return "";
        }

        // 格式化 SQL
        String formattedSql = FORMATTER.format(sql);

        StringBuilder message = new StringBuilder();
        message.append("\n").append("=== P6Spy SQL Log ===").append("\n");
        message.append("Time: ").append(now).append("\n");
        message.append("Elapsed: ").append(elapsed).append("ms").append("\n");
        message.append("Category: ").append(category).append("\n");

        // 如果有 prepared SQL，显示实际参数
        if (prepared != null && !prepared.trim().isEmpty()) {
            message.append("Prepared SQL: ").append(prepared).append("\n");
        }

        // 显示格式化后的 SQL
        message.append("SQL:\n").append(formattedSql).append("\n");
        message.append("=====================").append("\n");

        return message.toString();
    }
}