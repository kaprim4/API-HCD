package com.example.HCOData.service.document;

import com.example.HCOData.model.Operation;
import com.example.HCOData.request.ResponseApiImageDTO;
import com.example.HCOData.response.FirstTreatmentResponseDTO;
import com.example.HCOData.response.HCDResponseDTO;
import com.example.HCOData.response.ImageResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public interface ProcessDocumentService {

    HCDResponseDTO  startProcess(String clientRequest, MultipartFile image) throws IOException;

    FirstTreatmentResponseDTO processImage(File image, Operation operation, int numberOfPage, String typeImage, String uid, String responseIA);

    ImageResponseDTO findImageByUid(String uid);

}
