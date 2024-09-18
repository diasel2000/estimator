import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import {RouterModule} from "@angular/router";
import {DashboardComponent} from "../dashboard/dashboard.component";
import {TaskDialogComponent} from "../task-dialog/task-dialog.component";
import {MatIconModule} from "@angular/material/icon";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatListModule} from "@angular/material/list";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [RouterModule,DashboardComponent,MatIconModule,MatCheckboxModule,MatListModule,MatDialogModule,CommonModule],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.sass'
})
export class TaskListComponent {
  taskForm: FormGroup;

  constructor(private fb: FormBuilder, private dialog: MatDialog) {
    this.taskForm = this.fb.group({
      tasks: this.fb.array([])
    });
    this.addTask();
  }

  get tasks(): FormArray {
    return this.taskForm.get('tasks') as FormArray;
  }

  addTask(): void {
    const task = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      completed: [false]
    });
    this.tasks.push(task);
  }

  editTask(index: number): void {
    const task = this.tasks.at(index).value;
    const dialogRef = this.dialog.open(TaskDialogComponent, {
      data: task
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.tasks.at(index).patchValue(result);
      }
    });
  }

  deleteTask(index: number): void {
    this.tasks.removeAt(index);
  }
}
