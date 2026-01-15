package com.example.HCOData.response;

import com.example.HCOData.model.Brand;
import com.example.HCOData.model.Category;
import com.example.HCOData.model.ProductOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public enum CheckProductExistResponse {
    ;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class checkProductExistInProductOperationLabel {
        private boolean exist = false;
        private ProductOperation productOperation;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class checkIfProductExistInBrandLabel{
        private boolean exist = false;
        private Brand brand;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class checkIfProductExistInCategoryLabel{
        private boolean exist = false;
        private Category category;
    }

}
