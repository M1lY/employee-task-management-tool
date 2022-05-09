package io.github.M1lY.employeetaskmanagementtool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {
    private Long taskId;
    private String taskName;
    private String description;
    private Instant createdDate;
    private Instant deadline;
    private String username;
    private String status;
    private String timeLeft;
}
