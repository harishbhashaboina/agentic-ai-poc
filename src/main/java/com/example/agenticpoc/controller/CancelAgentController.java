package com.example.agenticpoc.controller;

import com.example.agenticpoc.agent.CancelAgent;
import com.example.agenticpoc.model.AgentRequest;
import com.example.agenticpoc.model.AgentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/agentic" )
public class CancelAgentController
{
    private final CancelAgent cancelAgent;

    public CancelAgentController( CancelAgent cancelAgent )
    {
        this.cancelAgent = cancelAgent;
    }

    @PostMapping( "/cancel" )
    public ResponseEntity<AgentResponse> handleCancel( @RequestBody AgentRequest request )
    {
        return new ResponseEntity<>( cancelAgent.handle( request ), HttpStatus.OK );
    }
}
