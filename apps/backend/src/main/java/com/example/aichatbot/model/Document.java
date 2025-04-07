package com.example.aichatbot.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    private String id;
    private String content;
    private Map<String, String> metadata;
    private LocalDateTime createdAt;
    private float[] embedding;
    private String source;
    private String type;
}