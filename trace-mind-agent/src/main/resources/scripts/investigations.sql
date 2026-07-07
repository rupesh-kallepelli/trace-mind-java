CREATE TABLE investigations (
    id UUID PRIMARY KEY,
    issue_description TEXT,
    service_name VARCHAR(255),
    namespace VARCHAR(255),
    status VARCHAR(50),
    root_cause TEXT,
    confidence_score DOUBLE PRECISION,
    report_markdown TEXT,
    started_at TIMESTAMP,
    completed_at TIMESTAMP
);