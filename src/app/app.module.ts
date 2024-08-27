import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from './components/register/register.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import { AuthInterceptor } from './interceptors/auth.interceptor';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    RegisterComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    HttpClientModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: []
})
export class AppModule { }
