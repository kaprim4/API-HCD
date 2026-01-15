package com.example.HCOData.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="sous_categorys")
public class SousCategory {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "sous_category_uid", columnDefinition = "VARCHAR(255)")
    private String sousCategoryUid;

    private String libelle;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_uid")
    private Category category;

    
    @JsonIgnore
    @OneToMany(mappedBy = "sousCategory",cascade = CascadeType.ALL)
    private List<Brand> brandList;





}
