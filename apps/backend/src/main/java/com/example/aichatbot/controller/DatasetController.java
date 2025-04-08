package com.example.aichatbot.controller;

import com.example.aichatbot.model.Document;
import com.example.aichatbot.service.VectorDatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/dataset")
public class DatasetController {

    private final VectorDatasetService vectorDatasetService;

    @Autowired
    public DatasetController(VectorDatasetService vectorDatasetService) {
        this.vectorDatasetService = vectorDatasetService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<org.springframework.ai.document.Document>> search(
            @RequestParam("query") String query,
            @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(vectorDatasetService.search(query, limit));
    }

    @PostMapping("/documents")
    public ResponseEntity<Void> addDocument(@RequestBody Document document) {
        vectorDatasetService.addDocument(document);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        vectorDatasetService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<TextProcessingResponse> uploadTextFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "source", defaultValue = "unknown") String source) {
        try {
            int chunksProcessed = vectorDatasetService.processAndStoreTextFile(file, source);
            return ResponseEntity.ok(new TextProcessingResponse(
                    "Archivo procesado correctamente",
                    chunksProcessed,
                    source));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TextProcessingResponse(
                    "Error al procesar el archivo: " + e.getMessage(),
                    0,
                    source));
        }
    }

    @PostMapping("/text-upload")
    public ResponseEntity<TextProcessingResponse> processText(
            @RequestBody TextRequest request) {
        try {
            int chunksProcessed = vectorDatasetService.processAndStoreText(request.text(), request.source());
            return ResponseEntity.ok(new TextProcessingResponse(
                    "Texto procesado correctamente",
                    chunksProcessed,
                    request.source()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TextProcessingResponse(
                    "Error al procesar el texto: " + e.getMessage(),
                    0,
                    request.source()));
        }
    }

    record TextProcessingResponse(String message, int chunksProcessed, String source) {
    }

    record TextRequest(String text, String source) {
    }
}
