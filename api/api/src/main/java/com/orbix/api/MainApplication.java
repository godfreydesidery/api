package com.orbix.api;

import java.util.ArrayList;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.hibernate.type.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.orbix.api.domain.Day;
import com.orbix.api.domain.Privilege;
import com.orbix.api.domain.Role;
import com.orbix.api.domain.User;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.UserService;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication()
@ComponentScan(basePackages={"com.orbix.api"})
@EnableJpaAuditing
@EnableAutoConfiguration
@EnableSwagger2
//@EnableWebMvc
public class MainApplication {
protected ConfigurableApplicationContext springContext;

    DayRepository dayRepository;

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		
	}
	
	public static void main(String[] args) throws Throwable {
		SpringApplication.run(MainApplication.class, args);
		
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner run(UserService userService, DayService dayService) {
		return args -> {
			dayService.saveDay(new Day());
			
			userService.saveRole(new Role(null, "USER", null));
			userService.saveRole(new Role(null, "GENERAL MANAGER", null));
			userService.saveRole(new Role(null, "ADMIN", null));
			userService.saveRole(new Role(null, "SUPER ADMIN", null));
			userService.saveRole(new Role(null, "ASSISTANT MANAGER", null));
			userService.saveRole(new Role(null, "SENIOR ACCOUNTANT", null));
			userService.saveRole(new Role(null, "ACCOUNTANT", null));
			userService.saveRole(new Role(null, "PROCUREMENT", null));
			userService.saveRole(new Role(null, "SALES MANAGER", null));
			userService.saveRole(new Role(null, "ASSISTANT SALES MANAGER", null));
			userService.saveRole(new Role(null, "PUBLIC RELATIONS MANAGER", null));
			
			userService.savePrivilege(new Privilege(null, "CREATE"));
			userService.savePrivilege(new Privilege(null, "READ"));
			userService.savePrivilege(new Privilege(null, "UPDATE"));
			userService.savePrivilege(new Privilege(null, "DELETE"));
			
			userService.saveUser(new User(null, "username1", "password", null, null, "1111", "Godfrey", "Desidery", "Shirima", "Godfrey Shirima", true, new ArrayList<>()));
			userService.saveUser(new User(null, "username2", "password", null, null, "2222", "Mary", "Augustino", "Michael", "Mary Michael", true, new ArrayList<>()));
			userService.saveUser(new User(null, "username3", "password", null, null, "3333", "Clemence", "Desidery", "Shirima", "Clemence Shirima", true, new ArrayList<>()));
			userService.saveUser(new User(null, "username4", "password", null, null, "4444", "Grasiana", "Desidery", "Shirima", "Grasiana Shirima", true, new ArrayList<>()));
			
			userService.addRoleToUser("username", "USER");
			userService.addRoleToUser("username", "GENERAL MANAGER");
			userService.addRoleToUser("username", "SUPER ADMIN");
			userService.addRoleToUser("username1", "USER");
			userService.addRoleToUser("username1", "ASSISTANT MANAGER");
			userService.addRoleToUser("username2", "USER");
			userService.addRoleToUser("username3", "USER");
			
			userService.addPrivilegeToRole("USER", "CREATE");
			userService.addPrivilegeToRole("USER", "READ");
			userService.addPrivilegeToRole("USER", "UPDATE");
			userService.addPrivilegeToRole("USER", "DELETE");
			userService.addPrivilegeToRole("GENERAL MANAGER", "CREATE");
			userService.addPrivilegeToRole("GENERAL MANAGER", "READ");
			userService.addPrivilegeToRole("GENERAL MANAGER", "UPDATE");
			userService.addPrivilegeToRole("GENERAL MANAGER", "DELETE");
			userService.addPrivilegeToRole("GENERAL MANAGER", "CREATE");
			userService.addPrivilegeToRole("GENERAL MANAGER", "READ");
			userService.addPrivilegeToRole("GENERAL MANAGER", "UPDATE");
			userService.addPrivilegeToRole("GENERAL MANAGER", "DELETE");
			userService.addPrivilegeToRole("SUPER ADMIN", "CREATE");
			userService.addPrivilegeToRole("SUPER ADMIN", "READ");
			userService.addPrivilegeToRole("SUPER ADMIN", "UPDATE");
			userService.addPrivilegeToRole("SUPER ADMIN", "DELETE");
		};
	}
	
	@Bean
   public Docket erpApi() {
      return new Docket(DocumentationType.SWAGGER_2).select()
         .apis(RequestHandlerSelectors.basePackage("com.orbix.api")).build();
   }
	
	 

	    
}
