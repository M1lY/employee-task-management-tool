package io.github.M1lY.employeetaskmanagementtool.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum Status {
    TODO, IN_PROGRESS, /*TO_REVIEW,*/ DONE;

    public static Stream<Status> stream(){
        return Stream.of(Status.values());
    }
}
