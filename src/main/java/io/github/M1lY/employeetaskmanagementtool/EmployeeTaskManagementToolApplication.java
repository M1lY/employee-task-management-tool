package io.github.M1lY.employeetaskmanagementtool;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@OpenAPIDefinition
public class EmployeeTaskManagementToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeTaskManagementToolApplication.class, args);
	}

}
