package com.example.aichatbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelUploadRequestDTO {
    private List<ExcelColumnInfo> columnInfo;
}