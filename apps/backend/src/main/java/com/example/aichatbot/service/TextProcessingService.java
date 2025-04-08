package com.example.aichatbot.service;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TextProcessingService {

    private final VectorSearchService vectorSearchService;
    private static final int CHUNK_SIZE = 1000; // Número de caracteres por chunk
    private static final int OVERLAP = 200; // Superposición entre chunks para mantener contexto

    @Autowired
    public TextProcessingService(VectorSearchService vectorSearchService) {
        this.vectorSearchService = vectorSearchService;
    }

    public int processAndStoreTextFile(MultipartFile file, String source) throws IOException {
        String content = readFileContent(file);
        List<Document> chunks = splitIntoChunks(content, source);
        return vectorSearchService.addDocuments(chunks) ? chunks.size() : 0;
    }

    public int processAndStoreText(String text, String source) {
        List<Document> chunks = splitIntoChunks(text, source);
        return vectorSearchService.addDocuments(chunks) ? chunks.size() : 0;
    }

    private String readFileContent(MultipartFile file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private List<Document> splitIntoChunks(String content, String source) {
        List<Document> chunks = new ArrayList<>();
        int contentLength = content.length();
        int startIndex = 0;

        while (startIndex < contentLength) {
            int endIndex = Math.min(startIndex + CHUNK_SIZE, contentLength);

            // Ajustar el final del chunk para que termine en un espacio o salto de línea
            while (endIndex < contentLength && !Character.isWhitespace(content.charAt(endIndex - 1))) {
                endIndex--;
            }

            String chunkContent = content.substring(startIndex, endIndex);
            Document document = new Document(chunkContent);
            document.getMetadata().put("source", source);
            document.getMetadata().put("chunk_id", UUID.randomUUID().toString());
            document.getMetadata().put("chunk_index", String.valueOf(chunks.size()));
            document.getMetadata().put("type", "document");

            chunks.add(document);

            // Avanzar el índice de inicio, considerando la superposición
            startIndex = endIndex - OVERLAP;
            if (startIndex <= endIndex) {
                startIndex = endIndex; // Asegurar progreso en caso de superposición incorrecta
            }
        }

        return chunks;
    }
}
