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

  constructor() {}

  async readExcelFile(file: File): Promise<void> {
    try {
      const arrayBuffer = await file.arrayBuffer();
      const workbook = XLSX.read(arrayBuffer);
      
      const sheets: ExcelSheet[] = workbook.SheetNames.map(sheetName => {
        const worksheet = workbook.Sheets[sheetName];
        const data = XLSX.utils.sheet_to_json(worksheet, { header: 1 }) as string[][];
        const headers = data[0] || [];
        return {
          name: sheetName,
          data: data.slice(1), // Remove headers from data
          headers
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