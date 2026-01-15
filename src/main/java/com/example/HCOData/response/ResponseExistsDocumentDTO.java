package com.example.HCOData.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseExistsDocumentDTO {

    private String sign;
    private String city;
    private String date;
    private String hour;
    private String operation;
    private String uid;
    private String address;
    private BigDecimal total;
    private BigDecimal totalMax;
    private Long articleCount;
    private String md5;





}
