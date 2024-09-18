import { Component } from '@angular/core';
import {CommonModule} from "@angular/common";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatIconModule} from "@angular/material/icon";
import {MatListModule} from "@angular/material/list";
import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-more',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule
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
