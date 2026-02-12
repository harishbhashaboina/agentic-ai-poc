package com.example.agenticpoc.model;

public record AgentRequest(
    String targetAgent,
    String prompt,
    String memberId,
    String billingAccountId
)
{
}
