export interface LoginResponsePayload {
    authenticationToken: string;
    expiresDate: Date;
    username: string;
    role: string;
}
