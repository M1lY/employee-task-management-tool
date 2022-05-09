package io.github.M1lY.employeetaskmanagementtool.repository;

import io.github.M1lY.employeetaskmanagementtool.model.JwtBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface JwtBlackListRepository extends JpaRepository<JwtBlackList, Long> {
    JwtBlackList findByTokenEquals(String token);
    int countAllByExpireLessThan(Instant date);
    void deleteAllByExpireLessThan(Instant date);
}
