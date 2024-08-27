import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { User } from '../../model/User';
import { UserRole } from '../../model/UserRole';
import { Role } from '../../model/Role';
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.sass']
})
export class DashboardComponent implements OnInit {
  user: User | null = null;
  subscriptionName: string = '';
  isAdmin: boolean = false;
  isUser: boolean = false;
  isViewer: boolean = false;
  isModerator: boolean = false;
  isEditor: boolean = false;

  constructor(private authService: AuthService,private userService:UserService) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
        console.log('User data:', user);
        this.user = user;

        this.subscriptionName = user.subscription ? user.subscription.name : 'No Subscription';

        this.isAdmin = user.roles.some(role => role.role.name === 'ROLE_ADMIN');
        this.isUser = user.roles.some(role => role.role.name === 'ROLE_USER');
        this.isViewer = user.roles.some(role => role.role.name === 'ROLE_VIEWER');
        this.isModerator = user.roles.some(role => role.role.name === 'ROLE_MODERATOR');
        this.isEditor = user.roles.some(role => role.role.name === 'ROLE_EDITOR');
      },
      error: (err) => {
        console.error('Error fetching user data:', err);
      }
    });
  }

  logout(): void {
    this.authService.logoutUser();
  }
}
