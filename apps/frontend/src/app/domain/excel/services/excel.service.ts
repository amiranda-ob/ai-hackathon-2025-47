import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import * as XLSX from 'xlsx';
import { ExcelFile, ExcelSheet, CategoryMapping } from '../models/excel.model';

@Injectable({
  providedIn: 'root'
})
export class ExcelService {
    private excelFileSubject = new BehaviorSubject<ExcelFile | null>(null);
    private categoryMappingSubject = new BehaviorSubject<CategoryMapping | null>(null);

    private generateAlphabetHeaders(columnCount: number, includeRowNumbers = true): string[] {
        const headers: string[] = [];
        if (includeRowNumbers) {
            headers.push('#'); // Use '#' as header for row numbers column
        }
        for (let i = 0; i < columnCount; i++) {
            let header = '';
            let num = i;
            do {
                header = String.fromCharCode(65 + (num % 26)) + header;
                num = Math.floor(num / 26) - 1;
            } while (num >= 0);
            headers.push(header);
        }
        return headers;
    }

    private addRowNumbers(data: (string | number)[][]): (string | number)[][] {
        return data.map((row, index) => {
            return [index + 1, ...row];
        });
    }

    async readExcelFile(file: File): Promise<void> {
        try {
            const arrayBuffer = await file.arrayBuffer();
            const workbook = XLSX.read(arrayBuffer);
            
            const sheets: ExcelSheet[] = workbook.SheetNames.map(sheetName => {
                const worksheet = workbook.Sheets[sheetName];
                const rawData = XLSX.utils.sheet_to_json(worksheet, { header: 1 }) as string[][];
                
                // Get the maximum number of columns in the sheet
                const maxColumns = Math.max(...rawData.map(row => row.length));
                const headers = this.generateAlphabetHeaders(maxColumns, true);
                
                // Add row numbers and remove original headers
                const dataWithRowNumbers = this.addRowNumbers(rawData.slice(1));
                
                return {
                    name: sheetName,
                    data: dataWithRowNumbers,
                    headers,
                    showRowNumbers: true
                };
            });

            this.excelFileSubject.next({
                sheets,
                activeSheet: 0
            });
            this.categoryMappingSubject.next(null);
        } catch (error) {
            console.error('Error reading Excel file:', error);
            throw error;
        }
    }

    setActiveSheet(index: number): void {
        const currentFile = this.excelFileSubject.value;
        if (currentFile && index >= 0 && index < currentFile.sheets.length) {
            this.excelFileSubject.next({
            ...currentFile,
            activeSheet: index
            });
        }
    }

    setCategoryMapping(mapping: CategoryMapping): void {
        this.categoryMappingSubject.next(mapping);
    }

    getExcelFile(): Observable<ExcelFile | null> {
        return this.excelFileSubject.asObservable();
    }

    getCategoryMapping(): Observable<CategoryMapping | null> {
        return this.categoryMappingSubject.asObservable();
    }

    clearData(): void {
        this.excelFileSubject.next(null);
        this.categoryMappingSubject.next(null);
    }
} 