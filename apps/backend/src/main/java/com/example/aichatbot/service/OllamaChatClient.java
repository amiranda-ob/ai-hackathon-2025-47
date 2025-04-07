package com.example.aichatbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OllamaChatClient {
    private static final Logger logger = LoggerFactory.getLogger(OllamaChatClient.class);
    private final ChatClient chatClient;
    private final List<Message> conversationHistory;
    private final String model;

    public OllamaChatClient(ChatClient chatClient, @Value("${spring.ai.ollama.model:llama2}") String model) {
        this.chatClient = chatClient;
        this.model = model;
        this.conversationHistory = new ArrayList<>();
    }

    public String generateResponse(String userMessage) {
        logger.info("Generando respuesta para el mensaje: {} usando el modelo: {}", userMessage, model);

        if (userMessage == null || userMessage.trim().isEmpty()) {
            logger.warn("Se recibió un mensaje vacío");
            return "Lo siento, no puedo procesar un mensaje vacío.";
        }

        try {
            // Agregar el mensaje del usuario al historial
            conversationHistory.add(new UserMessage(userMessage));

            // Crear el prompt con el historial de la conversación
            Prompt prompt = new Prompt(conversationHistory);

            // Obtener la respuesta del modelo
            ChatResponse response = chatClient.call(prompt);
            String assistantResponse = response.getResult().getOutput().getContent();

            // Agregar la respuesta del asistente al historial
            conversationHistory.add(new AssistantMessage(assistantResponse));

            logger.info("Respuesta generada exitosamente usando el modelo: {}", model);
            return assistantResponse;

        } catch (Exception e) {
            logger.error("Error al generar la respuesta", e);
            return "Lo siento, ocurrió un error al procesar tu mensaje. Por favor, intenta nuevamente.";
        }
    }

    public void clearHistory() {
        conversationHistory.clear();
        logger.info("Historial de conversación limpiado");
    }
}
