import { Component } from '@angular/core';
import {RouterModule} from "@angular/router";
import {DashboardComponent} from "../dashboard/dashboard.component";

@Component({
  selector: 'app-getting-started',
  standalone: true,
  imports: [RouterModule,DashboardComponent],
  templateUrl: './getting-started.component.html',
  styleUrl: './getting-started.component.sass'
})
export class GettingStartedComponent {

}
