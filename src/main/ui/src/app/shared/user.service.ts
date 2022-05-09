import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserModel} from "./user-model";

@Injectable({
    providedIn: 'root'
})
export class UserService {
    constructor(private http: HttpClient) {
    }

    getAllUsers(): Observable<Array<UserModel>>{
        return this.http.get<Array<UserModel>>('http://localhost:8080/api/users/all');
    }
}
