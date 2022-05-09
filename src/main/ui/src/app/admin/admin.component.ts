import {Component, OnInit, ViewChild} from '@angular/core';
import {UserModel} from "../shared/user-model";
import {UserService} from "../shared/user.service";
import {faEye, faPlusSquare} from '@fortawesome/free-solid-svg-icons';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CreateTaskRequestPayload} from "./create-task-request-payload";
import * as moment from "moment";
import {ThemePalette} from "@angular/material/core";
import {TaskService} from "../shared/task.service";
import {TaskModel} from "../shared/task-model";

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
    faEye = faEye;
    faPlusSquare = faPlusSquare;
    public users: Array<UserModel> = [];
    public user!: UserModel;
    addTaskForm!: FormGroup;
    createTaskRequestPayload!: CreateTaskRequestPayload;
    modal: any;

    public tasks: Array<TaskModel> = [];
    public todoTasks: Array<TaskModel> = [];
    public inprogressTasks: Array<TaskModel> = [];
    public doneTasks: Array<TaskModel> = [];

    @ViewChild('picker') picker: any;

    public dateControl = new FormControl(new Date());
    public date = new Date();
    public minDate: moment.Moment;
    public color: ThemePalette = 'primary';


    constructor(private userService: UserService, private taskService: TaskService) {
        this.minDate=moment();

        this.userService.getAllUsers().subscribe(user => {
            this.users=user;
        })
        this.createTaskRequestPayload={
            taskName: '',
            deadline: new Date(),
            description: '',
            username: ''
        }
    }

    ngOnInit(): void {
        this.addTaskForm=new FormGroup({
            taskName: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
        })
    }


    public onOpenModal(user: UserModel | null, mode: string){
        const container = document.getElementById('mainContainer');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-bs-toggle', 'modal');
        if(mode === 'add'){
            if(user) this.user=user;
            button.setAttribute('data-bs-target', '#addTaskModal');
            document.getElementById('dateInput')!.addEventListener('click', this.onClick.bind(this));
        }else if(mode === 'preview'){
            this.tasks=[];
            this.todoTasks=[];
            this.inprogressTasks=[];
            this.doneTasks=[];
            if(user) this.user=user;
            console.log(user);
            button.setAttribute('data-bs-target', '#userPreviewModal');
            this.taskService.getTasksByUsername(this.user.username).subscribe(task => {
                this.tasks=task;
                for (let t of task) {
                    if(t.status=="TODO") this.todoTasks.push(t);
                    else if(t.status=="IN_PROGRESS") this.inprogressTasks.push(t);
                    else this.doneTasks.push(t);
                }
            })
            console.log(this.tasks);
        }
        container!.appendChild(button);
        button.click();
    }

    onClick(event: any){
        this.picker.open();
    }

    c(event: any){
        console.log(event);
    }

    addTask() {
        this.createTaskRequestPayload.taskName=this.addTaskForm.get('taskName')?.value;
        this.createTaskRequestPayload.description=this.addTaskForm.get('description')?.value;
        this.createTaskRequestPayload.deadline=this.dateControl.value.toISOString();
        this.createTaskRequestPayload.username=this.user.username;


        if(!this.createTaskRequestPayload.taskName
            && !this.createTaskRequestPayload.description
            && !this.createTaskRequestPayload.deadline){
            return
        }

        this.taskService.createTask(this.createTaskRequestPayload).subscribe(data => {
            document.getElementById('closeModalButton')!.click();
            console.log(data);
        }, error => {
            console.log(error);
        })
    }
}
