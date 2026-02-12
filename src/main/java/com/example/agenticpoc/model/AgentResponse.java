package com.example.agenticpoc.model;

import java.util.List;
import java.util.Map;

public record AgentResponse(
    String handledBy,
    String summary,
    Map<String, String> data,
    List<AgentInteraction> interactions
)
{
}
