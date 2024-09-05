import { Component, OnInit } from '@angular/core';
import { SubscriptionService } from '../../service/subscription.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-subscriptions',
  templateUrl: './subscriptions.component.html',
  styleUrls: ['./subscriptions.component.sass'],
  standalone: true,
  imports: [CommonModule]
})
export class SubscriptionsComponent implements OnInit {
  subscriptions: any[] = [];

  constructor(private subscriptionService: SubscriptionService) {}

  ngOnInit(): void {
    this.loadSubscriptions();
  }

  loadSubscriptions(): void {
    this.subscriptionService.getAllSubscriptions().subscribe((data: any[]) => {
      this.subscriptions = data;
    });
  }

  subscribe(subscriptionID: number): void {
    console.log(`Subscribed to plan ID: ${subscriptionID}`);
  }
}
