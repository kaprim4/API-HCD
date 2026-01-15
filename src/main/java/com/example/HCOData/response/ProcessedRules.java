package com.example.HCOData.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessedRules {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String uid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    private String term;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RulesDTO.dateRule dateRule;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RulesDTO.PriceRule priceRule;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RulesDTO.SignRule signRule;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ItemRuleDTO itemRuleDTO;
    @JsonIgnore
    private boolean isItemsPriceMatched;
    @JsonIgnore
    private boolean isQuantityItemMatched;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean allRulesMatched;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalPriceOfMatchedItems;
}
