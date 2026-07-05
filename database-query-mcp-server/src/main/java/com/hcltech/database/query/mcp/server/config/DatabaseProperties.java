package com.hcltech.database.query.mcp.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "app")
public class DatabaseProperties {

  private Map<String, DatabaseConfig> databases = new HashMap<>();

  @Data
  public static class DatabaseConfig {

    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;
  }
}