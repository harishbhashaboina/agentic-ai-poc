package com.example.agenticpoc.agent;

import com.example.agenticpoc.model.AgentRequest;
import com.example.agenticpoc.model.AgentResponse;

public interface AgentHandler
{
    String getAgentType();

    AgentResponse handle( AgentRequest request );
}
