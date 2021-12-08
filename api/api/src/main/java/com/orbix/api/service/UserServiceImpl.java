/**
 * 
 */
package com.orbix.api.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orbix.api.domain.Privilege;
import com.orbix.api.domain.Role;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.PrivilegeRepository;
import com.orbix.api.repositories.RoleRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.security.Object_;
import com.orbix.api.security.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PrivilegeRepository privilegeRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("User not found in the database");
			throw new NotFoundException("User not found in database");
		}else {
			log.info("User found in database: {}", username);
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			role.getPrivileges().forEach(privilege -> {
				authorities.add(new SimpleGrantedAuthority(privilege.getName()));
			});
		});
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
	
	@Override
	public User saveUser(User user) {
		log.info("Saving new user to the database");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public Role saveRole(Role role) {
		log.info("Saving new role to the database");
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		User user = userRepository.findByUsername(username);
		Role role = roleRepository.findByName(rolename);
		try {
			user.getRoles().add(role);	
		}catch(Exception e) {
			log.info(e.getMessage());
		}					
	}

	@Override
	public User getUser(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> getUsers() {
		log.info("Fetching all users");
		return userRepository.findAll();
	}

	@Override
	public Privilege savePrivilege(Privilege privilege) {
		log.info("Saving new privilege to the database");
		return privilegeRepository.save(privilege);
	}

	@Override
	public void addPrivilegeToRole(String roleName, String privilegeName) {
		Role role = roleRepository.findByName(roleName);
		Privilege privilege = privilegeRepository.findByName(privilegeName);
		for(int i = 0; i < 100; i++) {
			System.out.println(i);
		}
		System.out.println(roleName);
		System.out.println(role.getName());
		System.out.println(privilege.getName());
		
		try {
			role.getPrivileges().add(privilege);
			System.out.println(role.getName());
			System.out.println(privilege.getName());
		}catch(Exception e) {
			log.info(e.getMessage());
		}			
	}

	@Override
	public List<Role> getRoles() {
		log.info("Fetching all roles");
		return roleRepository.findAll();
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public boolean deleteUser(User user) {
		userRepository.delete(user);
		return true;
	}

	@Override
	public Role getRole(String name) {
		return roleRepository.findByName(name);
	}

	@Override
	public List<String> getOperations() {
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
		return operations;
	}

	@Override
	public List<String> getObjects() {
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
					objects.add(value);
				}			
			}
		}
		List<String> subList = objects.subList(0, objects.size());
		Collections.sort(subList);
		return subList;
		//return objects;
	}

	@Override
	public List<String> getPrivileges(String roleName) {
		Collection<Privilege> privileges = roleRepository.findByName(roleName).getPrivileges();
		List<String> privilegesList = new ArrayList<String>();
		for(Privilege privilege : privileges) {
			privilegesList.add(privilege.getName());
		}
		return privilegesList;
	}

	@Override
	public Role getRoleById(Long id) {
		return roleRepository.findById(id).get();
	}

		
}
