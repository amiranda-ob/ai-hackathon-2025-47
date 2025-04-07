package com.example.aichatbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class OllamaChatClient {

    private static final Logger logger = LoggerFactory.getLogger(OllamaChatClient.class);
    private final ChatClient chatClient;

    public OllamaChatClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public ChatResponse generate(String message) {
        logger.info("Generando respuesta para el mensaje: {}", message);

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }

        Prompt prompt = new Prompt(message);
        ChatResponse response = chatClient.call(prompt);

        logger.info("Respuesta generada: {}", response.getResult().getOutput().getContent());
        return response;
    }
}
