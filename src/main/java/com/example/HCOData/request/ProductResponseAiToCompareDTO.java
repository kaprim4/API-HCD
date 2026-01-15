package com.example.HCOData.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseAiToCompareDTO extends Object {

    private String libel;
    private String price;
    private String quantity;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductResponseAiToCompareDTO imageResponseToCompare = (ProductResponseAiToCompareDTO) o;
        return
                libel.equals(imageResponseToCompare.getLibel()) &&
                        price.equals(imageResponseToCompare.getPrice()) &&
                        quantity.equals(imageResponseToCompare.getQuantity());
    }

    @Override
    public int hashCode() {

        return Objects.hash(libel, price, quantity);
    }

}
