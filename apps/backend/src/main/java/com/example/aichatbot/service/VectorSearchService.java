package com.example.aichatbot.service;

import com.example.aichatbot.model.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VectorSearchService {

    private final VectorStore vectorStore;
    private final EmbeddingClient embeddingClient;

    @Autowired
    public VectorSearchService(VectorStore vectorStore, EmbeddingClient embeddingClient) {
        this.vectorStore = vectorStore;
        this.embeddingClient = embeddingClient;
    }

    public void addDocument(Document document) {
        vectorStore.add(List.of(convertVectorDocument(document)));
    }

    private static List<org.springframework.ai.document.Document> convertVectorDocuments(List<Document> documents) {
        return documents.stream().map(VectorSearchService::convertVectorDocument).toList();
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

    public List<org.springframework.ai.document.Document> search(String query, int limit) {
        SearchRequest searchRequest = SearchRequest.query(query)
                .withTopK(limit)
                .withFilterExpression("type == 'document'");
        return vectorStore.similaritySearch(searchRequest);
    }

    public void deleteDocument(String id) {
        vectorStore.delete(List.of(id));
    }

    public List<org.springframework.ai.document.Document> search(String query) {
        return vectorStore.similaritySearch(query);
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

}
