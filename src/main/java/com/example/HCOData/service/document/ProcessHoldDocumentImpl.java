package com.example.HCOData.service.document;

import com.example.HCOData.constant.DocumentsPathConstants;
import com.example.HCOData.constant.UrlConstants;
import com.example.HCOData.enums.DocumentStatusEnum;
import com.example.HCOData.model.Image;
import com.example.HCOData.repository.ImageJPARepository;
import com.example.HCOData.repository.OperationTermsRepository;
import com.example.HCOData.request.ResponseApiImageDTO;
import com.example.HCOData.response.HCDResponseDTO;
import com.example.HCOData.service.aitreatment.AITreatmentService;
import com.example.HCOData.service.aitreatment.AIWorkflowService;
import com.example.HCOData.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ProcessHoldDocumentImpl implements ProcessHoldDocument {

    @Autowired
    private ImageJPARepository imageJPARepository;

    @Autowired
    OperationTermsRepository operationTermsRepository;

    @Autowired
    ProcessDocumentService processDocumentService;

    @Autowired
    AITreatmentService aiTreatmentService;

    @Autowired
    AIWorkflowService aIWorkflowService;




    @Override
    @Transactional
    public Map<String, String> restoreHoldDocuments() {
        Map<String, String> moderationDocuments = new HashMap<>();
        List<String> statusModerationList = Arrays.asList(DocumentStatusEnum.PROCESSING_NOT_EXISTS.getMessage(), DocumentStatusEnum.PROCESSING_IN_MODERATION.getValue());
        List<Image> documents = imageJPARepository.findByStatusModerationIn(statusModerationList);
        if (documents.isEmpty()) {
            moderationDocuments.put("No images found","No images found");
            return moderationDocuments;
        }
        documents.stream().filter(image -> DocumentStatusEnum.PROCESSING_NOT_EXISTS.getMessage().equals(image.getStatusModeration())).forEach(image -> {
            try {
                processDocumentDoesNotExist(image, moderationDocuments);
                moveDocumentAfterTreatment(image.getName().trim());

            } catch (IOException ex) {
                log.error("Error restoring document [{}] with UID [{}]: {}", image.getName(), image.getUid(), ex.getMessage(), ex);
                moderationDocuments.put(image.getName() + " with " + image.getUid(), "Error: " + ex.getMessage());
            }
        });
        documents.stream().filter(image -> DocumentStatusEnum.PROCESSING_IN_MODERATION.getValue().equals(image.getStatusModeration())).forEach(image -> processHoldDocument(image, moderationDocuments));
        return moderationDocuments;
    }

    private static boolean moveDocumentAfterTreatment(String nameDocument){
        Path sourcePath = Paths.get(DocumentsPathConstants.SECOND_DESTINATION+"/"+nameDocument);
        Path destinationPath = Paths.get(DocumentsPathConstants.NAS_DESTINATION+"/1/"+nameDocument);
        try{
            if(Files.exists(sourcePath)){
                Files.createDirectories(destinationPath.getParent());
                Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("File moved successfully");
                return true;
            }else{
                log.info("Source file does not exist {} ",sourcePath);
                return false;
            }

        }catch (IOException e){
            log.error("Error occurred while moving the file: {} " , e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void processDocumentDoesNotExist(Image image, Map<String, String> response) throws IOException {
        File document = getExistingFile(image.getName().trim());
        if (document != null && document.exists()) {
            Path destinationPath = Paths.get(DocumentsPathConstants.FIRST_DESTINATION, document.getName());
            Files.createDirectories(destinationPath.getParent());
            Files.copy(document.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            updateImage(image, "1", "PROCESSING");
            response.put(image.getName() + " fixed as non-existent document with " + image.getUid(), "DOCUMENT TREATED");
        } else {
            response.put(image.getName() + " with " + image.getUid(), "Document not found on the server");
        }
    }

    private void processHoldDocument(Image image, Map<String, String> response) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime updatedAt = image.getUpdatedAt();
        if (updatedAt != null && Duration.between(updatedAt, now).toMinutes() > 40) {
            updateImage(image, "1", "PROCESSING");
            response.put(image.getName() + " fixed hold document with " + image.getUid(), "DOCUMENT TREATED");
        } else {
            log.info("Document [{}] with UID [{}] not yet eligible for restoration", image.getName(), image.getUid());
        }
    }

    private void updateImage(Image image, String statusModeration, String status) {
        image.setLastModifiedBy(null);
        image.setStatusModeration(statusModeration);
        image.setStatus(status);
        imageJPARepository.save(image);
    }
    private static File getExistingFile(String fileName) {
        fileName = fileName.trim();
        File archiveDestination = new File(Paths.get(DocumentsPathConstants.SECOND_DESTINATION, fileName).toString());
        File nasDestination = new File(Paths.get(DocumentsPathConstants.NAS_DESTINATION + "/1", fileName).toString());
        return archiveDestination.exists() ? archiveDestination : nasDestination.exists() ? nasDestination : null;
    }

    @Override
    public Map<String, String> ReprocessedDocuments(MultipartFile file) {
        Map<String, String> resultTreatment = new HashMap<>();
        try {
            File fileConverter = ImageUtil.convertMultipartFileToFile(file);
            try (BufferedReader br = new BufferedReader(new FileReader(fileConverter))) {
                String line;
                while ((line = br.readLine()) != null) {
                    processEntries(line, resultTreatment);
                }
            }
            return resultTreatment;
        } catch (IOException e) {
            throw new RuntimeException("Error processing the file", e);
        }
    }

    @Override
    public void processPendingValidations() {
        List<Image> documents = imageJPARepository.findByStatusModeration(DocumentStatusEnum.WHITING_PROCESSING.getValue());
        documents.forEach(document->{
            log.info("start process for document : {}", document.getName());
            File documentFile = getExistingFile(document.getName());
            try {
                ResponseApiImageDTO responseApiImageDTO = new ResponseApiImageDTO();
                HCDResponseDTO hcdResponseDTO = new HCDResponseDTO();
                String responseIA = aiTreatmentService.processAIResult(ImageUtil.convertImageToBase64(documentFile), UrlConstants.API_AI_URL_SOURCE_SERVER, UrlConstants.API_AI_URL_BACKUP);
                try {
                    aIWorkflowService.processIfValid(responseIA, document.getUid(), responseApiImageDTO, documentFile, document.getOperations(), hcdResponseDTO);
                   log.info("Document {}, processed with uid : {} ", document.getName(), document.getUid());
                }catch (Exception e){
                    log.info(" Exception in Document {}  with uid : {} ", document.getName(), document.getUid());
                }

            } catch (IOException e) {
               log.error("Error processing AI result: {}", e.getMessage());
               e.printStackTrace();
            }

        });



    }

    private void processEntries(String line, Map<String, String> resultTreatment) {
        String[] entries = line.split(";");
        for (String entry : entries) {
            entry = entry.trim();
            Image image = imageJPARepository.findByUid(entry);
            if (image == null) {
                resultTreatment.put(entry, "UID not found");
                continue;
            }
            handleImageProcessing(entry, image, resultTreatment);
        }
    }

    private void handleImageProcessing(String entry, Image image, Map<String, String> resultTreatment) {
        String uid = image.getUid().trim();
        File documentFile = getExistingFile(image.getName());

        if (documentFile != null && documentFile.exists()) {
            try {
                image.setUid(uid + "_KO");
                image.setProcessReport("DOCUMENT_KO");
                image.setStatusModeration("DOCUMENT_KO");
                image.setStatus("DOCUMENT_KO");
                imageJPARepository.save(image);
                String responseIA = aiTreatmentService.processAIResult(ImageUtil.convertImageToBase64(documentFile), UrlConstants.API_AI_URL_SOURCE_SERVER, UrlConstants.API_AI_URL_BACKUP);
                HCDResponseDTO response = processDocumentService.processImage( documentFile, image.getOperations(), 1, ".jpg", entry, responseIA).getHcdResponseDTO();
                resultTreatment.put(entry, response.getMessage());
            } catch (Exception ex) {
                resultTreatment.put(entry, "Error processing document: " + ex.getMessage());
            }
        } else {
            resultTreatment.put(entry, "Image not found on server");
        }
    }

}
