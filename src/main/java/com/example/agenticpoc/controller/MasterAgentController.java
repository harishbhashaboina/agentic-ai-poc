package com.example.agenticpoc.controller;

import com.example.agenticpoc.agent.MasterAgent;
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
public class MasterAgentController
{
    private final MasterAgent masterAgent;

    public MasterAgentController( MasterAgent masterAgent )
    {
        this.masterAgent = masterAgent;
    }

    @PostMapping( "/master" )
    public ResponseEntity<AgentResponse> route( @RequestBody AgentRequest request )
    {
        return new ResponseEntity<>( masterAgent.handle( request ), HttpStatus.OK );
    }
}
