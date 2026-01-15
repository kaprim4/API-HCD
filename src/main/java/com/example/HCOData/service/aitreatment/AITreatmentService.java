package com.example.HCOData.service.aitreatment;

import com.example.HCOData.constant.MessageConstants;
import com.example.HCOData.constant.Patterns;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class AITreatmentService {

    @Autowired
    private AIService aIService;

    public String processAIResult(String imageToBase64, String apiSource, String apiBackup) {
        String date = Patterns.DATE_FORMAT.format(new Date());
        try {
            log.info("Sending request to primary IA server at {}, api source: {}", date, apiSource);
            String responseIa = aIService.sendDocumentToApi(imageToBase64, apiSource);

            if (!MessageConstants.IA_RESPONSE_KO.equals(responseIa)) {
                return responseIa;
            }
            log.info("Primary server failed, switching to backup server at {}, api backup: {}", date, apiBackup);
            return aIService.sendDocumentToApi(imageToBase64, apiBackup);

        } catch (Exception e) {
            log.error("Both servers failed to process the request: {}", e.getMessage(), e);
            throw new RuntimeException("AI processing failed", e);
        }
    }
}
