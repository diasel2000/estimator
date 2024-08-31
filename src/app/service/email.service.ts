import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EmailService {
  generateMailtoLink(subject: string, body: string): string {
    const encodedSubject = encodeURIComponent(subject);
    const encodedBody = encodeURIComponent(body);
    return `mailto:estimateyouritapp@gmail.com?subject=${encodedSubject}&body=${encodedBody}`;
  }
}
