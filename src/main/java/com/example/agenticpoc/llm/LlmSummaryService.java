package com.example.agenticpoc.llm;

import com.example.agenticpoc.model.AgentRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Service
public class LlmSummaryService
{
    private final LlmStudioClient llmStudioClient;
    private final LlmProperties properties;

    public LlmSummaryService( LlmStudioClient llmStudioClient, LlmProperties properties )
    {
        this.llmStudioClient = llmStudioClient;
        this.properties = properties;
    }

    public String buildCancelSummary( AgentRequest request, Map<String, String> data, boolean pendingPayments, String fallback )
    {
        if ( !properties.isEnabled() )
        {
            return fallback;
        }

        String systemPrompt = "You summarize membership cancellation decisions for support agents.";
        StringBuilder userPrompt = new StringBuilder();
        userPrompt.append( "User prompt: " ).append( safe( request == null ? null : request.prompt() ) ).append( "\n" );
        userPrompt.append( "Member ID: " ).append( safe( data.get( "memberId" ) ) ).append( "\n" );
        userPrompt.append( "Billing Account ID: " ).append( safe( data.get( "billingAccountId" ) ) ).append( "\n" );
        userPrompt.append( "Pending payments: " ).append( pendingPayments ).append( "\n" );
        userPrompt.append( "Pending amount: " ).append( safe( data.get( "pendingAmount" ) ) ).append( "\n" );
        userPrompt.append( "Provide a one-sentence response explaining the outcome." );

        Optional<String> generated = llmStudioClient.generateChatCompletion( systemPrompt, userPrompt.toString() );
        return generated.filter( StringUtils::hasText ).orElse( fallback );
    }

    private String safe( String value )
    {
        return StringUtils.hasText( value ) ? value : "n/a";
    }
}
