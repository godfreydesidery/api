/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Privilege;
import com.orbix.api.domain.Role;
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
	List<User>getUsers(); //edit this to limit the number, for perfomance.
	void addPrivilegeToRole(String roleName, String privilegeName);
}
