import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
  standalone: true,
  imports: [RouterOutlet]
})
export class AppComponent {
  constructor(private router: Router) {}

  navigateToLogin() {
    console.log("go")
    this.router.navigate(['login']);
  }
}
