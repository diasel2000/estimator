import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { User } from '../../model/User';
import { UserRole } from '../../model/UserRole';
import { Role } from '../../model/Role';

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

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe((user: User) => {
      this.user = user;

      this.subscriptionName = user.subscription ? user.subscription.name : 'No Subscription';

      this.isAdmin = user.roles.some(role => role.role.name === 'ROLE_ADMIN');
      this.isUser = user.roles.some(role => role.role.name === 'ROLE_USER');
    });
  }
}
