package com.example.HCOData.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "responseAI")
@EntityListeners(Auditable.class)
public class ResponseAI {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"), @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    private Long id;
    private String uid;
    private String name;
    private String ville;
    private String address;
    private String date;
    private String heure;
    private String montant_totale;
    private String montant_paye;
    private String montant_remise;
    private String quantity;
    private String ean;
    private String processReport;
    private LocalDateTime dateReception;

    @OneToMany(cascade = CascadeType.ALL, mappedBy ="responseAI")
    private List<ProduitResponseAI> produitResponseAIList;


    @ManyToOne( cascade = CascadeType.MERGE)
    @ToString.Exclude
    private Operation operations;

    @OneToMany(mappedBy = "responseAI", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Flags> flags;

    @Override
    public String toString() {
        return "ResponseAI{id=" + id + "}";
    }


}
