package com.example.HCOData.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"), @org.hibernate.annotations.Parameter(name = "increment_size", value = "1") })
    private Long id;
    private String rawLabel;
    private String shortLabel;
    private Long quantity;
    private BigDecimal unitPrice;
    private Long bundle;
    private BigDecimal price;
    private String packageUnity;
    private BigDecimal mass;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String category;
    private String categoryUid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String brand;


    private String brandUid;
    private String brandLine;
    private String gtin;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Flags> flags;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "image_id",referencedColumnName = "id")
    @JsonIgnore
    private Image image;

    public void removeParent() {
        this.image.removeChild(this);
        this.image = null;
    }
}
