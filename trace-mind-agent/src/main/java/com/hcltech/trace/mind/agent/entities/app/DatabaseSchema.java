package com.hcltech.trace.mind.agent.entities.app;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "database_schemas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DatabaseInfo database;

    private String schemaName;

    @Column(columnDefinition = "TEXT")
    private String description;
}