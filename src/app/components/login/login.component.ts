import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass'],
  standalone: true,
  imports: [CommonModule]
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  onEmailChange(event: Event) {
    this.email = (event.target as HTMLInputElement).value;
  }

  onPasswordChange(event: Event) {
    this.password = (event.target as HTMLInputElement).value;
  }

  onSubmit(event: Event) {
    event.preventDefault();
    console.log('Form submitted');
    if (this.email && this.password) {
      this.authService.loginUser({ email: this.email, password: this.password }).subscribe({
        next: () => this.router.navigate(['/dashboard']),
        error: err => this.errorMessage = 'Login failed'
      });
    } else {
      this.errorMessage = 'Please fill out all fields';
    }
  }
}
