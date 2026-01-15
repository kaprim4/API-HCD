package com.example.HCOData.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShopDTO {
    private String city;
    private String sign;
    private String subSign;
    private String phone;
    private String address;
    private String postalCode;
    private String geoLocation;
}
