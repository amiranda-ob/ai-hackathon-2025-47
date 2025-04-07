package com.example.aichatbot.service;

import com.example.aichatbot.model.Document;
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

    @Autowired
    public VectorSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void addDocument(Document document) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", document.getSource());
        metadata.put("type", document.getType());
        if (document.getMetadata() != null) {
            metadata.putAll(document.getMetadata());
        }

        org.springframework.ai.document.Document aiDocument = new org.springframework.ai.document.Document(
                document.getContent(),
                metadata);
        vectorStore.add(List.of(aiDocument));
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

}
