package com.example.HCOData.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "Parametres_passage_to_Moderation")
public class ParametresPassageToModeration {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
		
	@ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "operation_uid")
    private Operation operation;
	
	@Column(name = "buyHour")
    private boolean buyHour;

    @Column(name = "buyDate")
    private boolean buyDate;

    @Column(name = "buyDateFormat")
    private boolean buyDateFormat;

    @Column(name = "coherenceArticle")
    private boolean coherenceArticle;

    @Column(name = "shop")
    private boolean shop;

    @Column(name = "montantTotal")
    private boolean montantTotal;

    @Column(name = "montantPaye")
    private boolean montantPaye;

    @Column(name = "coherenceLibelle")
    private boolean coherenceLibelle;

    @Column(name = "coherenceProduit")
    private boolean coherenceProduit;

    @Column(name = "coherenceMontantTotal")
    private boolean coherenceMontantTotal;

    @Column(name = "coherenceLibQMontant")
    private boolean coherenceLibQMontant;

    @Column(name = "highFileSimilarity")
    private boolean highFileSimilarity;

    @Column(name = "sameShopMoment")
    private boolean sameShopMoment;

    @Column(name = "existingProductList")
    private boolean existingProductList;

    @Column(name = "bornageHorsCadre")
    private boolean bornageHorsCadre;

    @Column(name = "produitOffreSansCatMarque")
    private boolean produitOffreSansCatMarque;

    @Column(name = "produitHorsOffreSansCatMarque")
    private boolean produitHorsOffreSansCatMarque;

    @Column(name = "unitPriceOrQuantity")
    private boolean unitPriceOrQuantity;
    
    

}
