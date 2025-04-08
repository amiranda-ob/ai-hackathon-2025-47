import { Component, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { ExcelSheet, CategoryMapping } from '../../../domain/excel/models/excel.model';
import { input } from '@angular/core';
import { effect } from '@angular/core';

@Component({
    selector: 'app-excel-viewer',
    standalone: true,
    imports: [
        CommonModule,
        MatTableModule,
        MatButtonModule,
        MatTabsModule
    ],
    templateUrl: './excel-viewer.component.html',
    styleUrls: ['./excel-viewer.component.scss']
})
export class ExcelViewerComponent {
    excelFile = input<ExcelSheet | null>(null);
    categoryMapping = input<CategoryMapping | null>(null);
    columnSelected = output<{ columnIndex: number, category: 'questions' | 'answers' }>();

    displayedColumns: string[] = [];
    dataSource: any[] = [];

    constructor() {
        effect(() => {
            const file = this.excelFile();
            console.log('=> file', file);
            if (file) {
                this.displayedColumns = file.headers;
                console.log('=> displayedColumns', this.displayedColumns);
                this.dataSource = file.data.map(row => {
                    const obj: any = {};
                    file.headers.forEach((header, index) => {
                        obj[header] = row[index];
                    });
                    return obj;
                });
            }
        });
    }

    getColumnClass(columnIndex: number): string {
        const mapping = this.categoryMapping();
        if (!mapping) return '';
        if (columnIndex === mapping.questionColumnIndex) return 'question-column';
        if (columnIndex === mapping.answerColumnIndex) return 'answer-column';
        return '';
    }

    onColumnHeaderClick(columnIndex: number): void {
        const mapping = this.categoryMapping();
        if (!mapping) {
            this.columnSelected.emit({ columnIndex, category: 'questions' });
        } else if (mapping.questionColumnIndex === columnIndex) {
            this.columnSelected.emit({ columnIndex, category: 'answers' });
        } else if (mapping.answerColumnIndex === columnIndex) {
            this.columnSelected.emit({ columnIndex, category: 'questions' });
        } else {
            this.columnSelected.emit({ columnIndex, category: 'questions' });
        }
    }
} 