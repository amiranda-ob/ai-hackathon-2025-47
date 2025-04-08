package com.example.aichatbot.config;

import com.example.aichatbot.model.AIType;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AIConfig {

    @Value("${spring.ai.ollama.base-url}")
    private String ollamaBaseUrl;

    @Value("${spring.ai.ollama.model:llama2}")
    private String ollamaModel;

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    @Value("${spring.ai.openai.model:gpt-3.5-turbo}")
    private String openAiModel;

    @Bean
    public OllamaApi ollamaApi() {
        return new OllamaApi(ollamaBaseUrl);
    }

    @Bean
    public OpenAiApi openAiApi() {
        return new OpenAiApi(openAiApiKey);
    }

    @Bean
    public ChatClient ollamaClient(OllamaApi ollamaApi) {
        OllamaChatClient ollamaClient = new OllamaChatClient(ollamaApi);
        ollamaClient.withDefaultOptions(OllamaOptions.create().withModel(ollamaModel));
        return ollamaClient;
    }

    @Bean
    public ChatClient openAiClient(OpenAiApi openAiApi) {
        return new OpenAiChatClient(openAiApi, OpenAiChatOptions.builder().withModel(openAiModel).build());
    }

    @Bean
    @Primary
    public Map<AIType, ChatClient> chatClients(ChatClient ollamaClient, ChatClient openAiClient) {
        Map<AIType, ChatClient> clients = new HashMap<>();
        clients.put(AIType.OLLAMA, ollamaClient);
        clients.put(AIType.OPENAI, openAiClient);
        return clients;
    }
}
