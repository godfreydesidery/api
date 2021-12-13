/**
 * 
 */
package com.orbix.api.api;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.orbix.api.security.Operation;
import com.orbix.api.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserResource {
	
	private final UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<List<User>>getUsers(){
		return ResponseEntity.ok().body(userService.getUsers());
	}
	
	@GetMapping("/users/get_user")
	public ResponseEntity<User> getUser(
			@RequestParam(name = "username") String username){
		return ResponseEntity.ok().body(userService.getUser(username));
	}
	
	
	@PostMapping("/users/create")
	public ResponseEntity<User>createUser(
			@RequestBody User user){
		if(user.getUsername().equals("superuser")) {
			throw new InvalidOperationException("Username not available");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/create").toUriString());
		return ResponseEntity.created(uri).body(userService.saveUser(user));
	}
	
	@PutMapping("/users/update")
	public ResponseEntity<User>updateUser(
			@RequestBody User user){
		User userToUpdate = userService.getUserById(user.getId());
		if(!userToUpdate.getUsername().equalsIgnoreCase(user.getUsername())) {
			throw new InvalidOperationException("Updating the SUPER USER profile is not allowed");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/update").toUriString());
		return ResponseEntity.created(uri).body(userService.saveUser(user));
	}
	
	@DeleteMapping("/users/delete")
	public ResponseEntity<Boolean> deleteUser(
			@RequestParam(name = "id") Long id){
		User user = userService.getUserById(id);
		if(user.getUsername().equalsIgnoreCase("superuser")) {
			throw new InvalidOperationException("Deleting the SUPER USER profile is not allowed");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/delete").toUriString());
		return ResponseEntity.created(uri).body(userService.deleteUser(user));
	}
	
	@GetMapping("/roles/get_role")
	public ResponseEntity<Role> getRole(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(userService.getRole(name));
	}
	
	@PostMapping("/roles/create")
	public ResponseEntity<Role>saveRole(
			@RequestBody Role role){
		if(role.getName().equalsIgnoreCase("SUPER USER")) {
			throw new InvalidOperationException("Role name not available");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}
	
	@PutMapping("/roles/update")
	public ResponseEntity<Role>updateRole(
			@RequestBody Role role){
		Role roleToUpdate = userService.getRoleById(role.getId());		
		if(roleToUpdate.getName().equalsIgnoreCase("SUPER USER")) {
			throw new InvalidOperationException("Editing the SUPER USER role is not allowed");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles/update").toUriString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}
	
	@DeleteMapping("/roles/delete")
	public ResponseEntity<Boolean> deleteRole(
			@RequestParam(name = "id") Long id){
		Role role = userService.getRoleById(id);
		if(role.getName().equalsIgnoreCase("SUPER USER")) {
			throw new InvalidOperationException("Deleting the SUPER USER role is not allowed");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles/delete").toUriString());
		return ResponseEntity.created(uri).body(userService.deleteRole(role));
	}

	@PostMapping("/roles/addtouser")
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
	
	@PostMapping("/privileges/addtorole")
	public boolean addPrivilegeToRole(
			@RequestBody AccessModel form){	
		for(ObjectModel model : form.getPrivileges()) {
			String object = model.getObject();
			for(String operation : model.getOperations()) {
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
	
	@GetMapping("/shortcuts/load")
	public ResponseEntity<List<Shortcut>> loadShortcuts(
			@RequestParam String username){		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/shortcuts/create").toUriString());
		return ResponseEntity.created(uri).body(userService.loadShortcuts(username));
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
