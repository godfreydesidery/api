package com.orbix.api;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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
import com.orbix.api.security.Object_;
import com.orbix.api.security.Operation;
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
			
			userService.saveRole(new Role(null, "SUPER USER", null));
						
			userService.saveUser(new User(null, "superuser", "superuser", null, null, "1111", "Godfrey", "Desidery", "Shirima", "Godfrey Shirima", true, new ArrayList<>()));
			
			userService.addRoleToUser("superuser", "SUPER USER");
						
			Field[] objectFields = Object_.class.getDeclaredFields();
			Field[] operationFields = Operation.class.getDeclaredFields();
			for(int i = 0; i < objectFields.length; i++) {
				for(int j = 0; j < operationFields.length; j++) {
					Privilege privilege = new Privilege();
					privilege.setName(objectFields[i].getName()+"-"+operationFields[j].getName());
					try {
						userService.savePrivilege(privilege);
					}catch(Exception e) {
						System.out.println("Could not save privilege");
					}
				}
			}
		};
	}
	
	@Bean
   public Docket erpApi() {
      return new Docket(DocumentationType.SWAGGER_2).select()
         .apis(RequestHandlerSelectors.basePackage("com.orbix.api")).build();
   }
}
