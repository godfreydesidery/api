package com.orbix.api;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;

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
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.orbix.api.domain.Day;
import com.orbix.api.domain.Privilege;
import com.orbix.api.domain.Role;
import com.orbix.api.domain.User;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.security.Object_;
import com.orbix.api.security.Operation;
import com.orbix.api.service.DayService;
import com.orbix.api.service.MaterialServiceImpl;
import com.orbix.api.service.UserService;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MainApplication {
protected ConfigurableApplicationContext springContext;

    DayRepository dayRepository;
    
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Don't do this in production, use a proper list  of allowed origins
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    
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
			if(!dayService.hasData()) {
				/**
				 * Creating the first day
				 */
				log.info("Creating the first day "+(new Day()).toString());
				dayService.saveDay(new Day());
			}
			try {
				userService.saveRole(new Role(null, "ROOT", null));
			}catch(Exception e) {}	
			try {
				userService.saveUser(new User(null, "root", "r00tpA55w0Rd", null, null, "root@NAN", "Root", "Root", "Root", "Root @ Root", true, null,new ArrayList<>()));
				userService.saveUser(new User(null, "grasiana", "r00tpA55w0Rd", null, null, "1234", "	grasiana", "Root", "Shirima", "Grasiana Shirima", true, null,new ArrayList<>()));
			}catch(Exception e) {}		
			try {
				userService.addRoleToUser("root", "ROOT");
			}catch(Exception e) {}		
			
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
			try {
				userService.addPrivilegeToRole("ROOT", "USER-CREATE");
				userService.addPrivilegeToRole("ROOT", "USER-READ");
				userService.addPrivilegeToRole("ROOT", "USER-UPDATE");
				userService.addPrivilegeToRole("ROOT", "USER-DELETE");
				userService.addPrivilegeToRole("ROOT", "USER-ACTIVATE");
				
				userService.addPrivilegeToRole("ROOT", "ROLE-CREATE");
				userService.addPrivilegeToRole("ROOT", "ROLE-READ");
				userService.addPrivilegeToRole("ROOT", "ROLE-UPDATE");
				userService.addPrivilegeToRole("ROOT", "ROLE-DELETE");
				userService.addPrivilegeToRole("ROOT", "ROLE-ACTIVATE");
			}catch(Exception e) {}			
		};
	}
	
	@Bean
   public Docket erpApi() {
      return new Docket(DocumentationType.SWAGGER_2).select()
         .apis(RequestHandlerSelectors.basePackage("com.orbix.api")).build();
   }
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
	    CommonsMultipartResolver multipart = new CommonsMultipartResolver();
	    multipart.setMaxUploadSize(3 * 1024 * 1024);
	    return multipart;
	}

	@Bean
	@Order(0)
	public MultipartFilter multipartFilter() {
	    MultipartFilter multipartFilter = new MultipartFilter();
	    multipartFilter.setMultipartResolverBeanName("multipartResolver");
	    return multipartFilter;
	}
}
