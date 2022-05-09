package io.github.M1lY.employeetaskmanagementtool.service;

import io.github.M1lY.employeetaskmanagementtool.dto.TaskRequest;
import io.github.M1lY.employeetaskmanagementtool.dto.TaskResponse;
import io.github.M1lY.employeetaskmanagementtool.exceptions.EmployeeManagementException;
import io.github.M1lY.employeetaskmanagementtool.model.Status;
import io.github.M1lY.employeetaskmanagementtool.model.Task;
import io.github.M1lY.employeetaskmanagementtool.model.User;
import io.github.M1lY.employeetaskmanagementtool.repository.TaskRepository;
import io.github.M1lY.employeetaskmanagementtool.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public void create(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTaskName(taskRequest.getTaskName());
        task.setDescription(taskRequest.getDescription());
        task.setCreatedDate(Instant.now());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(taskRequest.getDeadline(), formatter);
        Instant instant = zonedDateTime.toInstant();
        task.setDeadline(instant);
        task.setUser(userRepository.findByUsername(taskRequest.getUsername()).orElseThrow(
                () -> new EmployeeManagementException("User: "+taskRequest.getUsername()+ " not found")));
        task.setStatus(Status.TODO);

        taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> mapToTaskResponse(task, "ADMIN"))
                .collect(Collectors.toList());
    }
    public List<TaskResponse> getMyTasks(Authentication authentication) {
        return getUserTasks(authentication.getName(), "USER");
    }

    public List<TaskResponse> getUserTasks(String username, String role) {
        return taskRepository.findAllByUser(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new EmployeeManagementException("User: "+username+" not found"))
        )
                .stream()
                .map(task -> mapToTaskResponse(task, role))
                .collect(Collectors.toList());
    }

    public TaskResponse getTask(Long taskId) {
        return mapToTaskResponse(taskRepository.findById(taskId)
                .orElseThrow(() -> new EmployeeManagementException("Task with id: "+taskId+" not found")),
                "ADMIN"
        );
    }

    private TaskResponse mapToTaskResponse(Task task, String role){
        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .taskName(task.getTaskName())
                .description(task.getDescription())
                .createdDate(task.getCreatedDate())
                .deadline(task.getDeadline())
                .username(task.getUser().getUsername())
                .status(task.getStatus().name())
                .timeLeft(task.getStatus().name().equals("DONE") ? "DONE" : calculateTimeLeft(Instant.now(), task.getDeadline(), role))
                .build();
    }
    private String calculateTimeLeft(Instant now, Instant deadline, String role) {
        long days = ChronoUnit.DAYS.between(now, deadline);
        long hours = ChronoUnit.HOURS.between(now, deadline);
        long minutes = ChronoUnit.MINUTES.between(now, deadline);
        long seconds = ChronoUnit.SECONDS.between(now, deadline);
        if(deadline.isBefore(now)){
            if(role.equals("ADMIN")) return "Late for "+calculateTimeLeft(deadline, Instant.now(), role);
            else return "You are late for "+calculateTimeLeft(deadline, Instant.now(), role);

        }
        if(days > 0) return days+pluralValidate(days, " Day")+" "+(hours-days*24)+pluralValidate((hours-days*24), " Hour");
        if(hours > 0) return hours+pluralValidate(hours, " Hour")+" "+(minutes-hours*60)+pluralValidate((minutes-hours*60), " Minute");
        if(minutes > 0) return  minutes+pluralValidate(minutes, " Minute")+" "+(seconds-minutes*60)+pluralValidate((seconds-minutes*60), " Second");

        return seconds+" "+pluralValidate(seconds, "Second");
    }

    private String pluralValidate(Long val, String name){
        if(val==0 || val>1) name+="s";
        return name;
    }

    public void changeStatus(Long taskId, int statusId, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new EmployeeManagementException("User: "+authentication.getName()+" not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EmployeeManagementException("Task with id: "+taskId+" not found"));
        int currentStatus = task.getStatus().ordinal();

        if((user.getRole().equals("USER") && task.getUser()==user && Math.abs(currentStatus-statusId)==1 && statusId<=2 && statusId>=0)
//                || (user.getRole().equals("ADMIN") &&
//                        (currentStatus==Status.TO_REVIEW.ordinal() && (statusId==Status.DONE.ordinal() || statusId==Status.TODO.ordinal()))
//                )
        ) {
            Status status = Status.stream().collect(Collectors.toList()).get(statusId);
            task.setStatus(status);
            taskRepository.save(task);
            return;
        }
        throw new EmployeeManagementException("Status change error");
    }
}
