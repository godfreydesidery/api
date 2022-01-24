/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = "packing_lists")
public class PackingList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String no;
	private String status;
	private LocalDate issueDate;
	private String comments;
	
	private Long createdBy;
	private Long createdAt;
	private Long approvedBy;
	private Long approvedAt;
	private Long postedBy;
	private Long postedAt;
	
	private double totalReturns = 0;
	private double totalDamages = 0;
	private double totalDiscounts = 0;
	private double totalExpenditures = 0;
	private double totalBank = 0;
	private double totalCash = 0;
	private double totalDeficit = 0;
	
	@ManyToOne(targetEntity = Customer.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "customer_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Customer customer;
	
	@ManyToOne(targetEntity = Employee.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "employee_id", nullable = false , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Employee employee;
	
	@OneToMany(targetEntity = PackingListDetail.class, mappedBy = "packingList", fetch = FetchType.EAGER, orphanRemoval = true)
    @Valid
    @JsonIgnoreProperties("packingList")
    private List<PackingListDetail> packingListDetails;
}
