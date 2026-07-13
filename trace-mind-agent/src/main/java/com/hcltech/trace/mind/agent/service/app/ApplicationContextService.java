package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.response.app.ApplicationContextResponse;

public interface ApplicationContextService {

    ApplicationContextResponse buildContext(
            Long applicationId);
}