import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { ExcelViewerComponent } from './presentation/components/excel-viewer/excel-viewer.component';
import { ExcelService } from './domain/excel/services/excel.service';
import { ExcelApiService } from './infrastructure/api/excel-api.service';
import { ExcelFile, CategoryMapping } from './domain/excel/models/excel.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatTabsModule,
    ExcelViewerComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  excelFile: ExcelFile | null = null;
  categoryMapping: CategoryMapping | null = null;

  constructor(
    private excelService: ExcelService,
    private excelApiService: ExcelApiService
  ) {
    this.excelService.getExcelFile().subscribe(file => {
      this.excelFile = file;
    });

    this.excelService.getCategoryMapping().subscribe(mapping => {
      this.categoryMapping = mapping;
    });
  }

  async onFileSelected(event: Event): Promise<void> {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      try {
        await this.excelService.readExcelFile(input.files[0]);
      } catch (error) {
        console.error('Error loading file:', error);
      }
    }
  }

  onColumnSelected(event: { columnIndex: number, category: 'questions' | 'answers' }): void {
    if (!this.excelFile) return;

    const newMapping: CategoryMapping = {
      ...(this.categoryMapping || {
        questionColumnIndex: -1,
        answerColumnIndex: -1,
        sheetIndex: this.excelFile.activeSheet
      }),
      [event.category === 'questions' ? 'questionColumnIndex' : 'answerColumnIndex']: event.columnIndex
    };

    this.excelService.setCategoryMapping(newMapping);
  }

  onSheetChange(index: number): void {
    this.excelService.setActiveSheet(index);
  }

  async uploadMapping(): Promise<void> {
    if (!this.categoryMapping) return;

    try {
      await this.excelApiService.uploadCategoryMapping(this.categoryMapping).toPromise();
      // Handle success
    } catch (error) {
      console.error('Error uploading mapping:', error);
      // Handle error
    }
  }
}
