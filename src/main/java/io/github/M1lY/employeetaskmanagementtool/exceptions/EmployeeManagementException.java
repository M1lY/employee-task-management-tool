package io.github.M1lY.employeetaskmanagementtool.exceptions;

import org.springframework.mail.MailException;

public class EmployeeManagementException extends RuntimeException {
    public EmployeeManagementException(String s, Exception e) {
        super(s,e);
    }

    public EmployeeManagementException(String s) {
        super(s);
    }
}
