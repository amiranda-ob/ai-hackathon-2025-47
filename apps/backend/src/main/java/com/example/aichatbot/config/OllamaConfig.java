package com.example.aichatbot.config;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
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
    public OllamaApi ollamaApi() {
        return new OllamaApi(baseUrl);
    }

    @Bean
    public ChatClient chatClient(OllamaApi ollamaApi) {
        OllamaChatClient client = new OllamaChatClient(ollamaApi);
        client.withDefaultOptions(OllamaOptions.create().withModel(model));
        return client;
    }

    @Bean
    public EmbeddingClient embeddingClient(OllamaApi ollamaApi) {
        return new OllamaEmbeddingClient(ollamaApi);
    }
}
