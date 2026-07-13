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
@Table(name = "table_columns")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DatabaseTable table;

    private String columnName;

    private String dataType;

    private boolean primaryKey;

    private boolean nullable;

    @Column(columnDefinition = "TEXT")
    private String businessMeaning;
}