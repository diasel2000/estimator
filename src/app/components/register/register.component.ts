import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.sass']
})
export class RegisterComponent {
  email: string = '';
  password: string = '';
  confirmPassword: string = '';
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

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
    if (this.password === this.confirmPassword) {
      this.authService.registerUser({ email: this.email, password: this.password }).subscribe({
        next: () => {
          this.successMessage = 'Registration successful';
          setTimeout(() => this.router.navigate(['/login']), 2000);
        },
        error: err => this.errorMessage = 'Registration failed'
      });
    } else {
      this.errorMessage = 'Passwords do not match';
    }
  }
}
