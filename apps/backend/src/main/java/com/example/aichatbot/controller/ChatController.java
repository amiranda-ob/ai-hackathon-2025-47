package com.example.aichatbot.controller;

import com.example.aichatbot.model.AIType;
import com.example.aichatbot.model.ExcelColumnInfo;
import com.example.aichatbot.model.ExcelUploadRequest;
import com.example.aichatbot.model.ExcelUploadRequestDTO;
import com.example.aichatbot.model.OptionSelectionRequest;
import com.example.aichatbot.model.RowInteractionRequest;
import com.example.aichatbot.model.RowInteractionResponse;
import com.example.aichatbot.service.ChatService;
import com.example.aichatbot.service.ExcelService;
import com.example.aichatbot.service.OptionSelectionService;
import com.example.aichatbot.service.RowInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ExcelService excelService;
    private final RowInteractionService rowInteractionService;
    private final OptionSelectionService optionSelectionService;
    private final ChatService chatService;

    @Autowired
    public ChatController(ExcelService excelService, RowInteractionService rowInteractionService,
                          OptionSelectionService optionSelectionService, ChatService chatService) {
        this.excelService = excelService;
        this.rowInteractionService = rowInteractionService;
        this.optionSelectionService = optionSelectionService;
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        AIType aiType = request.type() != null ? request.type() : AIType.OLLAMA;
        String response = chatService.chat(request.message(), aiType);
        return ResponseEntity.ok(new ChatResponse(response, aiType.name(), System.currentTimeMillis()));
    }

    @PostMapping("/upload")
    public ResponseEntity<ExcelUploadRequest> uploadExcel(@RequestParam("file") MultipartFile file,
                                                          @RequestBody ExcelUploadRequestDTO requestDTO) {
        try {
            List<ExcelColumnInfo> columnInfo = requestDTO.getColumnInfo();
            ExcelUploadRequest response = excelService.processExcelFile(file, columnInfo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/row/{rowIndex}")
    public ResponseEntity<RowInteractionResponse> interactWithRow(@RequestBody RowInteractionRequest request) {
        try {
            RowInteractionResponse response = rowInteractionService.processRowInteraction(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/row/{rowIndex}")
    public ResponseEntity<Void> selectOption(@RequestBody OptionSelectionRequest request,
                                             @RequestParam("file") MultipartFile file) {
        try {
            optionSelectionService.persistOptionSelection(request, file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    record ChatRequest(String message, AIType type) {
    }

    record ChatResponse(String response, String aiType, long timestamp) {
    }

}


