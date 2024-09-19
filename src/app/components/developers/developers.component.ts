import { Component } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { } from '@angular/material';
import {DeveloperDialogComponent} from "../developer-dialog/developer-dialog.component";
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatToolbarModule} from "@angular/material/toolbar";

@Component({
  selector: 'app-developers',
  standalone: true,
  imports: [
    MatDialogModule, CommonModule, FormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatToolbarModule
  ],
  templateUrl: './developers.component.html',
  styleUrls: ['./developers.component.sass']
})
export class DevelopersComponent {
  developers: any[] = [];
  currentPage = 1;
  pageSize = 8;

  constructor(private dialog: MatDialog) {}

  openAddDeveloperDialog(): void {
    const dialogRef = this.dialog.open(DeveloperDialogComponent, {
      width: '600px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.developers.push(result);
      }
    });
  }

  get paginatedDevelopers() {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.developers.slice(startIndex, startIndex + this.pageSize);
  }

  changePage(page: number): void {
    this.currentPage = page;
  }
}
