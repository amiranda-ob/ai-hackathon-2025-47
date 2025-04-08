import { Component, output, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { ExcelSheet, CategoryMapping, UnreliableCell } from '../../../domain/excel/models/excel.model';
import { input } from '@angular/core';
import { effect } from '@angular/core';
import { UnreliableCellDialogComponent } from '../unreliable-cell-dialog/unreliable-cell-dialog.component';

@Component({
    selector: 'app-excel-viewer',
    standalone: true,
    imports: [
        CommonModule,
        MatTableModule,
        MatButtonModule,
        MatTabsModule,
        MatMenuModule,
        MatDialogModule,
        MatInputModule,
        MatFormFieldModule,
        FormsModule
    ],
    templateUrl: './excel-viewer.component.html',
    styleUrls: ['./excel-viewer.component.scss']
})
export class ExcelViewerComponent {
    private dialog = inject(MatDialog);

    excelFile = input<ExcelSheet | null>(null);
    categoryMapping = input<CategoryMapping | null>(null);
    columnSelected = output<{ columnIndex: number, category: 'questions' | 'answers' }>();
    unreliableCellMarked = output<UnreliableCell>();

    displayedColumns: string[] = [];
    dataSource: any[] = [];
    contextMenuPosition = { x: '0px', y: '0px' };
    reasonDialogOpen = false;
    selectedCell: { rowIndex: number, columnIndex: number } | null = null;
    reasonText = '';

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

    isCellUnreliable(rowIndex: number, columnIndex: number): boolean {
        const mapping = this.categoryMapping();
        if (!mapping || columnIndex !== mapping.answerColumnIndex) return false;
        return mapping.unreliableCells.some(cell => cell.rowIndex === rowIndex);
    }

    onColumnHeaderClick(event: MouseEvent, columnIndex: number): void {
        event.preventDefault();
        
        this.contextMenuPosition.x = `${event.clientX}px`;
        this.contextMenuPosition.y = `${event.clientY}px`;
        
        this.contextMenu.menuData = { columnIndex };
        
        this.contextMenu.openMenu();
    }

    async onCellClick(event: MouseEvent, rowIndex: number, columnIndex: number): Promise<void> {
        const mapping = this.categoryMapping();
        if (!mapping || columnIndex !== mapping.answerColumnIndex) return;

        event.preventDefault();
        
        const dialogRef = this.dialog.open(UnreliableCellDialogComponent);
        const result = await dialogRef.afterClosed().toPromise();
        
        if (result) {
            const unreliableCell: UnreliableCell = {
                rowIndex,
                reason: result
            };
            this.unreliableCellMarked.emit(unreliableCell);
        }
    }

    selectColumnCategory(columnIndex: number, category: 'questions' | 'answers'): void {
        this.columnSelected.emit({ columnIndex, category });
    }
} 