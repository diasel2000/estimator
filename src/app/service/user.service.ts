import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getCurrentUser(): Observable<any> {
    return this.http.get(`${this.apiUrl}/profile`);
  }

  updateSubscription(subscriptionName: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/update-subscription`, { subscriptionName });
  }

  deleteUser(email: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/email/${email}`);
  }
}
