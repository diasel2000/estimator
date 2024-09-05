import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { User } from '../../model/User';
import { Router } from '@angular/router';
import {CommonModule} from "@angular/common";
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.sass'],
  standalone: true,
  imports: [CommonModule]
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  subscriptions: string[] = ['Basic', 'Premium', 'Pro'];
  isEditing: boolean = false;
  errorMessage: string | null = null;

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

  onSubmit(event: Event): void {
    event.preventDefault();
    if (this.user) {
      const form = event.target as HTMLFormElement;
      const formData = new FormData(form);
      const subscriptionName = formData.get('subscription')?.toString() || '';

      this.userService.updateSubscription(subscriptionName).subscribe({
        next: () => {
          this.isEditing = false;
          this.loadProfile();
        },
        error: (err) => {
          this.errorMessage = 'Failed to update profile. Please try again later.';
          console.error('Error updating profile:', err);
        }
      });
    }
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

  logout(): void {
    this.authService.logoutUser();
    this.router.navigate(['/login']);
  }
}
