package com.example.HCOData.request;

import com.example.HCOData.model.Image;
import com.example.HCOData.response.HCDResponseDTO;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseApiImageDTO {

    private HCDResponseDTO hcdResponseDTO;
    private Image image;
}
