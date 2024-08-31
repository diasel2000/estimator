import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { User } from '../../model/User';
import { UserService } from "../../service/user.service";
import { CommonModule } from "@angular/common";
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.sass'],
  standalone:true,
  imports: [CommonModule]
})
export class DashboardComponent implements OnInit {
  user: User | null = null;
  subscriptionName: string = '';
  isAdmin: boolean = false;
  isUser: boolean = false;
  isViewer: boolean = false;
  isModerator: boolean = false;
  isEditor: boolean = false;

  constructor(private authService: AuthService, private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
        console.log('User data:', user);
        this.user = user;
        this.subscriptionName = user.subscription ? user.subscription.subscriptionName : 'No Subscription';

        if (user.userRoles && user.userRoles.length > 0) {
          this.isAdmin = user.userRoles.some(role => role.role.name === 'ROLE_ADMIN');
          this.isUser = user.userRoles.some(role => role.role.name === 'ROLE_USER');
          this.isViewer = user.userRoles.some(role => role.role.name === 'ROLE_VIEWER');
          this.isModerator = user.userRoles.some(role => role.role.name === 'ROLE_MODERATOR');
          this.isEditor = user.userRoles.some(role => role.role.name === 'ROLE_EDITOR');
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

  logout(): void {
    this.authService.logoutUser();
  }
}
