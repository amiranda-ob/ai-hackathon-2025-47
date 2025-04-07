package com.example.aichatbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OllamaChatClientTest {

    @Mock
    private ChatClient chatClient;

    private OllamaChatClient ollamaChatClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ollamaChatClient = new OllamaChatClient(chatClient, "llama2");
    }

    @Test
    void testGenerateResponse() {
        String userMessage = "Hola";
        String expectedResponse = "¡Hola! ¿En qué puedo ayudarte?";

        Generation generation = new Generation(expectedResponse);
        ChatResponse mockResponse = new ChatResponse(List.of(generation));

        when(chatClient.call(any(Prompt.class))).thenReturn(mockResponse);

        String response = ollamaChatClient.generateResponse(userMessage);
        assertEquals(expectedResponse, response);
    }

    @Test
    void testEmptyMessage() {
        String response = ollamaChatClient.generateResponse("");
        assertEquals("Lo siento, no puedo procesar un mensaje vacío.", response);
    }

    @Test
    void testNullMessage() {
        String response = ollamaChatClient.generateResponse(null);
        assertEquals("Lo siento, no puedo procesar un mensaje vacío.", response);
    }
}
