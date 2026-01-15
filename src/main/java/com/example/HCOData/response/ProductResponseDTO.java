package com.example.HCOData.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponseDTO {
    private String productName;
    private String productPrice;
    private String productQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productCategory;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productBrand;

    private String brandUid;

    private String categoryUid;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductResponseDTO productResponseDTO = (ProductResponseDTO) o;
        if(productBrand != null && productCategory !=null && brandUid!= null && categoryUid != null){
            return productName.equals(productResponseDTO.productName) &&
                    productPrice.equals(productResponseDTO.productPrice) &&
                    productQuantity.equals(productResponseDTO.productQuantity) &&
                    productCategory.equals(productResponseDTO.productCategory)&&
                    productBrand.equals(productResponseDTO.productBrand)&&
                    brandUid.equals(productResponseDTO.brandUid) &&
                    categoryUid.equals(productResponseDTO.categoryUid);
        }else if(productBrand != null){
            return productName.equals(productResponseDTO.productName) &&
                    productPrice.equals(productResponseDTO.productPrice) &&
                    productQuantity.equals(productResponseDTO.productQuantity)&&
                    productBrand.equals(productResponseDTO.productBrand);
        }else if(productCategory !=null){
            return productName.equals(productResponseDTO.productName) &&
                    productPrice.equals(productResponseDTO.productPrice) &&
                    productQuantity.equals(productResponseDTO.productQuantity)&&
                    productCategory.equals(productResponseDTO.productCategory);
        }else{
            return productName.equals(productResponseDTO.productName) &&
                    productPrice.equals(productResponseDTO.productPrice) &&
                    productQuantity.equals(productResponseDTO.productQuantity);
        }
    }
}
