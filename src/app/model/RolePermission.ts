import { Role } from './Role';
import { Permission } from './Permission';
import { RolePermissionKey } from './RolePermissionKey';

export class RolePermission {
  constructor(
    public id: RolePermissionKey,
    public role: Role,
    public permission: Permission
  ) {}
}
