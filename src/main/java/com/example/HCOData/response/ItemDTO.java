package com.example.HCOData.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDTO {

    private String id;
    private String rawLabel;
    private String shortLabel;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long quantity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal unitPrice;
    private String packageUnity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal mass;
    private List<String> flags;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal price;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long bundle;
    private ProductDTO product;
}
