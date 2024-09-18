import { Component, Inject } from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA, MatDialogModule} from '@angular/material/dialog';
import { FormBuilder, FormGroup } from '@angular/forms';
import {RouterModule} from "@angular/router";
import {DashboardComponent} from "../dashboard/dashboard.component";
import {MatIconModule} from "@angular/material/icon";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatListModule} from "@angular/material/list";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-task-dialog',
  standalone: true,
  imports: [RouterModule,DashboardComponent,MatIconModule,MatCheckboxModule,MatListModule,MatDialogModule,CommonModule],
  templateUrl: './task-dialog.component.html',
  styleUrls: ['./task-dialog.component.sass']
})
export class TaskDialogComponent {
  taskForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.taskForm = this.fb.group({
      title: [data.title],
      description: [data.description],
      completed: [data.completed]
    });
  }

  save(): void {
    this.dialogRef.close(this.taskForm.value);
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
