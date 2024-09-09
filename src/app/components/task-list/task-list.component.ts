import { Component } from '@angular/core';
import {RouterModule} from "@angular/router";
import {DashboardComponent} from "../dashboard/dashboard.component";

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [RouterModule,DashboardComponent],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.sass'
})
export class TaskListComponent {

}
