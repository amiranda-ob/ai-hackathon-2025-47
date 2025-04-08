import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CategoryMapping } from '../../domain/excel/models/excel.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExcelApiService {
  private readonly API_URL = '/api/upload';

  constructor(private http: HttpClient) {}

  uploadCategoryMapping(mapping: CategoryMapping): Observable<any> {
    return this.http.post(this.API_URL, mapping);
  }
} 