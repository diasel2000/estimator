import { Component } from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {MatCheckboxChange, MatCheckboxModule} from '@angular/material/checkbox';
import {CommonModule} from "@angular/common";
import {MatListModule} from "@angular/material/list";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-getting-started',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCheckboxModule,
    MatListModule,
    MatCardModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule],
  templateUrl: './getting-started.component.html',
  styleUrls: ['./getting-started.component.sass']
})
export class GettingStartedComponent {
  steps = [
    { label: 'Dashboard', route: '/dashboard', completed: false, description: 'Get familiar with your dashboard.' },
    { label: 'Projects', route: '/dashboard/projects', completed: false, description: 'Explore project management tools.' },
    { label: 'Tasks', route: '/dashboard/tasks', completed: false, description: 'Learn how to manage your tasks.' },
    { label: 'Developers', route: '/dashboard/developers', completed: false, description: 'Find and connect with developers.' }
  ];

  constructor(private router: Router) {}

  onCheckboxChange(event: MatCheckboxChange, step: any) {
    step.completed = event.checked;
    if (event.checked) {
      this.router.navigate([step.route]);
    }
  }
}
