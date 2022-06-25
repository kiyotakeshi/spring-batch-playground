package com.example.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String EXISTS_SQL = "select exists(select * from employee where id = ?)";

    public boolean exists(Integer id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXISTS_SQL, Boolean.class, id));
    }
}
