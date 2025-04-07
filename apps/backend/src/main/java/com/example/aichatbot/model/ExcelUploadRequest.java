package com.example.aichatbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelUploadRequest {
    private String fileName;
    private List<ExcelColumnInfo> columnInfo;
    private List<List<String>> rows;
    private String sheetName;
    private Integer totalRows;
    private Integer totalColumns;
}