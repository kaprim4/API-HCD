package com.example.HCOData.enums;



public enum ToModerationFlagsEnum {
    BUYHOUR("BuyHour"),
    BUYDATE("BuyDate Check Validité"),
    BUYDATEFORMAT("BuyDate Format"),
    COHERENCEARTICLE("Coherence Article"),
    SHOP("Shop"),
    MONTANTTOTAL("Montant Total"),
    MONTANTPAYE("Montant Paye"),
    COHERENCELIBELLE("Coherence Libelle"),
    COHERENCEPRODUIT("Coherence Produit"),
    COHERENCEMONTANTTOTAL("Coherence Montant Total"),
    COHERENCLIBQMONTANT("Coherence Lib Q Montant"),
    HIGHFILESIMILARITY("High File Similarity"),
    SAMESHOPMOMENT("Same Shop Moment"),
    EXISTINGPRODUCTLIST("Existing Product List"),
    BORNAGEHORSCADRE("Bornage Hors Cadre"),
    PRODUITOFFRESANSCATMARQUE("Produit Offre Sans CAT Marque"),
    PRODUITHORSOFFRESANSCATMARQUE("Produit HorsOffre Sans CAT Marque"),
	UNITPRICEORQUANTITY("unite price");

    private final String valeur;

    private ToModerationFlagsEnum(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return this.valeur;
    }



}
