import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
  standalone: true,
  imports: [RouterOutlet, CommonModule, RouterModule]
})
export class AppComponent {
  currentUrl: string = '';

  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      const url = this.router.url;
      this.currentUrl = url;
      const fragment = this.router.parseUrl(url).fragment;
      if (this.isHomePage() && fragment) {
        const element = document.getElementById(fragment);
        if (element) {
          element.scrollIntoView({ behavior: 'smooth' });
        }
      }
    });
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

  isHomePage(): boolean {
    // Check if the current URL is either the root '/' or the root with a fragment
    return this.currentUrl === '/' || this.currentUrl.startsWith('/') && this.router.parseUrl(this.currentUrl).fragment !== undefined;
  }

  shouldDisplayHomePage(): boolean {
    // Display home page content if the current route is home or contains a fragment
    return this.isHomePage() && (this.currentUrl === '/' || this.currentUrl.includes('#'));
  }
}
