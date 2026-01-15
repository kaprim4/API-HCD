package com.example.HCOData.response;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageResponseToCompareDTO {

    private String dominantLanguage;
    private String status;
    private String processReport;
    private String imageUrl;
    private String name;
    private String endpointUid;
    private String campaignUid;
    private String buyHour;
    private String buyDate;
    private String articleCount;
    private String total;
    private String totalMax;
    private String locale;
    private String city;
    private String sign;
    private String subSign;
    private String phone;
    private String address;
    private String postalCode;
    private String geoLocation;
    private List<ProductResponseDTO> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageResponseToCompareDTO imageResponseToCompareDTO = (ImageResponseToCompareDTO) o;
        return
                status.equals(imageResponseToCompareDTO.status) &&
                processReport.equals(imageResponseToCompareDTO.processReport) &&
                name.equals(imageResponseToCompareDTO.name) &&
                buyHour.equals(imageResponseToCompareDTO.buyHour) &&
                buyDate.equals(imageResponseToCompareDTO.buyDate) &&
                articleCount.equals(imageResponseToCompareDTO.articleCount) &&
                total.equals(imageResponseToCompareDTO.total) &&
                totalMax.equals(imageResponseToCompareDTO.totalMax) &&
                city.equals(imageResponseToCompareDTO.city) &&
                sign.equals(imageResponseToCompareDTO.sign) &&
                products.equals(imageResponseToCompareDTO.products);
    }
}




