package com.example.aichatbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelColumnInfo {
    private Integer columnIndex;
    private String columnName;
    private String columnType; // Por ejemplo, "pregunta", "respuesta", etc.
}