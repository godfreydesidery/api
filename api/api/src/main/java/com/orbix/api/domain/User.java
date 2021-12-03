/**
 * 
 */
package com.orbix.api.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author GODFREY
 *
 */
@Entity
@Data  
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "users")
public class User { 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String username;
	@Column
	private String password;
	@Column(unique=true, nullable=true)
	private String accessToken;
	@Column(unique=true, nullable=true)
	private String refreshToken;	
	@NotBlank
	@Column(unique = true)
	private String rollNo;
	@NotBlank
	private String firstName;
	private String secondName;
	@NotBlank
	private String lastName;
	@NotBlank
	@Column(unique = true)
	private String alias;
	private int active = 1;
	
//    private boolean accountVerified;
//    private int failedLoginAttempts;
//    private boolean loginDisabled;

	
	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Role> roles = new ArrayList<>();	
}
