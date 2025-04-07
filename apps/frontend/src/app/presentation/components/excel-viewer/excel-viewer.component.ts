import { Component, Input, Output, EventEmitter, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { ExcelSheet, CategoryMapping } from '../../../domain/excel/models/excel.model';

@Component({
  selector: 'app-excel-viewer',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatTabsModule],
  templateUrl: './excel-viewer.component.html',
  styleUrls: ['./excel-viewer.component.scss']
})
export class ExcelViewerComponent implements OnChanges {
  @Input() excelFile: ExcelSheet | null = null;
  @Input() categoryMapping: CategoryMapping | null = null;
  @Output() columnSelected = new EventEmitter<{ columnIndex: number, category: 'questions' | 'answers' }>();

  displayedColumns: string[] = [];
  dataSource: any[] = [];

  ngOnChanges() {
    if (this.excelFile) {
      this.displayedColumns = this.excelFile.headers;
      this.dataSource = this.excelFile.data.map(row => {
        const obj: any = {};
        this.excelFile!.headers.forEach((header, index) => {
          obj[header] = row[index];
        });
        return obj;
      });
    }
  }

  getColumnClass(columnIndex: number): string {
    if (!this.categoryMapping) return '';
    if (columnIndex === this.categoryMapping.questionColumnIndex) return 'question-column';
    if (columnIndex === this.categoryMapping.answerColumnIndex) return 'answer-column';
    return '';
  }

  onColumnHeaderClick(columnIndex: number): void {
    if (!this.categoryMapping) {
      this.columnSelected.emit({ columnIndex, category: 'questions' });
    } else if (this.categoryMapping.questionColumnIndex === columnIndex) {
      this.columnSelected.emit({ columnIndex, category: 'answers' });
    } else if (this.categoryMapping.answerColumnIndex === columnIndex) {
      this.columnSelected.emit({ columnIndex, category: 'questions' });
    } else {
      this.columnSelected.emit({ columnIndex, category: 'questions' });
    }
  }
} 