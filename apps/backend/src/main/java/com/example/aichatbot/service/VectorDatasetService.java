package com.example.aichatbot.service;

import com.example.aichatbot.model.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class VectorDatasetService {

    private static final int CHUNK_SIZE = 1000; // Número de caracteres por chunk
    private static final int OVERLAP = 200; // Superposición entre chunks para mantener contexto

    private final VectorStore vectorStore;

    @Autowired
    public VectorDatasetService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<org.springframework.ai.document.Document> search(String query, int limit) {
        SearchRequest searchRequest = SearchRequest.query(query)
                .withTopK(limit)
                .withFilterExpression("type == 'document'");
        return vectorStore.similaritySearch(searchRequest);
    }

    public List<org.springframework.ai.document.Document> search(String query) {
        return vectorStore.similaritySearch(query);
    }

    public void addDocument(Document document) {
        vectorStore.add(List.of(convertVectorDocument(document)));
    }

    public boolean addDocuments(List<org.springframework.ai.document.Document> documents) {
        try {
            int batchSize = 100;
            for (int i = 0; i < documents.size(); i += batchSize) {
                int endIndex = Math.min(i + batchSize, documents.size());
                List<org.springframework.ai.document.Document> batch = documents.subList(i, endIndex);
                vectorStore.add(batch);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteDocument(String id) {
        vectorStore.delete(List.of(id));
    }

    public int processAndStoreTextFile(MultipartFile file, String source) throws IOException {
        String content = readFileContent(file);
        List<org.springframework.ai.document.Document> chunks = splitIntoChunks(content, source);
        return addDocuments(chunks) ? chunks.size() : 0;
    }

    public int processAndStoreText(String text, String source) {
        List<org.springframework.ai.document.Document> chunks = splitIntoChunks(text, source);
        return addDocuments(chunks) ? chunks.size() : 0;
    }

    private static List<org.springframework.ai.document.Document> convertVectorDocuments(List<Document> documents) {
        return documents.stream().map(VectorDatasetService::convertVectorDocument).toList();
    }

    private static org.springframework.ai.document.Document convertVectorDocument(Document document) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", document.getSource());
        metadata.put("type", document.getType());
        if (document.getMetadata() != null) {
            metadata.putAll(document.getMetadata());
        }
        org.springframework.ai.document.Document aiDocument = new org.springframework.ai.document.Document(
                document.getContent(),
                metadata);
        return aiDocument;
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

    private List<org.springframework.ai.document.Document> splitIntoChunks(String content, String source) {
        List<org.springframework.ai.document.Document> chunks = new ArrayList<>();
        int contentLength = content.length();
        int startIndex = 0;

        while (startIndex < contentLength) {
            int endIndex = Math.min(startIndex + CHUNK_SIZE, contentLength);

            // Ajustar el final del chunk para que termine en un espacio o salto de línea
            while (endIndex < contentLength && !Character.isWhitespace(content.charAt(endIndex - 1))) {
                endIndex--;
            }

            String chunkContent = content.substring(startIndex, endIndex);
            org.springframework.ai.document.Document document = new org.springframework.ai.document.Document(chunkContent);
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
