package io.github.M1lY.employeetaskmanagementtool.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.Instant;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long taskId;
    @NotBlank(message = "Task Name cannot be empty or null")
    private String taskName;
    @Nullable
    @Lob
    private String description;
    private Instant createdDate;
    private Instant deadline;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    private Status status;
}
