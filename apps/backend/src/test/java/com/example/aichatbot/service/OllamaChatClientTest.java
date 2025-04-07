package com.example.aichatbot.service;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class OllamaChatClientTest {

    @Autowired
    private OllamaChatClient chatClient;

    @Test
    public void testGenerateWithValidPrompt() {
        // This test will only pass if OPENAI_API_KEY is set
        ChatResponse response = chatClient.generate("Hello, how are you?");
        assertNotNull(response);
    }

    @Test
    public void testGenerateWithEmptyPrompt() {
        assertThrows(IllegalArgumentException.class, () -> {
            chatClient.generate("");
        });
    }

    @Test
    public void testGenerateWithNullPrompt() {
        assertThrows(IllegalArgumentException.class, () -> {
            chatClient.generate(null);
        });
    }
}
