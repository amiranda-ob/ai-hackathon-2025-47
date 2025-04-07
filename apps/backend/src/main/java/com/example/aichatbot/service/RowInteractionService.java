package com.example.aichatbot.service;

import com.example.aichatbot.model.ExcelColumnInfo;
import com.example.aichatbot.model.RowInteractionRequest;
import com.example.aichatbot.model.RowInteractionResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RowInteractionService {

    public RowInteractionResponse processRowInteraction(RowInteractionRequest request) {
        // Aquí puedes implementar la lógica para generar opciones de respuesta basadas
        // en los datos de la fila
        List<String> responseOptions = generateResponseOptions(request.getRowData(), request.getColumnInfo());
        return new RowInteractionResponse(request.getRowIndex(), request.getRowData(), responseOptions);
    }

    private List<String> generateResponseOptions(List<String> rowData, List<ExcelColumnInfo> columnInfo) {
        // Aquí puedes implementar la lógica para generar opciones de respuesta
        // Por ejemplo, si la columna 2 es de tipo "pregunta", puedes generar opciones
        // de respuesta basadas en esa pregunta
        List<String> responseOptions = new ArrayList<>();
        for (ExcelColumnInfo column : columnInfo) {
            if (column.getColumnType().equals("pregunta")) {
                String pregunta = rowData.get(column.getColumnIndex());
                // Aquí puedes implementar la lógica para generar opciones de respuesta basadas
                // en la pregunta
                responseOptions.add("Opción 1 para: " + pregunta);
                responseOptions.add("Opción 2 para: " + pregunta);
                responseOptions.add("Opción 3 para: " + pregunta);
            }
        }
        return responseOptions;
    }
}