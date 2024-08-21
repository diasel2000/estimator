export class RegisterRequest {
  constructor(
    public username: string,
    public email: string,
    public password: string,
    public googleId?: string  // optional field
  ) {}
}
