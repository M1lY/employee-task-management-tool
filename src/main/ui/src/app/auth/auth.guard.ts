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

        console.log(state.url);


        const isAuthenticated = this.authService.isLoggedIn();
        if(state.url=="/login" || state.url=='/signup'){
            if(!isAuthenticated){
                console.log("aa");
                return true;
            }else{
                console.log("ab");
                this.router.navigateByUrl('');
                return false;
            }
        }

        if(!isAuthenticated){
            console.log("b");
            this.router.navigateByUrl('login');
            return false;
            // return this.router.createUrlTree(['login']);

        }

        const roleUser = this.authService.getRole();
        if(state.url==="/"){
            if(roleUser=='ADMIN'){
                console.log("c");
                this.router.navigateByUrl('admin');
            }else{
                console.log("d");
                this.router.navigateByUrl('user');
            }
            return false;
        }

        const roleRequired = route.data["roles"] as Array<string>;

        if(roleRequired.includes(roleUser)) {
            console.log("e");
            return true;
        }else{
            console.log("f");
            this.router.navigateByUrl('');
            return false;
        }

    }

}
