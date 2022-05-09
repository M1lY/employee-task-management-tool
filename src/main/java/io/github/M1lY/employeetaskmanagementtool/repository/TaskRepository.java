package io.github.M1lY.employeetaskmanagementtool.repository;

import io.github.M1lY.employeetaskmanagementtool.model.Task;
import io.github.M1lY.employeetaskmanagementtool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);
}
