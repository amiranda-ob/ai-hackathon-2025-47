package com.example.aichatbot.controller;

import com.example.aichatbot.service.TextProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/text")
@CrossOrigin(origins = "*")
public class TextProcessingController {

    private final TextProcessingService textProcessingService;

    @Autowired
    public TextProcessingController(TextProcessingService textProcessingService) {
        this.textProcessingService = textProcessingService;
    }

    @PostMapping("/upload")
    public ResponseEntity<TextProcessingResponse> uploadTextFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "source", defaultValue = "unknown") String source) {
        try {
            int chunksProcessed = textProcessingService.processAndStoreTextFile(file, source);
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

    @PostMapping("/process")
    public ResponseEntity<TextProcessingResponse> processText(
            @RequestBody TextRequest request) {
        try {
            int chunksProcessed = textProcessingService.processAndStoreText(request.text(), request.source());
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
}

record TextProcessingResponse(String message, int chunksProcessed, String source) {
}

record TextRequest(String text, String source) {
}
