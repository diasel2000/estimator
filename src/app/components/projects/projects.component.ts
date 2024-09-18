import { Component } from '@angular/core';
import {RouterModule} from "@angular/router";
import {DashboardComponent} from "../dashboard/dashboard.component";
import {MatTableModule} from "@angular/material/table";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [MatTableModule, MatCardModule, MatButtonModule, DashboardComponent, CommonModule, RouterModule],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.sass'
})
export class ProjectsComponent {

  displayedColumns: string[] = ['role', 'specialists', 'hours', 'hoursPerDay', 'rate', 'cost', 'duration'];
  dataSource = [
    { role: 'UX/UI Designer', specialists: 1, hours: 909, hoursPerDay: 8, rate: 25, cost: 22725, duration: 23 },
    { role: 'Backend Developer', specialists: 1, hours: 7109, hoursPerDay: 8, rate: 30, cost: 213270, duration: 178 },
    { role: 'Front-end Developer', specialists: 1, hours: 1594, hoursPerDay: 8, rate: 20, cost: 31880, duration: 40 },
    { role: 'Project Manager', specialists: 1, hours: 2000, hoursPerDay: 8, rate: 20, cost: 40000, duration: 0 },
    { role: 'QA', specialists: 3, hours: 3000, hoursPerDay: 8, rate: 48, cost: 144000, duration: 25 },
  ];

  replaceBackendDeveloper() {
    this.dataSource = this.dataSource.map(row =>
      row.role === 'Backend Developer'
        ? { ...row, specialists: 2, hours: 7500, rate: 35, cost: 262500 }
        : row
    );
  }
}
