package com.example.aichatbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionSelectionRequest {
    private Integer rowIndex;
    private String selectedOption;
    private String fileName;
}