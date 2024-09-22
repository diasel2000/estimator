import { Component } from '@angular/core';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

interface CustomField {
  title: string;
  description: string;
  link: string;
}

@Component({
  selector: 'app-developer-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  templateUrl: './developer-dialog.component.html',
  styleUrls: ['./developer-dialog.component.sass']
})
export class DeveloperDialogComponent {
  countries = ['USA', 'Canada', 'UK', 'Germany', 'France', 'Other'];
  roles = ['PM', 'Developer', 'Tester', 'Designer', 'DevOps', 'Other'];

  developer = {
    name: '',
    status: '',
    location: '',
    customLocation: '',
    workStart: '',
    workEnd: '',
    role: '',
    salary: '',
    customFields: [] as CustomField[]
  };

  constructor(public dialogRef: MatDialogRef<DeveloperDialogComponent>) {}

  addCustomField(): void {
    this.developer.customFields.push({ title: '', description: '', link: '' });
  }

  removeCustomField(index: number): void {
    this.developer.customFields.splice(index, 1);
  }

  addDeveloper(): void {
    if (this.developer.workStart && this.developer.workEnd) {
      const startDate = new Date(this.developer.workStart);
      const endDate = new Date(this.developer.workEnd);
      if (endDate <= startDate) {
        alert('End date must be later than Start date.');
        return;
      }
    }
    this.dialogRef.close(this.developer);
  }
}
