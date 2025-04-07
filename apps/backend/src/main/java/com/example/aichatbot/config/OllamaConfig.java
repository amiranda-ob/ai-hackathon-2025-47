package com.example.aichatbot.config;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {

    @Value("${spring.ai.ollama.base-url}")
    private String baseUrl;

    @Value("${spring.ai.ollama.model:llama2}")
    private String model;

    @Bean
    public ChatClient chatClient() {
        OllamaApi ollamaApi = new OllamaApi(baseUrl);
        return new OllamaChatClient(ollamaApi);
    }
}
