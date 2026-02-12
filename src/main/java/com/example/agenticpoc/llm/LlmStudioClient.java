package com.example.agenticpoc.llm;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class LlmStudioClient
{
    private final RestTemplate restTemplate;
    private final LlmProperties properties;

    public LlmStudioClient( RestTemplateBuilder restTemplateBuilder, LlmProperties properties )
    {
        this.properties = properties;
        this.restTemplate = restTemplateBuilder
            .setConnectTimeout( Duration.ofMillis( properties.getTimeoutMs() ) )
            .setReadTimeout( Duration.ofMillis( properties.getTimeoutMs() ) )
            .build();
    }

    public Optional<String> generateChatCompletion( String systemPrompt, String userPrompt )
    {
        if ( !properties.isEnabled() )
        {
            return Optional.empty();
        }

        String url = properties.getBaseUrl() + "/chat/completions";
        ChatCompletionRequest request = new ChatCompletionRequest(
            properties.getModel(),
            List.of(
                new Message( "system", systemPrompt ),
                new Message( "user", userPrompt ) ),
            properties.getTemperature(),
            properties.getMaxTokens(),
            false );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        if ( StringUtils.hasText( properties.getApiKey() ) )
        {
            headers.setBearerAuth( properties.getApiKey() );
        }

        try
        {
            ResponseEntity<ChatCompletionResponse> response = restTemplate
                .postForEntity( url, new HttpEntity<>( request, headers ), ChatCompletionResponse.class );
            if ( response.getBody() == null || response.getBody().choices() == null || response.getBody().choices().isEmpty() )
            {
                return Optional.empty();
            }

            Message message = response.getBody().choices().get( 0 ).message();
            if ( message == null || !StringUtils.hasText( message.content() ) )
            {
                return Optional.empty();
            }

            return Optional.of( message.content().trim() );
        }
        catch ( RestClientException ex )
        {
            return Optional.empty();
        }
    }

    public record ChatCompletionRequest(
        String model,
        List<Message> messages,
        double temperature,
        @JsonProperty( "max_tokens" ) int maxTokens,
        boolean stream
    )
    {
    }

    public record ChatCompletionResponse(
        List<Choice> choices
    )
    {
        public record Choice(
            Message message
        )
        {
        }
    }

    public record Message(
        String role,
        String content
    )
    {
    }
}
