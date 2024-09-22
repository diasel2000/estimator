import { Component } from '@angular/core';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";

@Component({
  selector: 'app-developer-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, MatDialogModule, MatButtonModule, MatFormFieldModule, MatInputModule],
  templateUrl: './developer-dialog.component.html',
  styleUrls: ['./developer-dialog.component.sass']
})
export class DeveloperDialogComponent {
  developer = {
    name: '',
    status: '',
    location: '',
    workStart: '',
    workEnd: '',
    role: '',
    salary: '',
    customFields: [] as any[]
  };

  constructor(public dialogRef: MatDialogRef<DeveloperDialogComponent>) {}

  addCustomField(): void {
    this.developer.customFields.push({ title: '', description: '', link: '' });
  }

  removeCustomField(index: number): void {
    this.developer.customFields.splice(index, 1);
  }

  addDeveloper(): void {
    this.dialogRef.close(this.developer);
  }
}
