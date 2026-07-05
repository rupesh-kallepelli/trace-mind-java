package com.hcltech.database.query.mcp.server.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
public class DatabaseConfiguration {

  @Bean
  public Map<String, DataSource> dataSources(
      DatabaseProperties properties) {

    Map<String, DataSource> dataSources = new HashMap<>();

    properties.getDatabases()
        .forEach((name, config) -> {

          HikariDataSource ds = new HikariDataSource();

          ds.setDriverClassName(
              config.getDriverClassName());

          ds.setJdbcUrl(
              config.getJdbcUrl());

          ds.setUsername(
              config.getUsername());

          ds.setPassword(
              config.getPassword());

          dataSources.put(name, ds);
        });

    return dataSources;
  }

  @Bean
  public Map<String, JdbcTemplate> jdbcTemplates(
      Map<String, DataSource> dataSources) {

    Map<String, JdbcTemplate> jdbcTemplates = new HashMap<>();

    dataSources.forEach((name, ds) -> jdbcTemplates.put(
        name,
        new JdbcTemplate(ds)));

    return jdbcTemplates;
  }
}