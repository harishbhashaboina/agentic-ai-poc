package com.example.agenticpoc.model;

public record AgentInteraction(
    String fromAgent,
    String toAgent,
    String reason,
    String resultSummary
)
{
}
