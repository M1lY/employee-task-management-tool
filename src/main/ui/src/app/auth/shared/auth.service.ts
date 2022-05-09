import {EventEmitter, Injectable, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SignupRequestPayload} from "../signup/signup-request.payload";
import {map, Observable} from "rxjs";
import {LoginRequestPayload} from "../login/login-request.payload";
import {LoginResponsePayload} from "../login/login-response.payload";
import {LocalStorageService} from "ngx-webstorage";
import {Router} from "@angular/router";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    @Output() loggedIn: EventEmitter<boolean> = new EventEmitter<boolean>();
    @Output() username: EventEmitter<string> = new EventEmitter<string>();


    constructor(private httpClient: HttpClient, private localStorage: LocalStorageService, private router: Router) {
    }

    signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
        return this.httpClient.post('http://localhost:8080/api/auth/signup', signupRequestPayload,
            {responseType: 'text'});
    }

    login(loginRequestPayload: LoginRequestPayload): Observable<boolean>{
        return this.httpClient.post<LoginResponsePayload>('http://localhost:8080/api/auth/login', loginRequestPayload)
            .pipe(map(data => {
                this.localStorage.store('authenticationToken', data.authenticationToken);
                this.localStorage.store('expiresDate', data.expiresDate);
                this.localStorage.store('username', data.username);
                this.localStorage.store('role', data.role);

                this.loggedIn.emit(true);
                this.username.emit(data.username);
                return true;
            }))
    }

    getJwtToken() {
        return this.localStorage.retrieve('authenticationToken');
    }

    isLoggedIn(): boolean{
        return this.getJwtToken()!=null;
    }

    getRole(): string{
        return this.localStorage.retrieve('role');
    }

    getUsername(): string{
        return this.localStorage.retrieve('username');
    }


    logout() {
        this.httpClient.post('http://localhost:8080/api/auth/logout', '', {responseType: "text"}).subscribe(data => {
            this.localStorage.clear('authenticationToken');
            this.localStorage.clear('expiresDate');
            this.localStorage.clear('username');
            this.localStorage.clear('role');

            this.loggedIn.emit(false);
            this.username.emit("");
            this.router.navigateByUrl('');
        }, error => {
        })
    }
}
