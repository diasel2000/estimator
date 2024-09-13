import {Component, CUSTOM_ELEMENTS_SCHEMA, ElementRef, OnInit, ViewChild} from '@angular/core';
import { UserService } from "../../service/user.service";
import { Router } from '@angular/router';
import { User } from '../../model/User';
import { CommonModule } from "@angular/common";
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import {MatChipsModule} from "@angular/material/chips";

interface Developer {
  name: string;
  status: string;
  role: string;
  projects: string[];
}

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

  developer: Developer | null = null;

  @ViewChild('sliderContent', { static: false }) sliderContent: ElementRef | undefined;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.loadUser();
    this.loadRecentlyVisited();
    this.loadDeveloper();
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
    this.recentlyVisited = [
      { name: 'Project 1', type: 'Project', route: '/dashboard/projects/1' },
      { name: 'Task 2', type: 'Task', route: '/dashboard/tasks/2' },
      { name: 'Developer 3', type: 'Developer', route: '/dashboard/developers/3' }
    ];
  }

  private loadDeveloper(): void {
    this.developer = {
      name: 'John Doe',
      status: 'Active',
      role: 'Programmer',
      projects: ['Project 1', 'Project 2', 'Project 3']
    };
  }

  navigateTo(item: RecentlyVisitedItem): void {
    this.router.navigate([item.route]);
  }

  addRecentlyVisited(): void {
    const newItem: RecentlyVisitedItem = {
      name: `New Project ${this.recentlyVisited.length + 1}`,
      type: 'Project',
      route: `/dashboard/projects/new/${this.recentlyVisited.length + 1}`
    };
    this.recentlyVisited.push(newItem);
  }

  scroll(direction: number): void {
    if (this.sliderContent) {
      const scrollAmount = 240;
      this.sliderContent.nativeElement.scrollBy({
        left: direction * scrollAmount,
        behavior: 'smooth'
      });
    }
  }
}
