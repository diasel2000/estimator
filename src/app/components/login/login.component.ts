import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { CommonModule } from "@angular/common";
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass'],
  standalone: true,
  imports: [CommonModule]
})
export class LoginComponent implements OnInit {
  email: string = '';
  password: string = '';
  errorMessage: string | null = null;
  showPassword: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      console.log('ngOnInit called');
      this.handleGoogleAuthResponse();
    }
  }

  onEmailChange(event: Event) {
    this.email = (event.target as HTMLInputElement).value;
  }

  onPasswordChange(event: Event) {
    this.password = (event.target as HTMLInputElement).value;
  }

  onSubmit(event: Event) {
    event.preventDefault();
    if (this.email && this.password) {
      this.authService.loginUser({ email: this.email, password: this.password }).subscribe({
        next: () => this.router.navigate(['/dashboard']),
        error: err => this.errorMessage = 'Login failed'
      });
    } else {
      this.errorMessage = 'Please fill out all fields';
    }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  private handleGoogleAuthResponse() {
    if (isPlatformBrowser(this.platformId)) {
      const urlParams = new URLSearchParams(window.location.search);
      const token = urlParams.get('token');
      if (token) {
        localStorage.setItem('token', token);
        this.router.navigate(['/dashboard']);
      } else {
        this.router.navigate(['/login']);
      }
    }
  }
}
