import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TaskModel} from "./task-model";
import {CreateTaskRequestPayload} from "../admin/create-task-request-payload";

@Injectable({
    providedIn: 'root'
})
export class TaskService {

    constructor(private http: HttpClient) {
    }

    getMyTasks(): Observable<Array<TaskModel>> {
        return this.http.get<Array<TaskModel>>('http://localhost:8080/api/tasks');
    }

    changeStatus(taskId: number, statusId: number) {
        this.http.post('http://localhost:8080/api/tasks/status/'+taskId+'/'+statusId, '', {responseType: "text"}).subscribe();
    }

    createTask(createTaskRequestPayload: CreateTaskRequestPayload): Observable<any>{
        return this.http.post('http://localhost:8080/api/tasks/create', createTaskRequestPayload, {responseType: "text"});
    }

    getTasksByUsername(username: string): Observable<Array<TaskModel>> {
        return this.http.get<Array<TaskModel>>('http://localhost:8080/api/tasks/user/'+username);
    }
}
