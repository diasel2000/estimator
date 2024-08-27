import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from './components/register/register.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";

@NgModule({
  declarations: [
    RegisterComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    CommonModule
  ],
  providers: [],
  bootstrap: []
})
export class AppModule { }
