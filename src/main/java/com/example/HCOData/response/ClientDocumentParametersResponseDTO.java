package com.example.HCOData.response;

import com.example.HCOData.model.Operation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ClientDocumentParametersResponseDTO {

    private File image;

    private int numberOfPages;

    private String imageType;

    private Operation operation;

    private String imageUid;


}
