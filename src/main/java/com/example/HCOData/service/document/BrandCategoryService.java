package com.example.HCOData.service.document;

import com.example.HCOData.model.NewBrand;
import com.example.HCOData.model.NewCategory;
import com.example.HCOData.model.Product;

import java.util.List;
import java.util.Set;

public interface BrandCategoryService {
    void setProductCategoryAndBrand(Product product, Set<String> moderationFlagsList);
    List<NewBrand> getAllBrand();
    List<NewCategory> getAllCategory();

}
