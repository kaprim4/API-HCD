package com.example.HCOData.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

public enum RulesDTO {
    ;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class dateRule{
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String min;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String max;
        private boolean isMatched = false;

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class PriceRule{
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BigDecimal eligibleOnly;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BigDecimal totalAmount;
        private boolean isMatched;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SignRule{
        private List<String> signList;
        private boolean isMatched;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ORDTO{
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Long quantityRequired;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String strategy;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String term;
        private boolean isMatched;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Long quantityMatched;
        private List<String> itemsMatched;
    }
}


