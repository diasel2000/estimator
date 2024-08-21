import { Subscription } from './Subscription';
import { UserRole } from './UserRole';

export class User {
  constructor(
    public id: number,
    public username: string,
    public email: string,
    public googleId: string,
    public createdAt: Date,
    public subscription: Subscription,
    public roles: UserRole[]
  ) {}
}
