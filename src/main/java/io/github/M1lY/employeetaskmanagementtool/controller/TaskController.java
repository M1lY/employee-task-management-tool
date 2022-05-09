package io.github.M1lY.employeetaskmanagementtool.controller;

import io.github.M1lY.employeetaskmanagementtool.dto.TaskRequest;
import io.github.M1lY.employeetaskmanagementtool.dto.TaskResponse;
import io.github.M1lY.employeetaskmanagementtool.exceptions.EmployeeManagementException;
import io.github.M1lY.employeetaskmanagementtool.model.User;
import io.github.M1lY.employeetaskmanagementtool.repository.UserRepository;
import io.github.M1lY.employeetaskmanagementtool.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<List<TaskResponse>> getMyTasks(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getMyTasks(authentication));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody TaskRequest taskCreateRequest){
        taskService.create(taskCreateRequest);
        return new ResponseEntity<>("Task created", HttpStatus.CREATED);
    }

    @GetMapping("/id/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long taskId){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getTask(taskId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponse>> getAllTasks(){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasks());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<TaskResponse>> getUserTasks(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getUserTasks(username, "ADMIN"));
    }

    @PostMapping("/status/{taskId}/{statusId}")
    public ResponseEntity<String> changeStatus(@PathVariable Long taskId, @PathVariable int statusId, Authentication authentication){
        taskService.changeStatus(taskId, statusId, authentication);
        return new ResponseEntity<>("Task status updated",HttpStatus.OK);
    }
}
