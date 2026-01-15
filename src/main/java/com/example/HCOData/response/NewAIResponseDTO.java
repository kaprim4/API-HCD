package com.example.HCOData.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewAIResponseDTO {

    private String name;
    private String date;
    private String heure;
    private String ville;
    private String address;
    private String montant_totale;
    private String montant_paye;
    private String montant_remise;
    private String produits;
    private String ean;
    private String quantity;
    private String processReport;

}
