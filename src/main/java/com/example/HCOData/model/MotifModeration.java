package com.example.HCOData.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Data
@Table(name = "Motif_Moderation")
public class MotifModeration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motifName;

    private Long imageId;


    public MotifModeration(String motifName, Long imageId) {
        this.motifName = motifName;
        this.imageId = imageId;
    }


}
