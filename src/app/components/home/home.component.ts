import {Component, CUSTOM_ELEMENTS_SCHEMA, OnInit} from '@angular/core';
import { UserService } from "../../service/user.service";
import { Router } from '@angular/router';
import { User } from '../../model/User';
import { CommonModule } from "@angular/common";
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import {MatChipsModule} from "@angular/material/chips";

interface RecentlyVisitedItem {
  name: string;
  type: 'Project' | 'Task' | 'Developer';
  route: string;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, MatCardModule, MatChipsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.sass',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HomeComponent implements OnInit {
  user: User | null = null;
  recentlyVisited: RecentlyVisitedItem[] = [];

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.loadUser();
    this.loadRecentlyVisited();
  }

  private loadUser(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => this.user = user,
      error: (err) => {
        console.error('Error fetching user data:', err);
        this.router.navigate(['/login']);
      }
    });
  }

  private loadRecentlyVisited(): void {
    // Sample recently visited data
    this.recentlyVisited = [
      { name: 'Project 1', type: 'Project', route: '/dashboard/projects/1' },
      { name: 'Task 2', type: 'Task', route: '/dashboard/tasks/2' },
      { name: 'Developer 3', type: 'Developer', route: '/dashboard/developers/3' }
    ];
  }

  navigateTo(item: RecentlyVisitedItem): void {
    this.router.navigate([item.route]);
  }
}
