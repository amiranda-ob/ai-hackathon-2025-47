package com.example.aichatbot.controller;

import com.example.aichatbot.model.ChatRequest;
import com.example.aichatbot.service.OllamaChatClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OllamaChatClient chatClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testChatEndpoint() throws Exception {
        String testMessage = "Hola";
        String expectedResponse = "¡Hola! ¿En qué puedo ayudarte?";

        when(chatClient.generateResponse(anyString())).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/chat")
                .contentType(MediaType.TEXT_PLAIN)
                .content(testMessage))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testChatEndpointWithEmptyMessage() throws Exception {
        // Prepare request with empty message
        ChatRequest request = new ChatRequest("", "gpt-3.5-turbo");

        // Perform request
        mockMvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
