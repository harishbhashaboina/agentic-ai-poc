package com.example.agenticpoc.controller;

import com.example.agenticpoc.agent.PaymentAgent;
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
public class PaymentAgentController
{
    private final PaymentAgent paymentAgent;

    public PaymentAgentController( PaymentAgent paymentAgent )
    {
        this.paymentAgent = paymentAgent;
    }

    @PostMapping( "/payment" )
    public ResponseEntity<AgentResponse> handlePayment( @RequestBody AgentRequest request )
    {
        return new ResponseEntity<>( paymentAgent.handle( request ), HttpStatus.OK );
    }
}
