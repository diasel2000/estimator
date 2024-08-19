import { Component } from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  username: string = '';
  email: string = '';
  password: string = '';
  csrfToken: string = ''; // Обновите, если требуется

  constructor(private authService: AuthService) {}

  onRegister(): void {
    this.authService.register(this.username, this.email, this.password).subscribe(response => {

    }, error => {
    });
  }
}
