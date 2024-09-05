import {RouterModule, Routes} from '@angular/router';
import { AuthGuardService } from './service/auth-guard.service';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import {RegisterComponent} from "./components/register/register.component";
import {NgModule} from "@angular/core";
import {ProfileComponent} from "./components/profile/profile.component";
import {SubscriptionsComponent} from "./components/subscriptions/subscriptions.component";
import {ManageSubscriptionsComponent} from "./components/manage-subscriptions/manage-subscriptions.component";
import {ManageUsersComponent} from "./components/manage-users/manage-users.component";

export const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuardService] },
  { path: 'users/profile', component: ProfileComponent, canActivate: [AuthGuardService] },
  { path: 'oauth2/authorization/google', component: DashboardComponent, canActivate: [AuthGuardService] },
  { path: 'subscriptions', component: SubscriptionsComponent, canActivate: [AuthGuardService] },
  { path: 'admin/subscriptions', component: ManageSubscriptionsComponent, canActivate: [AuthGuardService] },
  { path: 'admin/users', component: ManageUsersComponent, canActivate: [AuthGuardService] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes),],
  exports: [RouterModule]
})
export class AppRoutingModule {}
