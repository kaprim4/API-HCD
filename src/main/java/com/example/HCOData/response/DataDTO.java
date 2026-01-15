package com.example.HCOData.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataDTO {
    private String buyHour;
    private String buyDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long articleCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal total;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalMax;
    private List<String> payment;
    private String locale;
    private ShopDTO shop;
    private List<ItemDTO> items;

}
