import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {Injectable} from "@angular/core";
import {AuthService} from "./auth/shared/auth.service";
import {catchError, EMPTY, raceWith, throwError} from "rxjs";
import {Router} from "@angular/router";
import {LocalStorageService} from "ngx-webstorage";

@Injectable({
    providedIn: 'root'
})
export class TokenInterceptor implements HttpInterceptor {
    constructor(private authServie: AuthService, private router: Router, private localStorage: LocalStorageService) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.authServie.getJwtToken();
        if (token) {
            req = TokenInterceptor.addToken(req, token);
            return next.handle(req).pipe(catchError(err => {
                if(err instanceof HttpErrorResponse && err.status===401){
                    this.localStorage.clear('authenticationToken');
                    this.localStorage.clear('expiresDate');
                    this.localStorage.clear('username');
                    this.localStorage.clear('role');
                    this.authServie.loggedIn.emit(false);
                    this.router.navigateByUrl('');
                    return EMPTY;
                }else{
                    return throwError(err);
                }
            }))
        }
        return next.handle(req);
    }

    private static addToken(req: HttpRequest<any>, token: any) {
        return req.clone({
            setHeaders: {
                Authorization: 'Bearer '+token
            }
        });
    }

}
