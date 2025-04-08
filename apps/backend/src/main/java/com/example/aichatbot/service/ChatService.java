package com.example.aichatbot.service;

import com.example.aichatbot.model.AIType;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final Map<AIType, ChatClient> chatClients;
    private final VectorDatasetService vectorSearchService;
    private static final int MAX_RELEVANT_DOCS = 10;

    @Autowired
    public ChatService(Map<AIType, ChatClient> chatClients, VectorDatasetService vectorSearchService) {
        this.chatClients = chatClients;
        this.vectorSearchService = vectorSearchService;
    }

    public String chat(String userInput, AIType aiType) {
        // 1. Buscar documentos relevantes en Qdrant
        List<Document> relevantDocs = vectorSearchService.search(userInput, MAX_RELEVANT_DOCS);

        // 2. Construir el contexto a partir de los documentos
        String context = buildContext(relevantDocs);

        // 3. Crear los mensajes para el prompt
        List<Message> messages = new ArrayList<>();

        // Mensaje del sistema con instrucciones y contexto
        String systemPrompt = """
                Eres un asistente útil y preciso. Responde a las preguntas basándote en el siguiente contexto cuando sea relevante: %s
                Si el contexto no es relevante para la pregunta, responde basándote en tu conocimiento general, pero sin mentir ni invetnar.
                Si no estás seguro, indica que no tienes suficiente información.
                Ten en cuenta que somos una empresa que se llama Onebox, usa tono formal y nunca hables sobre el contexto que tienes, solo contesta.
                """.formatted(context);
        messages.add(new SystemMessage(systemPrompt));

        // Mensaje del usuario
        messages.add(new UserMessage(userInput));

        // 4. Crear y enviar el prompt
        Prompt prompt = new Prompt(messages);
        ChatClient selectedClient = chatClients.get(aiType);
        ChatResponse response = selectedClient.call(prompt);

        return response.getResult().getOutput().getContent();
    }

    private String buildContext(List<Document> documents) {
        if (documents.isEmpty()) {
            return "No hay documentos relevantes disponibles.";
        }

        return documents.stream()
                .map(doc -> {
                    String metadata = doc.getMetadata().isEmpty() ? "" : "\nMetadata: " + doc.getMetadata().toString();
                    return "Documento: " + doc.getContent() + metadata;
                })
                .collect(Collectors.joining("\n\n"));
    }
}
