import {Component, HostListener, OnInit} from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { User } from '../../model/User';
import { UserService } from "../../service/user.service";
import { CommonModule } from "@angular/common";
import { Router } from '@angular/router';
import { FormsModule } from "@angular/forms";

interface Project {
  name: string;
}

interface Task {
  name: string;
}

interface Developer {
  name: string;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.sass'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class DashboardComponent implements OnInit {
  user: User | null = null;
  subscriptionName: string = '';
  roles = {
    isAdmin: false,
    isUser: false,
    isViewer: false,
    isModerator: false,
    isEditor: false,
  };

  sidebarHidden = false;
  showUserMenu = false;
  searchQuery = '';
  dropdowns: { [key: string]: boolean } = {
    projects: false,
    tasks: false,
    developers: false,
  };

  projects: Project[] = [];
  tasks: Task[] = [];
  developers: Developer[] = [];

  constructor(private authService: AuthService, private userService: UserService, private router: Router) {}

  ngOnInit(): void {

    this.projects = [{ name: 'Project 1' }, { name: 'Project 2' }];
    this.tasks = [{ name: 'Task 1' }, { name: 'Task 2' }];
    this.developers = [{ name: 'Dev 1' }, { name: 'Dev 2' }];

    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
        console.log('User data:', user);
        this.user = user;
        this.subscriptionName = user.subscription ? user.subscription.subscriptionName : 'No Subscription';

        if (user.userRoles && user.userRoles.length > 0) {
          this.roles.isAdmin = user.userRoles.some(role => role.role.roleName === 'ROLE_ADMIN');
          this.roles.isUser = user.userRoles.some(role => role.role.roleName === 'ROLE_USER');
          this.roles.isViewer = user.userRoles.some(role => role.role.roleName === 'ROLE_VIEWER');
          this.roles.isModerator = user.userRoles.some(role => role.role.roleName === 'ROLE_MODERATOR');
          this.roles.isEditor = user.userRoles.some(role => role.role.roleName === 'ROLE_EDITOR');
        } else {
          console.warn('User roles are not defined');
        }
      },
      error: (err) => {
        console.error('Error fetching user data:', err);
        this.router.navigate(['/login']);
      }
    });
  }

  toggleUserMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  @HostListener('document:click', ['$event'])
  closeMenuOnClickOutside(event: Event): void {
    this.showUserMenu = false;
  }

  stopPropagation(event: Event): void {
    event.stopPropagation();
  }

  toggleDropdown(menu: string): void {
    this.dropdowns[menu] = !this.dropdowns[menu];
  }

  navigate(path: string): void {
    this.router.navigate([path]);
  }

  addProject(): void {
    console.log('Project added');
  }

  addTask(): void {
    console.log('Task added');
  }

  onSearch(): void {
    console.log('Search query:', this.searchQuery);
  }

  logout(): void {
    this.authService.logoutUser();
    this.router.navigate(['/login']);
  }
}
