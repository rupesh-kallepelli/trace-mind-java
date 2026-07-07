CREATE TABLE investigation_evidence (
    id UUID PRIMARY KEY,
    investigation_id UUID,
    agent_type VARCHAR(50),
    summary TEXT,
    evidence JSONB,
    created_at TIMESTAMP
);