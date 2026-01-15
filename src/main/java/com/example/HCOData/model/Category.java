package com.example.HCOData.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {

	    @Id
	    @GeneratedValue(generator = "uuid2")
	    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	    @Column(name = "categoryUid", columnDefinition = "VARCHAR(255)")
	    private String categoryUid;

	    private String category;

	    private String rayon;

	    private String categoryName;

	    @Column(name = "isValidate")
	    private Boolean isValidate;

	    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	    @JsonIgnore
	    private List<SousCategory> souscategorys;

	    @ManyToMany
	    @JoinTable(
	            name = "operation_category",
	            joinColumns = @JoinColumn(name = "category_uid"),
	            inverseJoinColumns = @JoinColumn(name = "operation_uid"))
	    private List<Operation> operations;
}
