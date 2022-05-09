import {Component, OnInit} from '@angular/core';
import {TaskModel} from "../shared/task-model";
import {TaskService} from "../shared/task.service";
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";

@Component({
    selector: 'app-user',
    templateUrl: './user.component.html',
    styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
    public tasks: Array<TaskModel> = [];
    public todoTasks: Array<TaskModel> = [];
    public inprogressTasks: Array<TaskModel> = [];
    public doneTasks: Array<TaskModel> = [];

    constructor(private taskService: TaskService) {
        this.taskService.getMyTasks().subscribe(task => {
            this.tasks=task;
            for (let t of task) {
                if(t.status=="TODO") this.todoTasks.push(t);
                else if(t.status=="IN_PROGRESS") this.inprogressTasks.push(t);
                else this.doneTasks.push(t);
            }
        })
    }

    ngOnInit(): void {
    }

    dropped($event: CdkDragDrop<Array<TaskModel>, any>) {
        if($event.previousContainer === $event.container){
            moveItemInArray(
                $event.container.data,
                $event.previousIndex,
                $event.currentIndex
            );
        }else{
            transferArrayItem(
                $event.previousContainer.data,
                $event.container.data,
                $event.previousIndex,
                $event.currentIndex
            )
            let taskId = $event.container.data[$event.currentIndex].taskId;
            let statusId = $event.container.id.slice($event.container.id.length-1,$event.container.id.length);

            console.log($event.container);

            this.taskService.changeStatus(taskId, Number(statusId));
        }
    }
}
