/**
 * 
 */
package com.orbix.api.domain;

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
@Table(name = "receipts")
public class Receipt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String no;
	private double cash;
	private double voucher;
	private double deposit;
	private double loyalty;
	private double crCard;
	private double cheque;
	private double cap;
	private double invoice;
	private double crNote;
	private double mobile;
	private double other;
	
	private Long createdBy;
	private Long createdAt;
	
	@ManyToOne(targetEntity = Till.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "till_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Till till;
	
	@OneToMany(targetEntity = ReceiptDetail.class, mappedBy = "receipt", fetch = FetchType.EAGER, orphanRemoval = true)
    @Valid
    @JsonIgnoreProperties("receipt")
    private List<ReceiptDetail> receiptDetails;
}
