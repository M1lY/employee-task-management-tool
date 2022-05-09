import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {SignupComponent} from "./auth/signup/signup.component";
import {LoginComponent} from "./auth/login/login.component";
import {UserComponent} from "./user/user.component";
import {AdminComponent} from "./admin/admin.component";
import {AuthGuard} from "./auth/auth.guard";

const routes: Routes = [
    {path: '', component: LoginComponent, canActivate: [AuthGuard]},
    {path: 'signup', component: SignupComponent, canActivate: [AuthGuard]},
    {path: 'login', component: LoginComponent, canActivate: [AuthGuard]},
    {path: 'user', component: UserComponent, canActivate: [AuthGuard], data: {roles: ['USER']}},
    {path: 'admin', component: AdminComponent, canActivate: [AuthGuard], data: {roles: ['ADMIN']}},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
