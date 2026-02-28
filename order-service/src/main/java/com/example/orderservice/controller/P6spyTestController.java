package com.example.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class P6spyTestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/p6spy")
    public String testP6spy() {
        // 执行简单查询测试
        jdbcTemplate.queryForList("SELECT 1 as test");
        jdbcTemplate.update("INSERT INTO test_table (name) VALUES (?)", "test-value");
        return "p6spy test executed, check console for SQL output";
    }
}
