package com.example.HCOData.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_operation")
public class ProductOperation {
	
    @Id
    private Long id;
    private String ean;
    private String term;
    private Long quantityRequired;
    private String strategy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_id")
    @JsonIgnore
    private Brand brandOperation;
    
}
