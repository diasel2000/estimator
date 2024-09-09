import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './service/auth-guard.service';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { SubscriptionsComponent } from './components/subscriptions/subscriptions.component';
import { ManageSubscriptionsComponent } from './components/manage-subscriptions/manage-subscriptions.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { HomeComponent } from './components/home/home.component';
import { GettingStartedComponent } from './components/getting-started/getting-started.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { TaskListComponent } from './components/task-list/task-list.component';
import { DevelopersComponent } from './components/developers/developers.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuardService], children: [
      { path: 'home', component: HomeComponent },
      { path: 'getting-started', component: GettingStartedComponent },
      { path: 'projects', component: ProjectsComponent },
      { path: 'tasks', component: TaskListComponent },
      { path: 'developers', component: DevelopersComponent }
    ]
  },
  { path: 'users/profile', component: ProfileComponent, canActivate: [AuthGuardService] },
  { path: 'subscriptions', component: SubscriptionsComponent, canActivate: [AuthGuardService] },
  { path: 'admin/subscriptions', component: ManageSubscriptionsComponent, canActivate: [AuthGuardService] },
  { path: 'admin/users', component: ManageUsersComponent, canActivate: [AuthGuardService] },
  { path: 'oauth2/authorization/google', component: DashboardComponent, canActivate: [AuthGuardService] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
