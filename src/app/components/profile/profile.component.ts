import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { User } from '../../model/User';
import { Router } from '@angular/router';
import { CommonModule } from "@angular/common";
import { AuthService } from "../../service/auth.service";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.sass'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  subscriptions: string[] = ['Basic', 'Premium', 'Pro'];
  isEditing: boolean = false;
  errorMessage: string | null = null;
  isGoogleAccount: boolean = false;
  activeTab: 'account' | 'support' | 'devices' = 'account';
  showNotificationsModal: boolean = false;
  notifications = {
    mobilePush: false,
    emailAlways: false,
    emailDigest: false
  };

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
        this.user = user;
        //TODO add backend logic
        this.isGoogleAccount = false;
      },
      error: (err) => {
        console.error('Error fetching user data:', err);
        this.router.navigate(['/login']);
      }
    });
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
  }

  onAccountSubmit(event: Event): void {
    event.preventDefault();
    // Update email or password logic here
  }

  deleteAccount(): void {
    if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
      this.userService.deleteUser(this.user?.email || '').subscribe({
        next: () => {
          this.router.navigate(['/register']);
        },
        error: (err) => {
          this.errorMessage = 'Failed to delete account.';
          console.error('Error deleting account:', err);
        }
      });
    }
  }

  logoutFromAllDevices(): void {
    if (confirm('Are you sure you want to log out from all devices?')) {
      this.authService.logoutAllDevices().subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (err) => {
          this.errorMessage = 'Failed to log out from all devices.';
          console.error('Error logging out:', err);
        }
      });
    }
  }

  toggleNotifications(): void {
    this.showNotificationsModal = !this.showNotificationsModal;
  }

  saveNotificationSettings(): void {
    console.log('Notification settings saved:', this.notifications);
    this.toggleNotifications();
  }
}
