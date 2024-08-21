import { User } from './User';
import { Role } from './Role';
import { UserRoleKey } from './UserRoleKey';

export class UserRole {
  constructor(
    public id: UserRoleKey,
    public user: User,
    public role: Role
  ) {}
}
