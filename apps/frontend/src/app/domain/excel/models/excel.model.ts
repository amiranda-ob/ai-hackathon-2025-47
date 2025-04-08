export interface ExcelSheet {
  name: string;
  data: string[][];
  headers: string[];
}

export interface ExcelFile {
  sheets: ExcelSheet[];
  activeSheet: number;
}

export interface ColumnCategory {
  columnIndex: number;
  category: 'questions' | 'answers';
  sheetIndex: number;
}

export interface CategoryMapping {
  questionColumnIndex: number;
  answerColumnIndex: number;
  sheetIndex: number;
} 