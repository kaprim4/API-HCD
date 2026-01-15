package com.example.HCOData.service.document;

import com.example.HCOData.response.CheckPotentialDuplicateReceiptsDTO;
import com.example.HCOData.model.Image;

public interface ProcessCheckDocumentDoubleService {

    CheckPotentialDuplicateReceiptsDTO checkIfTcExist(String idOperation, Image imageToSave);

}
