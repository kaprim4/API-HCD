package com.example.HCOData.service.businessrules;


import com.example.HCOData.model.Image;
import com.example.HCOData.model.Operation;
import com.example.HCOData.request.ResponseApiImageDTO;
import com.example.HCOData.response.HCDResponseDTO;

import java.io.File;

public interface BusinessRulesService {

     Image moderationProcessor(String responseApi, String uid, ResponseApiImageDTO responseApiImageDTO, File imageFile, Operation operation, HCDResponseDTO hcdResponseDTO);

}
