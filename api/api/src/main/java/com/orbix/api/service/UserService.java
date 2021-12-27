/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Privilege;
import com.orbix.api.domain.Role;
import com.orbix.api.domain.Shortcut;
import com.orbix.api.domain.User;

/**
 * @author GODFREY
 *
 */
public interface UserService {
	User saveUser(User user);
	Role saveRole(Role role);
	Privilege savePrivilege(Privilege privilege);
	void addRoleToUser(String username, String roleName);
	User getUser(String username);
	User getUserById(Long id);
	boolean deleteUser(User user);
	List<User>getUsers(); //edit this to limit the number, for perfomance.
	void addPrivilegeToRole(String roleName, String privilegeName);
	void removePrivilegeFromRole(String roleName, String privilegeName);
	List<Role>getRoles(); // return all the roles
	Role getRole(String name);
	Role getRoleById(Long id);
	boolean deleteRole(Role role);
	List<String>getOperations();
	List<String>getObjects();
	List<String>getPrivileges(String roleName);
	boolean createShortcut(String username, String name, String link);
	List<Shortcut> loadShortcuts(String username);
	
	Long getUserId(HttpServletRequest request);
	
}
