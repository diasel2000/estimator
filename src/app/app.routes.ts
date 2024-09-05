import {RouterModule, Routes} from '@angular/router';
import { AuthGuardService } from './service/auth-guard.service';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import {RegisterComponent} from "./components/register/register.component";
import {NgModule} from "@angular/core";
import {ProfileComponent} from "./components/profile/profile.component";

export const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuardService] },
  { path: 'users/profile', component: ProfileComponent, canActivate: [AuthGuardService] },
  { path: 'oauth2/authorization/google', component: DashboardComponent, canActivate: [AuthGuardService] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes),],
  exports: [RouterModule]
})
export class AppRoutingModule {}
