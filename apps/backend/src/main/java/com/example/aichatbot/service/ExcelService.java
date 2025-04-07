package com.example.aichatbot.service;

import com.example.aichatbot.model.ExcelColumnInfo;
import com.example.aichatbot.model.ExcelUploadRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    public ExcelUploadRequest processExcelFile(MultipartFile file, List<ExcelColumnInfo> columnInfo)
            throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Obtener filas de datos
            List<List<String>> rows = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    List<String> rowData = new ArrayList<>();
                    for (int j = 0; j < columnInfo.size(); j++) {
                        Cell cell = row.getCell(j);
                        rowData.add(cell != null ? cell.toString() : "");
                    }
                    rows.add(rowData);
                }
            }

            // Crear objeto de respuesta
            return new ExcelUploadRequest(
                    file.getOriginalFilename(),
                    columnInfo,
                    rows,
                    sheet.getSheetName(),
                    rows.size(),
                    columnInfo.size());
        }
    }

    private String determineColumnType(int columnIndex, String columnName) {
        // Aquí puedes definir la lógica para determinar el tipo de columna
        // Por ejemplo, si el nombre de la columna contiene "pregunta", entonces es de
        // tipo "pregunta"
        if (columnName.toLowerCase().contains("pregunta")) {
            return "pregunta";
        } else if (columnName.toLowerCase().contains("respuesta")) {
            return "respuesta";
        }
        return "desconocido";
    }
}