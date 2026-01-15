package com.example.HCOData.model;

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
import javax.persistence.Table;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flags")
public class Flags {


    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"), @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    private Long id;

    private String name;

    private String imageUidFlag;

    private boolean isDuplcate;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "potentialDuplicateReceipts_id")
    private PotentialDuplicateReceipts potentialDuplicateReceipts;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="respnseAi_id")
    private ResponseAI responseAI;
    
    private String imageUid;
}
