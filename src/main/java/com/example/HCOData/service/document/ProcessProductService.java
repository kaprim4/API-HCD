package com.example.HCOData.service.document;

import com.example.HCOData.response.NewAIResponseDTO;
import com.example.HCOData.model.Image;
import com.example.HCOData.model.NewBrand;
import com.example.HCOData.model.NewCategory;
import com.example.HCOData.model.ProductOperation;
import com.example.HCOData.model.ResponseAI;

import java.util.List;
import java.util.Set;

public interface ProcessProductService {

    void addProductToImage(Image imageToSave, NewAIResponseDTO newAIResponseDTO, List<NewBrand> brands, List<NewCategory> categories, Set<String> moderationFlagsList);

    ResponseAI mapProductResponse(ResponseAI responseAI, NewAIResponseDTO newAIResponseDTO);

}
