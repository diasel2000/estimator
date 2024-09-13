import {Component, CUSTOM_ELEMENTS_SCHEMA, ElementRef, OnInit, ViewChild} from '@angular/core';
import { UserService } from "../../service/user.service";
import { Router } from '@angular/router';
import { User } from '../../model/User';
import { CommonModule } from "@angular/common";
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import {MatChipsModule} from "@angular/material/chips";
import {FlatTreeControl} from "@angular/cdk/tree";
import {MatTreeFlatDataSource, MatTreeFlattener} from "@angular/material/tree";

interface Developer {
  name: string;
  status: string;
  region: string;
  flag: string;
  workStart: string;
  workEnd: string;
  role: string;
  salary: string;
  workExperience: string[];
  skills: string[];
  languages: string[];
}

interface FlatNode {
  expandable: boolean;
  item: string;
  level: number;
}

const TREE_DATA = [
  {
    item: 'Work Experience',
    children: ['Company 1 (2 years)', 'Company 2 (3 years)']
  },
  {
    item: 'Skills',
    children: ['Angular', 'Java', 'SQL']
  },
  {
    item: 'Languages',
    children: ['English', 'Russian', 'Spanish']
  }
];

interface RecentlyVisitedItem {
  name: string;
  type: 'Project' | 'Task' | 'Developer';
  route: string;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, MatCardModule, MatChipsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.sass',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HomeComponent implements OnInit {
  user: User | null = null;
  recentlyVisited: RecentlyVisitedItem[] = [];

  developer: Developer | null = null;

  private _transformer = (node: any, level: number) => ({
    expandable: !!node.children && node.children.length > 0,
    item: node.item,
    level
  });

  treeControl = new FlatTreeControl<FlatNode>(
    node => node.level,
    node => node.expandable
  );

  treeFlattener = new MatTreeFlattener(
    this._transformer,
    node => node.level,
    node => node.expandable,
    node => node.children
  );

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

  @ViewChild('sliderContent', { static: false }) sliderContent: ElementRef | undefined;

  constructor(private userService: UserService, private router: Router) {this.dataSource.data = TREE_DATA;}

  ngOnInit(): void {
    this.loadUser();
    this.loadRecentlyVisited();
    this.loadDeveloper();
  }

  private loadUser(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => this.user = user,
      error: (err) => {
        console.error('Error fetching user data:', err);
        this.router.navigate(['/login']);
      }
    });
  }

  private loadRecentlyVisited(): void {
    this.recentlyVisited = [
      { name: 'Project 1', type: 'Project', route: '/dashboard/projects/1' },
      { name: 'Task 2', type: 'Task', route: '/dashboard/tasks/2' },
      { name: 'Developer 3', type: 'Developer', route: '/dashboard/developers/3' }
    ];
  }

  hasChild = (_: number, node: FlatNode) => node.expandable;

  private loadDeveloper(): void {
    this.developer = {
      name: 'John Doe',
      status: 'Active',
      region: 'USA',
      flag: '/assets/flags/usa.png',
      workStart: 'Jan 2020',
      workEnd: 'Present',
      role: 'Senior Programmer',
      salary: '$120,000/year',
      workExperience: ['Company 1', 'Company 2'],
      skills: ['Angular', 'Java', 'SQL'],
      languages: ['English', 'Russian', 'Spanish']
    };
  }

  navigateTo(item: RecentlyVisitedItem): void {
    this.router.navigate([item.route]);
  }

  addRecentlyVisited(): void {
    const newItem: RecentlyVisitedItem = {
      name: `New Project ${this.recentlyVisited.length + 1}`,
      type: 'Project',
      route: `/dashboard/projects/new/${this.recentlyVisited.length + 1}`
    };
    this.recentlyVisited.push(newItem);
  }

  scroll(direction: number): void {
    if (this.sliderContent) {
      const scrollAmount = 240;
      this.sliderContent.nativeElement.scrollBy({
        left: direction * scrollAmount,
        behavior: 'smooth'
      });
    }
  }
}
