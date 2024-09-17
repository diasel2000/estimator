import { Component } from '@angular/core';
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-more',
  standalone: true,
  imports: [CommonModule
  ],
  templateUrl: './more.component.html',
  styleUrl: './more.component.sass'
})
export class MoreComponent {
  isOpen = false;

  toggleModal() {
    this.isOpen = !this.isOpen;
  }
}
