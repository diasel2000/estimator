import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.sass']
})
export class RegisterComponent {
  username: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  onUsernameChange(event: Event) {
    this.username = (event.target as HTMLInputElement).value;
  }

  onEmailChange(event: Event) {
    this.email = (event.target as HTMLInputElement).value;
  }

  onPasswordChange(event: Event) {
    this.password = (event.target as HTMLInputElement).value;
  }

  onConfirmPasswordChange(event: Event) {
    this.confirmPassword = (event.target as HTMLInputElement).value;
  }

  onSubmit(event: Event) {
    event.preventDefault();

    this.successMessage = null;
    this.errorMessage = null;

    if (this.password === this.confirmPassword) {
      this.authService.registerUser({
        username: this.username,
        email: this.email,
        password: this.password
      }).subscribe({
        next: response => {
          this.successMessage = response.message || 'Registration successful';
          setTimeout(() => this.router.navigate([response.redirectTo || '/login']), 2000);
        },
        error: err => {
          this.errorMessage = err.message || 'Registration failed';
        }
      });
    } else {
      this.errorMessage = 'Passwords do not match';
    }
  }
}
