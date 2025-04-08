import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-unreliable-cell-dialog',
    standalone: true,
    imports: [
        CommonModule,
        MatDialogModule,
        MatButtonModule,
        MatInputModule,
        MatFormFieldModule,
        FormsModule
    ],
    templateUrl: './unreliable-cell-dialog.component.html',
    styleUrls: ['./unreliable-cell-dialog.component.scss']
})
export class UnreliableCellDialogComponent {
    dialogRef = inject(MatDialogRef<UnreliableCellDialogComponent>);
    reason = '';
} 