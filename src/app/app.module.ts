import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from './components/register/register.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import { AuthInterceptor } from './interceptors/auth.interceptor';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {LoginComponent} from "./components/login/login.component";
import {AppRoutingModule} from "./app.routes";
import {AuthGuardService} from "./service/auth-guard.service";
import {AuthService} from "./service/auth.service";
import {AppComponent} from "./app.component";

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    DashboardComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    AuthService,
    AuthGuardService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
