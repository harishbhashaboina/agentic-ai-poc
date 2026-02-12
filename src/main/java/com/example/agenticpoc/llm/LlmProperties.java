package com.example.agenticpoc.llm;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "agentic.llm" )
public class LlmProperties
{
    private boolean enabled = false;
    private String baseUrl = "http://localhost:1234/v1";
    private String model = "local-model";
    private String apiKey = "lm-studio";
    private double temperature = 0.2;
    private int maxTokens = 200;
    private int timeoutMs = 5000;

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled( boolean enabled )
    {
        this.enabled = enabled;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl( String baseUrl )
    {
        this.baseUrl = baseUrl;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel( String model )
    {
        this.model = model;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public void setApiKey( String apiKey )
    {
        this.apiKey = apiKey;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public void setTemperature( double temperature )
    {
        this.temperature = temperature;
    }

    public int getMaxTokens()
    {
        return maxTokens;
    }

    public void setMaxTokens( int maxTokens )
    {
        this.maxTokens = maxTokens;
    }

    public int getTimeoutMs()
    {
        return timeoutMs;
    }

    public void setTimeoutMs( int timeoutMs )
    {
        this.timeoutMs = timeoutMs;
    }
}
