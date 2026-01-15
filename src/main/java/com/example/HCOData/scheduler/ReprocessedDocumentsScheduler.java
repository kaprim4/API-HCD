package com.example.HCOData.scheduler;

import com.example.HCOData.service.document.ProcessHoldDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Map;

@Slf4j
@Component
public class ReprocessedDocumentsScheduler {

    private final ProcessHoldDocument processHoldDocument;

    public ReprocessedDocumentsScheduler(ProcessHoldDocument processHoldDocument) {
        this.processHoldDocument = processHoldDocument;
    }

   // @Scheduled(fixedRate = 2400000)
    public void executeRestoreHoldDocuments() {
        try {
            Map<String, String> result = processHoldDocument.restoreHoldDocuments();
            log.info("Restore Hold Documents Executed : {} ",result);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    //@Scheduled(fixedRate = 180000)
    public void processPendingValidations() {
        try {
            processHoldDocument.processPendingValidations();
            log.info("Document pending is processed : {} ");
        }catch (Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
