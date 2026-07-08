    create table investigation_events (
        id uuid not null,
        created_at timestamp(6) with time zone,
        event_type varchar(255) check (event_type in ('INVESTIGATION_CREATED','INVESTIGATION_STARTED','INVESTIGATION_COMPLETED','INVESTIGATION_FAILED','AGENT_STARTED','AGENT_COMPLETED','AGENT_FAILED','MCP_CALL_STARTED','MCP_CALL_COMPLETED','MCP_CALL_FAILED','EVIDENCE_FOUND','RCA_GENERATED','RECOMMENDATION_GENERATED')),
        investigation_id uuid,
        message varchar(5000),
        payload oid,
        source varchar(255),
        primary key (id)
    )