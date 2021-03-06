import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginRequestPayload} from "./login-request.payload";
import {AuthService} from "../shared/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {throwError} from "rxjs";
import {LocalStorageService} from "ngx-webstorage";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
    loginRequestPayload!: LoginRequestPayload;
    loginForm!: FormGroup;
    registerSuccessMessage: string = '';
    isError!: boolean;

    constructor(private authService: AuthService, private activatedRoute: ActivatedRoute, private router: Router,
                private toastr: ToastrService, private localstorage: LocalStorageService) {
        this.loginRequestPayload={
            username: '',
            password: ''
        };
    }

    ngOnInit(): void {
        this.loginForm=new FormGroup({
            username: new FormControl('', Validators.required),
            password: new FormControl('', Validators.required)
        });

        this.activatedRoute.queryParams.subscribe(params => {
           if(params['registered'] !== undefined && params['registered'] === 'true'){
               this.toastr.success('Signup Successful');
               this.registerSuccessMessage = 'Please check your inbox for activate your account';
           }
        });
    }

    login(){
        this.loginRequestPayload.username=this.loginForm.get('username')?.value;
        this.loginRequestPayload.password=this.loginForm.get('password')?.value;

        if(!this.loginRequestPayload.username || !this.loginRequestPayload.password){
            this.isError=true;
            return;
        }

        this.authService.login(this.loginRequestPayload).subscribe(data => {
            this.isError=false;
            if(this.localstorage.retrieve('role')=='USER'){
                this.router.navigateByUrl('user')
            }else{
                this.router.navigateByUrl('admin')
            }
            // this.router.navigateByUrl('');
            this.toastr.success('Login Successful');
        }, error => {
            this.isError=true;
            // throwError(error);
        });
    }

}
