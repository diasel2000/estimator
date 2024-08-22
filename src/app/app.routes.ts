import { Routes } from '@angular/router';
import { AuthGuardService } from './service/auth-guard.service';
import { DashboardComponent } from './dashboard/dashboard.component';

export const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuardService] }
];
