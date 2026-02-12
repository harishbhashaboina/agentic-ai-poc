package com.example.agenticpoc.agent;

import com.example.agenticpoc.model.AgentInteraction;
import com.example.agenticpoc.model.AgentRequest;
import com.example.agenticpoc.model.AgentResponse;
import com.example.agenticpoc.model.PaymentSummary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CancelAgent implements AgentHandler
{
    private final PaymentAgent paymentAgent;

    public CancelAgent( PaymentAgent paymentAgent )
    {
        this.paymentAgent = paymentAgent;
    }

    @Override
    public String getAgentType()
    {
        return "cancel";
    }

    @Override
    public AgentResponse handle( AgentRequest request )
    {
        Map<String, String> data = new LinkedHashMap<>();
        List<AgentInteraction> interactions = new ArrayList<>();

        if ( request != null )
        {
            if ( StringUtils.hasText( request.memberId() ) )
            {
                data.put( "memberId", request.memberId() );
            }
            if ( StringUtils.hasText( request.billingAccountId() ) )
            {
                data.put( "billingAccountId", request.billingAccountId() );
            }
        }

        PaymentSummary paymentSummary = paymentAgent.checkPendingPayments( request );
        interactions.add( new AgentInteraction(
            "CancelAgent",
            "PaymentAgent",
            "Check pending payments before cancellation",
            paymentSummary.reason() ) );

        data.put( "pendingPayments", Boolean.toString( paymentSummary.hasPendingPayments() ) );
        data.put( "pendingAmount", paymentSummary.pendingAmount().toPlainString() );

        String summary = paymentSummary.hasPendingPayments()
            ? "Cancellation blocked due to pending payments"
            : "Cancellation is eligible to proceed";

        if ( request != null && StringUtils.hasText( request.prompt() ) )
        {
            summary = summary + " for prompt: " + request.prompt();
        }

        return new AgentResponse( "CancelAgent", summary, data, interactions );
    }
}
