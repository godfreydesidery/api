/**
 * 
 */
package com.orbix.api.domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GODFREY
 *
 */
@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "tills")
public class Till {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String tillNo;
	@Column(unique = true)
	private String computerName;
	private boolean active = true;
}
