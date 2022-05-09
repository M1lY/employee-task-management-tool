import {Component, Input, OnInit} from '@angular/core';
import {TaskModel} from "../task-model";

@Component({
    selector: 'app-task-tile',
    templateUrl: './task-tile.component.html',
    styleUrls: ['./task-tile.component.css']
})
export class TaskTileComponent implements OnInit {
    @Input() tasks!: TaskModel[];
    @Input() flag: boolean = true;
    @Input() done: boolean = false;

    constructor() {
    }

    ngOnInit(): void {
    }

}
