package com.example.HCOData.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="operation_brand_category")
public class OperationBrandCategory {
	
	    @Id
	    @GeneratedValue(generator = "uuid2")
	    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	    @Column(name = "uid", columnDefinition = "VARCHAR(255)")
	    private String uid;
	   
	    @Column(name = "t_brand")
	    private String brand;
	    
	    @Column(name = "t_category")
	    private String category;
	    
	    @ManyToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "operation_uid")
	    private Operation operation;
}
