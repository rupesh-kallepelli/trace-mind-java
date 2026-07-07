CREATE TABLE incidents (
    id UUID PRIMARY KEY,
    investigation_id UUID,
    incident_number VARCHAR(50),
    severity VARCHAR(50),
    status VARCHAR(50),
    assigned_to VARCHAR(255),
    created_at TIMESTAMP,
    resolved_at TIMESTAMP
);