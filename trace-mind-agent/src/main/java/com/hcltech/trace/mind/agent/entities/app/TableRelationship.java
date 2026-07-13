package com.hcltech.trace.mind.agent.entities.app;

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
@Table(name = "table_relationships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DatabaseTable sourceTable;

    @ManyToOne
    private DatabaseTable targetTable;

    private String relationshipType;
}