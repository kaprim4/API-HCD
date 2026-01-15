package com.example.HCOData.service.logger;

import com.example.HCOData.model.Operation;
import com.example.HCOData.model.PaLogger;
import com.example.HCOData.repository.PaLoggerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@Slf4j
public class LoggingInterceptor {


    @Autowired
    PaLoggerRepository paLoggerRepository;

    @Transactional
    public void loggStatusDocument(String imageName, Operation operation, String status, String imageUid, String iaResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonIaResponse = objectMapper.writeValueAsString(iaResponse);
            PaLogger paLogger = paLoggerRepository.existsByImageUid(imageUid)
                    ? paLoggerRepository.findByImageUid(imageUid)
                    : new PaLogger();
            paLogger.setDateReception(LocalDateTime.now());
            paLogger.setStatus(status);
            paLogger.setImageUid(imageUid);
            paLogger.setIaResponse(jsonIaResponse);
            paLogger.setOperation(operation.getUid());
            paLogger.setImageName(imageName);
            paLoggerRepository.save(paLogger);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("An unexpected error occurred while logging document {} ", e.getMessage());
        }

    }
}
