package io.github.M1lY.employeetaskmanagementtool.repository;

import io.github.M1lY.employeetaskmanagementtool.model.Status;
import io.github.M1lY.employeetaskmanagementtool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByEnabledTrue();
    List<User> findAllByRoleAndEnabledTrue(String role);
}
