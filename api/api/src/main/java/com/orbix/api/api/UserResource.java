/**
 * 
 */
package com.orbix.api.api;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbix.api.domain.Privilege;
import com.orbix.api.domain.Role;
import com.orbix.api.domain.Shortcut;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.PrivilegeRepository;
import com.orbix.api.repositories.RoleRepository;
import com.orbix.api.security.Object_;
import com.orbix.api.security.Operation;
import com.orbix.api.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserResource {
	
	private final RoleRepository roleRepository;
	private final PrivilegeRepository privilegeRepository;
	private final UserService userService;
	
	@GetMapping("/users")
	@PreAuthorize("hasAnyAuthority('USER-READ')")
	public ResponseEntity<List<User>>getUsers(){
		return ResponseEntity.ok().body(userService.getUsers());
	}
	
	@GetMapping("/users/get_user")
	@PreAuthorize("hasAnyAuthority('USER-READ')")
	public ResponseEntity<User> getUser(
			@RequestParam(name = "username") String username){
		return ResponseEntity.ok().body(userService.getUser(username));
	}
	
	@GetMapping("/users/load_user")
	public ResponseEntity<UserModel> loadUser(
			@RequestParam(name = "username") String username){
		User user = userService.getUser(username);
		if(!user.isActive()) {
			throw new InvalidOperationException("Could not load user, user not active");
		}
		UserModel userModel = new UserModel();
		userModel.setId(user.getId());
		userModel.setAlias(user.getAlias());
		return ResponseEntity.ok().body(userModel);
	}
	
	
	@PostMapping("/users/create")
	@PreAuthorize("hasAnyAuthority('USER-CREATE')")
	public ResponseEntity<User>createUser(
			@RequestBody User user){
		if(user.getUsername().equals("root")) {
			throw new InvalidOperationException("Username not available");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/create").toUriString());
		return ResponseEntity.created(uri).body(userService.saveUser(user));
	}
		
	@PutMapping("/users/update")
	@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<User>updateUser(
			@RequestBody User user, 
			HttpServletRequest request){
		String authorizationHeader = request.getHeader("Authorization");
		String username = getUsernameFromAuthorizationHeader(authorizationHeader);		
		User userToUpdate = userService.getUserById(user.getId());
		if(userToUpdate.getUsername().equals(username)) {
			if(userToUpdate.isActive() == true && user.isActive() == false) {
				throw new InvalidOperationException("A user can not deactivate itself");
			}else if(userToUpdate.isActive() == false && user.isActive() == true) {
				throw new InvalidOperationException("A user can not activate itself");
			}
		}
		if(userToUpdate.getUsername().equals("root")) {
			if(userToUpdate.isActive() == true && user.isActive() == true) {
				throw new InvalidOperationException("The root user can only be activated or deactivated");
			}else if(userToUpdate.isActive() == false && user.isActive() == false) {
				throw new InvalidOperationException("The root user can only be activated or deactivated");
			}
			boolean active = user.isActive();
			userToUpdate.setActive(active);
			user = userToUpdate;
			user.setPassword("");//ensure that the root password is not changed in the operation
		}
		
		
		if(!userToUpdate.getUsername().equalsIgnoreCase(user.getUsername())) {
			throw new InvalidOperationException("Updating the ROOT profile is not allowed");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/update").toUriString());
		return ResponseEntity.created(uri).body(userService.saveUser(user));
	}
	
	@DeleteMapping("/users/delete")
	@PreAuthorize("hasAnyAuthority('USER-DELETE')")
	public ResponseEntity<Boolean> deleteUser(
			@RequestParam(name = "id") Long id){
		User user = userService.getUserById(id);
		if(user.getUsername().equalsIgnoreCase("root")) {
			throw new InvalidOperationException("Deleting the ROOT profile is not allowed");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/delete").toUriString());
		return ResponseEntity.created(uri).body(userService.deleteUser(user));
	}
	
	@GetMapping("/roles/get_role")
	@PreAuthorize("hasAnyAuthority('ROLE-READ')")
	public ResponseEntity<Role> getRole(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(userService.getRole(name));
	}
	
	@PostMapping("/roles/create")
	@PreAuthorize("hasAnyAuthority('ROLE-CREATE')")
	public ResponseEntity<Role>saveRole(
			@RequestBody Role role){
		if(role.getName().equalsIgnoreCase("ROOT")) {
			throw new InvalidOperationException("Role name not available");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}
	
	@PutMapping("/roles/update")
	@PreAuthorize("hasAnyAuthority('ROLE-UPDATE')")
	public ResponseEntity<Role>updateRole(
			@RequestBody Role role){
		Role roleToUpdate = userService.getRoleById(role.getId());		
		if(roleToUpdate.getName().equalsIgnoreCase("ROOT")) {
			throw new InvalidOperationException("Editing the ROOT role is not allowed");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles/update").toUriString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}
	
	@DeleteMapping("/roles/delete")
	@PreAuthorize("hasAnyAuthority('ROLE-DELETE')")
	public ResponseEntity<Boolean> deleteRole(
			@RequestParam(name = "id") Long id){
		Role role = userService.getRoleById(id);
		if(role.getName().equalsIgnoreCase("ROOT")) {
			throw new InvalidOperationException("Deleting the ROOT role is not allowed");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles/delete").toUriString());
		return ResponseEntity.created(uri).body(userService.deleteRole(role));
	}

	@PostMapping("/roles/addtouser")
	@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<Role>addRoleToUser(
			@RequestBody RoleToUserForm form){
		userService.addRoleToUser(form.getUsername(), form.getRoleName());
		return ResponseEntity.ok().build();
	}
		
	
	@GetMapping("/token/refresh")
	public void refreshToken(
			HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException{
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String refresh_token = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username =decodedJWT.getSubject();
				User user = userService.getUser(username);
				String access_token = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+8*60*60*1000))
						.withIssuer(request.getRequestURI().toString())
						.withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);
						
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			}catch(Exception exception) {
				response.setHeader("error", exception.getMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
				//response.sendError(FORBIDDEN.value);
				Map<String, String> error = new HashMap<>();
				error.put("error_message", exception.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
				
			}
						
		}else {
			 throw new RuntimeException("Refresh token is missing");
		}
	}
	
	@GetMapping("/roles")
	public ResponseEntity<List<Role>>getRoles(){
		return ResponseEntity.ok().body(userService.getRoles());
	}
	
	@GetMapping("/operations")
	public ResponseEntity<List<String>>getOperations(){
		return ResponseEntity.ok().body(userService.getOperations());
	}
	
	@GetMapping("/objects")
	public ResponseEntity<List<String>>getObjects(){
		return ResponseEntity.ok().body(userService.getObjects());
	}
	
	@GetMapping("/privileges")
	public ResponseEntity<List<PrivilegeModel>>getPrivileges(
			@RequestParam(name = "role") String roleName){
		List<String> privileges = userService.getPrivileges(roleName);
		List<PrivilegeModel> modelList = new ArrayList<PrivilegeModel>();
		for(String privilege : privileges) {
			PrivilegeModel model = new PrivilegeModel();
			int posH = privilege.indexOf("-");
			model.setObject(privilege.substring(0, posH));
			model.setOperation(privilege.substring(posH + 1));
			modelList.add(model);
		}		
		return ResponseEntity.ok().body(modelList);
	}
	
	@GetMapping("/check_op_valid")
	public boolean checkOpValid(
			@RequestParam(name = "object") String obj,
			@RequestParam(name = "operation") String op){
		String privilege = obj + "-" + op;
		Optional<Privilege> p = privilegeRepository.findByName(privilege);
		if(p.isPresent()) {
			return true;
		}else {
			return false;
		}
	}
	
	@PostMapping("/privileges/addtorole")
	@PreAuthorize("hasAnyAuthority('ROLE-UPDATE')")
	public boolean addPrivilegeToRole(
			@RequestBody AccessModel form){	
		Role role = roleRepository.findByName(form.getRole());
		Collection<Privilege> privileges = new ArrayList<>(role.getPrivileges());
		
		for(Privilege p : privileges) {
			userService.removePrivilegeFromRole(role.getName(), p.getName());
		}
		int i = 0;		
		for(ObjectModel model : form.getPrivileges()) {
			if(i > form.getPrivileges().length - 1) {
				break;
			}
			i++;
			String object = model.getObject();
			int j = 0;
			for(String operation : model.getOperations()) {
				if(j > model.getOperations().length - 1) {
					break;
				}
				j++;
				userService.addPrivilegeToRole(form.getRole(), object+"-"+operation);
			}
		}
		return true;
	}
	
	@PostMapping("/shortcuts/create")	
	public ResponseEntity<Boolean> createShortcut(
			@RequestParam String username,
			@RequestParam String name,
			@RequestParam String link){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/shortcuts/create").toUriString());
		return ResponseEntity.created(uri).body(userService.createShortcut(username, name, link));
	}
	
	@PostMapping("/shortcuts/remove")	
	public ResponseEntity<Boolean> removeShortcut(
			@RequestParam String username,
			@RequestParam String name){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/shortcuts/remove").toUriString());		
		return ResponseEntity.created(uri).body(userService.removeShortcut(username, name));
	}
	
	@GetMapping("/shortcuts/load")
	public ResponseEntity<List<Shortcut>> loadShortcuts(
			@RequestParam String username){		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/shortcuts/create").toUriString());
		return ResponseEntity.created(uri).body(userService.loadShortcuts(username));
	}
	
	private String getUsernameFromAuthorizationHeader(String authorizationHeader) {
		String token = authorizationHeader.substring("Bearer ".length());
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(token);
		String username = decodedJWT.getSubject();
		return username;
	}
	
	@GetMapping("/load_privilege_model")
	public List<AuthorityModel> loadAuthModels() {
		List<AuthorityModel> models = new ArrayList<>();
		
		List<String> objects = new ArrayList<String>();
		for(Field field : Object_.class.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if(Modifier.isStatic(modifiers)) {
				String value = "";
				try {
					value = Object_.class.getDeclaredField(field.getName()).get(null).toString();
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!value.equals("")) {
					if(value.contains("-")) {
						objects.add(value.substring(0, value.indexOf("-")));
					}else {
						objects.add(value);
					}
					
				}			
			}
		}
		
		List<String> operations = new ArrayList<String>();
		for(Field field : Operation.class.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if(Modifier.isStatic(modifiers)) {
				String value = "";
				try {
					value = Operation.class.getDeclaredField(field.getName()).get(null).toString();
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!value.equals("")) {
					operations.add(value);
				}			
			}
		}
		
		for(String object : objects) {
			AuthorityModel m = new AuthorityModel();
			m.setObject(object);
			List<String> s = new ArrayList<>();
			for(String operation : operations) {				
				String privilege = object+"-"+operation;
				Optional<Privilege> p =privilegeRepository.findByName(privilege);
				if(p.isPresent()) {
					s.add(operation);
				}
			}
			m.setOperations(s);
			models.add(m);
		}		
		return models;
	}
}
 
@Data
@AllArgsConstructor
@NoArgsConstructor
class RoleToUserForm{
	private String username;
	private String roleName;	
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class PrivilegeModel{
	private String object;
	private String operation;	
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AccessModel{
	String role;
	ObjectModel[] privileges; 
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ObjectModel{
	String object;
	String[] operations;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserModel{
	Long id;
	String alias;	
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AuthorityModel{
	String object;
	List<String> operations;
}


