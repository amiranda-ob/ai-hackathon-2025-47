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
        // Prepare request
        ChatRequest request = new ChatRequest("Hello, how are you?", "gpt-3.5-turbo");

        // Mock service response
        when(chatClient.generate(anyString())).thenReturn(new ChatResponse(
                List.of(new Generation("I'm doing well, thank you for asking!"))));

        // Perform request
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("I'm doing well, thank you for asking!"))
                .andExpect(jsonPath("$.model").value("gpt-3.5-turbo"))
                .andExpect(jsonPath("$.timestamp").exists());
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
