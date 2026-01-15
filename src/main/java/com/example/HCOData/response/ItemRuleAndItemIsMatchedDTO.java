package com.example.HCOData.response;

import lombok.*;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemRuleAndItemIsMatchedDTO {

    private ItemRuleDTO itemRuleDTO;
    private boolean itemIsMatchedPrice;
    private boolean quantityMatched;
    private boolean itemMatchedBoolean;
    private BigDecimal totalPriceItemMatched;
}
