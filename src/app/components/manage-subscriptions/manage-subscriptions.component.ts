import { Component, OnInit } from '@angular/core';
import { SubscriptionService } from '../../service/subscription.service';
import { HttpClient } from '@angular/common/http';
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-manage-subscriptions',
  templateUrl: './manage-subscriptions.component.html',
  styleUrls: ['./manage-subscriptions.component.sass'],
  standalone: true,
  imports: [CommonModule]
})
export class ManageSubscriptionsComponent implements OnInit {
  subscriptions: any[] = [];

  constructor(private subscriptionService: SubscriptionService, private http: HttpClient) {}

  ngOnInit(): void {
    this.loadSubscriptions();
  }

  loadSubscriptions(): void {
    this.subscriptionService.getAllSubscriptions().subscribe(
      (data: any[]) => {
        this.subscriptions = data;
      },
      (error) => {
        console.error('Error loading subscriptions', error);
      }
    );
  }

  deleteSubscription(subscriptionID: number): void {
    this.http.delete(`/api/admin/subscriptions/${subscriptionID}`).subscribe(
      () => {
        this.subscriptions = this.subscriptions.filter(sub => sub.subscriptionID !== subscriptionID);
        console.log(`Deleted subscription with ID: ${subscriptionID}`);
      },
      (error) => {
        console.error('Error deleting subscription', error);
      }
    );
  }
}
