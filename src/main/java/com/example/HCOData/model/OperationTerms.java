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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="operationTermes")
public class OperationTerms {
	
	    @Id
	    @GeneratedValue(generator = "uuid2")
	    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	    @Column(name = "motCleUID", columnDefinition = "VARCHAR(255)")
	    private String motCleUID;
	   
	    @Column(name = "moclefName")
	    private String name;
	    
	    @ManyToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "operation_uid")
	    private Operation operation;

	    

}
