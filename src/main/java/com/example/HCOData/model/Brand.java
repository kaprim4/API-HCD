package com.example.HCOData.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "brandUid", columnDefinition = "VARCHAR(255)")
    private String brandUid;
    private String brand;
    private String brandLine;
    private String indus;
    

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sous_category_id")
    private SousCategory sousCategory;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "brandOperation")
    private List<ProductOperation> productOperationList;
    
    @Column(name = "isValidate")
    private Boolean isValidate = Boolean.FALSE;
}
