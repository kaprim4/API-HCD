package com.example.HCOData.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "barecodes")
public class BareCodes {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"), @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    private Long id;
    private String type;
    private String data;

    @ManyToOne
    private Image image;
}
