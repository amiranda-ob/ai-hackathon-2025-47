import { Component, output, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
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
        MatTabsModule,
        MatMenuModule
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
    contextMenuPosition = { x: '0px', y: '0px' };

    @ViewChild(MatMenuTrigger) contextMenu!: MatMenuTrigger;

    constructor() {
        effect(() => {
            const file = this.excelFile();
            if (file) {
                this.displayedColumns = file.headers;
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

    onColumnHeaderClick(event: MouseEvent, columnIndex: number): void {
        event.preventDefault();
        
        // Set the position of the context menu
        this.contextMenuPosition.x = `${event.clientX}px`;
        this.contextMenuPosition.y = `${event.clientY}px`;
        
        // Store the column index for later use
        this.contextMenu.menuData = { columnIndex };
        
        // Open the context menu
        this.contextMenu.openMenu();
    }

    selectColumnCategory(columnIndex: number, category: 'questions' | 'answers'): void {
        this.columnSelected.emit({ columnIndex, category });
    }
} 