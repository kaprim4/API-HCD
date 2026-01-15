package com.example.HCOData.model;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productResponseAi")
@EntityListeners(Auditable.class)
public class ProduitResponseAI {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"), @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    private Long id;
    private String libelle;
    private String prix;
    private String quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    private ResponseAI responseAI;

    @Override
    public String toString() {
        return "ProduitResponseAI{id=" + id + "}";
    }
}
