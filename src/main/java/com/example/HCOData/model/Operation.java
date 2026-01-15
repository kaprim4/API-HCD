package com.example.HCOData.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "operation")
public class Operation {
    @Id
    private String uid;
    private String name;
    private String term;
    private String min;
    private String max;
    private BigDecimal  eligibleOnly;
    private BigDecimal totalAmount;
    private Long quantityRequired;
    private String strategy;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "operation")
    private List<SignOperation> signList;

    @ManyToMany(mappedBy = "operations")
    private List<Category> category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operations",fetch = FetchType.LAZY)
    private Set<Image> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operations")
    private Set<ResponseAI> responseAIS;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OperationTerms> operationTerms = new HashSet<>();

    @Column(name = "t_offre")
    private String offre;

    @Column(name = "t_offreMin")
    private String offreMin;

    @Column(name = "t_offreMax")
    private String offreMax;

    @Column(name = "checkDoublon")
    private boolean checkDoublon;

    @Column(name = "checkSign")
    private boolean checkSign;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operation", fetch = FetchType.LAZY)
    private List<ParametresPassageToModeration> parametresPassageToModeration;

    @Column(name = "passDirectToModeration")
    private boolean passDirectToModeration;

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, term); // Only use non-collection fields
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(uid, operation.uid);
    }
}

