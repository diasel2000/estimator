import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { User } from '../../model/User';
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-manage-users',
  templateUrl: './manage-users.component.html',
  styleUrls: ['./manage-users.component.sass'],
  standalone: true,
  imports: [CommonModule]
})
export class ManageUsersComponent implements OnInit {
  users: User[] = [];
  errorMessage: string = '';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.fetchUsers();
  }

  fetchUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
      },
      error: (error) => {
        this.errorMessage = error;
      }
    });
  }

  deleteUser(userId: number): void {
    if (!userId) {
      console.error('User ID is invalid:', userId);
      alert('Cannot delete user: Invalid ID');
      return;
    }

    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUserById(userId).subscribe({
        next: () => {
          this.users = this.users.filter(user => user.userID !== userId);
          alert('User deleted successfully');
        },
        error: (error) => {
          this.errorMessage = error;
        }
      });
    }
  }

}
