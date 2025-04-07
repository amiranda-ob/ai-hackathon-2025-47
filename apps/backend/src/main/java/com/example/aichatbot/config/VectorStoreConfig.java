package com.example.aichatbot.config;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorStoreConfig {

    @Value("${spring.ai.qdrant.url}")
    private String qdrantUrl;

    @Value("${spring.ai.qdrant.port}")
    private Integer qdrantPort;

    @Value("${spring.ai.qdrant.collection-name}")
    private String collectionName;

    @Bean
    public VectorStore vectorStore(EmbeddingClient embeddingClient) {
        QdrantVectorStore.QdrantVectorStoreConfig.Builder builder = QdrantVectorStore.QdrantVectorStoreConfig.builder()
                .withHost(qdrantUrl)
                .withPort(qdrantPort)
                .withCollectionName(collectionName);
        return new QdrantVectorStore(builder.build(), embeddingClient);
    }
}
