package com.example.HCOData.response;

import com.example.HCOData.model.RechercheBrand;
import com.example.HCOData.model.RechercheCategory;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;


public enum RechercheBrandCategoryDTO {
;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class RechercheBrandDTO{
        private RechercheBrand rechercheBrand;
        private boolean isDuplicate = false;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class RechercheCategoryDTO{
        private RechercheCategory rechercheCategory;
        private boolean isDuplicate = false;
    }
}
