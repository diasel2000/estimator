import {Component, ElementRef, HostListener, Renderer2} from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import {EmailService} from "./service/email.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
  standalone: true,
  imports: [RouterOutlet, CommonModule, RouterModule]
})
export class AppComponent {
  currentUrl: string = '';
  emailLink: string;

  constructor(private router: Router, private el: ElementRef, private renderer: Renderer2,private emailService: EmailService) {
    const subject = 'Request for Project Estimation';
    const body = `
Dear Estimator,

I hope this message finds you well. I am reaching out to establish contact with your esteemed company to discuss potential opportunities and gather detailed feedback on a project we are currently considering.

Please find below the key details that will help us tailor our request:

Purpose of Contact: [Specify your purpose, e.g., "I am an investor looking to explore potential investment opportunities," "I am a client seeking detailed information regarding your services," "I am inquiring about a specific project or request," "Other topic: [Provide details]"]

Project Name: [Enter the project name]

Project Description: [Provide a brief description of the project]

Estimated Deadline: [Specify the expected completion date]

Budget: [Indicate the budget range]

We look forward to your response and hope to discuss how we can collaborate effectively. If you require any additional information to proceed, please feel free to let us know.

Thank you for your time and consideration.

Best regards,

[Your Full Name]
[Your Position]
[Your Company Name]
[Your Contact Information]
    `;
    this.emailLink = this.emailService.generateMailtoLink(subject, body);
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

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const aboutSection = this.el.nativeElement.querySelector('#about');
    const rect = aboutSection.getBoundingClientRect();
    const viewHeight = window.innerHeight || document.documentElement.clientHeight;

    if (rect.top <= viewHeight - 100) {
      this.renderer.addClass(aboutSection, 'in-view');
    } else {
      this.renderer.removeClass(aboutSection, 'in-view');
    }
  }
}
