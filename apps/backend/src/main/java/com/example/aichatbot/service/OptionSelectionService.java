package com.example.aichatbot.service;

import com.example.aichatbot.model.OptionSelectionRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class OptionSelectionService {

    public void persistOptionSelection(OptionSelectionRequest request, MultipartFile file) throws IOException {
        // Aquí puedes implementar la lógica para persistir la opción seleccionada en el
        // archivo Excel
        // Por ejemplo, puedes abrir el archivo Excel, buscar la fila correspondiente y
        // actualizar la celda de respuesta
        try (FileInputStream fis = new FileInputStream(new File(request.getFileName()));
                Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(request.getRowIndex());
            if (row != null) {
                // Aquí puedes implementar la lógica para actualizar la celda de respuesta
                // Por ejemplo, si la columna de respuesta es la 5, puedes actualizar la celda 5
                // de la fila
                Cell cell = row.getCell(5);
                if (cell == null) {
                    cell = row.createCell(5);
                }
                cell.setCellValue(request.getSelectedOption());
            }
            // Guardar los cambios en el archivo Excel
            try (FileOutputStream fos = new FileOutputStream(new File(request.getFileName()))) {
                workbook.write(fos);
            }
        }
    }
}