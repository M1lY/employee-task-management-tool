package io.github.M1lY.employeetaskmanagementtool.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Lob
    @Column(length = 500)
    private String token;
    private Instant expire;
}
