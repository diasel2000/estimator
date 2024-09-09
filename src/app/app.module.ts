import {APP_INITIALIZER, ErrorHandler, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import * as Sentry from '@sentry/angular';

import { AppComponent } from './app.component';
import {AppRoutingModule, routes} from './app.routes';
import { AuthService } from './service/auth.service';
import { AuthGuardService } from './service/auth-guard.service';
import { AuthInterceptor } from './interceptors/auth.interceptor';

import { RegisterComponent } from './components/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { ProfileComponent } from './components/profile/profile.component';
import { SubscriptionsComponent } from './components/subscriptions/subscriptions.component';
import { ManageSubscriptionsComponent } from './components/manage-subscriptions/manage-subscriptions.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { TaskListComponent } from './components/task-list/task-list.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { HomeComponent } from './components/home/home.component';
import { GettingStartedComponent } from './components/getting-started/getting-started.component';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    DashboardComponent,
    LoginComponent,
    ProfileComponent,
    SubscriptionsComponent,
    ManageSubscriptionsComponent,
    ManageUsersComponent,
    TaskListComponent,
    ProjectsComponent,
    HomeComponent,
    GettingStartedComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CommonModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    RouterModule.forRoot(routes)
  ],
  providers: [
    AuthService,
    AuthGuardService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    {
      provide: ErrorHandler,
      useValue: Sentry.createErrorHandler({
        showDialog: true,
      }),
    }, {
      provide: Sentry.TraceService,
      deps: [Router],
    },
    {
      provide: APP_INITIALIZER,
      useFactory: () => () => {},
      deps: [Sentry.TraceService],
      multi: true,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
