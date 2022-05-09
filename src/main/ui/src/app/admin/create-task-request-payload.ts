export class CreateTaskRequestPayload {
    taskName: string = "";
    deadline: Date = new Date();
    description: string = "";
    username: string = "";
}
