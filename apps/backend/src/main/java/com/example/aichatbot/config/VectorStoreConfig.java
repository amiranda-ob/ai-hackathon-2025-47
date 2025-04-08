package com.example.aichatbot.config;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
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
    public EmbeddingClient ollamaEmbeddingClient(OllamaApi ollamaApi) {
        return new OllamaEmbeddingClient(ollamaApi);
    }

    @Bean
    public EmbeddingClient openAiEmbeddingClient(OpenAiApi openAiApi) {
        return new OpenAiEmbeddingClient(openAiApi);
    }

    @Bean
    public VectorStore vectorStore(EmbeddingClient ollamaEmbeddingClient) {
        QdrantVectorStore.QdrantVectorStoreConfig.Builder builder = QdrantVectorStore.QdrantVectorStoreConfig.builder()
                .withHost(qdrantUrl)
                .withPort(qdrantPort)
                .withCollectionName(collectionName);
        return new QdrantVectorStore(builder.build(), ollamaEmbeddingClient);
    }
}
