package com.example.HCOData.mappers;

import com.example.HCOData.model.ResponseAI;
import com.example.HCOData.response.NewAIResponseDTO;
import com.example.HCOData.service.document.ProcessProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AiMapper {

    @Autowired
    ProcessProductService processProductService;

    public  ResponseAI mapResponseAiToResponseAIObject(NewAIResponseDTO newAIResponseDTO) {
        ResponseAI responseAI = new ResponseAI();
        responseAI.setName(newAIResponseDTO.getName());
        responseAI.setAddress(newAIResponseDTO.getAddress());
        responseAI.setMontant_remise(newAIResponseDTO.getMontant_remise());
        responseAI.setProcessReport(newAIResponseDTO.getProcessReport());
        responseAI.setVille(newAIResponseDTO.getVille());
        responseAI.setDate(newAIResponseDTO.getDate());
        responseAI.setEan(newAIResponseDTO.getEan());
        responseAI.setHeure(newAIResponseDTO.getHeure());
        responseAI.setAddress(newAIResponseDTO.getAddress());
        responseAI.setEan(newAIResponseDTO.getEan());
        responseAI.setMontant_paye(newAIResponseDTO.getMontant_paye());
        responseAI.setMontant_remise(newAIResponseDTO.getMontant_remise());
        responseAI.setQuantity(newAIResponseDTO.getQuantity());
        responseAI.setMontant_totale(newAIResponseDTO.getMontant_totale());
        responseAI.setProcessReport(newAIResponseDTO.getProcessReport());
        return processProductService.mapProductResponse(responseAI, newAIResponseDTO);

    }
}
