import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {observable, Observable, takeUntil} from 'rxjs';
import {AuthService} from "./shared/auth.service";

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    constructor(private authService: AuthService, private router: Router) {
    }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {



        const isAuthenticated = this.authService.isLoggedIn();
        if(state.url=="/login" || state.url=='/signup'){
            if(!isAuthenticated){
                return true;
            }else{
                this.router.navigateByUrl('');
                return false;
            }
        }

        if(!isAuthenticated){
            this.router.navigateByUrl('login');
            return false;
        }

        const roleUser = this.authService.getRole();
        if(state.url==="/"){
            if(roleUser=='ADMIN'){
                this.router.navigateByUrl('admin');
            }else{
                this.router.navigateByUrl('user');
            }
            return false;
        }

        const roleRequired = route.data["roles"] as Array<string>;

        if(roleRequired.includes(roleUser)) {
            return true;
        }else{
            this.router.navigateByUrl('');
            return false;
        }

    }

}
