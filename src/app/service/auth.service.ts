import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../environments/environment';
import { User } from '../model/User';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  registerUser(data: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, data).pipe(
      tap(response => console.log('Registration response:', response)),
      catchError(this.handleError<any>('registerUser'))
    );
  }

  loginUser(data: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, data).pipe(
      tap(response => {
        if (isPlatformBrowser(this.platformId)) {
          localStorage.setItem('token', response.token);
          if (response.csrfToken) {
            localStorage.setItem('csrfToken', response.csrfToken);
          }
        }
      }),
      tap(() => {
        this.router.navigate(['/dashboard']);
      }),
      catchError(this.handleError<any>('loginUser'))
    );
  }

  handleGoogleAuthResponse(response: any): void {
    if (isPlatformBrowser(this.platformId) && response.token) {
      localStorage.setItem('token', response.token);
      if (response.csrfToken) {
        localStorage.setItem('csrfToken', response.csrfToken);
      }
      this.router.navigate(['/dashboard']);
    } else {
      console.error('No token received from Google');
      this.router.navigate(['/login']);
    }
  }

  logoutUser(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
      localStorage.removeItem('csrfToken');
    }
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('token');
    }
    return null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getCsrfToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('csrfToken');
    }
    return null;
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: HttpErrorResponse): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return throwError(() => new Error(error.error.message || 'An unknown error occurred'));
    };
  }
}
