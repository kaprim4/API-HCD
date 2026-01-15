package com.example.HCOData.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemRuleDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long quantityRequired;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String strategy;
    private List<RulesDTO.ORDTO> OR;
    private boolean isMatched;
    private List<String> itemsMatched;
}
