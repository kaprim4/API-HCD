package com.example.HCOData.mappers;

import com.example.HCOData.response.ImageResponseDTO;
import com.example.HCOData.model.Image;

public interface ImageMapperInterface {

    ImageResponseDTO mapImageToImageDTO(Image image, String idOperation);
    
    ImageResponseDTO mapPostImageToImageDTO(Image image, String idOperation);


}
