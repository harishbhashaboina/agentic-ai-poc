package com.example.agenticpoc.agent;

import com.example.agenticpoc.model.AgentInteraction;
import com.example.agenticpoc.model.AgentRequest;
import com.example.agenticpoc.model.AgentResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MasterAgent
{
    private final Map<String, AgentHandler> agentsByType;

    public MasterAgent( List<AgentHandler> agents )
    {
        this.agentsByType = agents.stream()
            .collect( Collectors.toMap(
                agent -> normalize( agent.getAgentType() ),
                agent -> agent,
                ( first, second ) -> first,
                LinkedHashMap::new ) );
    }

    public AgentResponse handle( AgentRequest request )
    {
        String target = resolveTarget( request );
        AgentHandler agent = agentsByType.get( target );

        if ( agent == null )
        {
            Map<String, String> data = new LinkedHashMap<>();
            data.put( "requestedTarget", String.valueOf( request == null ? null : request.targetAgent() ) );
            data.put( "availableAgents", String.join( ",", agentsByType.keySet() ) );
            return new AgentResponse(
                "MasterAgent",
                "Unknown target agent",
                data,
                List.of() );
        }

        AgentResponse response = agent.handle( request );
        Map<String, String> data = response.data() == null
            ? new LinkedHashMap<>()
            : new LinkedHashMap<>( response.data() );
        data.putIfAbsent( "routedBy", "MasterAgent" );

        List<AgentInteraction> interactions =
            response.interactions() == null ? List.of() : response.interactions();

        return new AgentResponse(
            response.handledBy(),
            response.summary(),
            data,
            interactions );
    }

    private String resolveTarget( AgentRequest request )
    {
        if ( request == null )
        {
            return "";
        }

        if ( StringUtils.hasText( request.targetAgent() ) )
        {
            return normalize( request.targetAgent() );
        }

        if ( StringUtils.hasText( request.prompt() ) )
        {
            String normalizedPrompt = request.prompt().toLowerCase( Locale.ROOT );
            if ( normalizedPrompt.contains( "cancel" ) )
            {
                return "cancel";
            }
            if ( normalizedPrompt.contains( "payment" ) )
            {
                return "payment";
            }
        }

        if ( StringUtils.hasText( request.billingAccountId() ) )
        {
            return "payment";
        }

        if ( StringUtils.hasText( request.memberId() ) )
        {
            return "cancel";
        }

        return "";
    }

    private String normalize( String value )
    {
        if ( value == null )
        {
            return "";
        }

        return value.trim().toLowerCase( Locale.ROOT );
    }
}
