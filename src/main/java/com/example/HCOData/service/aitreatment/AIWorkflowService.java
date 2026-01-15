package com.example.HCOData.service.aitreatment;

import com.example.HCOData.constant.MessageConstants;
import com.example.HCOData.enums.DocumentStatusEnum;
import com.example.HCOData.enums.OperationEnums;
import com.example.HCOData.exception.InternalServerErrorException;
import com.example.HCOData.service.logger.LoggingInterceptor;
import com.example.HCOData.model.Image;
import com.example.HCOData.model.Operation;
import com.example.HCOData.repository.ImageJPARepository;
import com.example.HCOData.request.ClientRequestDTO;
import com.example.HCOData.request.ResponseApiImageDTO;
import com.example.HCOData.response.FirstTreatmentResponseDTO;
import com.example.HCOData.response.HCDResponseDTO;
import com.example.HCOData.response.NewAIResponseDTO;
import com.example.HCOData.service.businessrules.BusinessRulesService;
import com.example.HCOData.utils.ImageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;


@Service
@Slf4j
public class AIWorkflowService {


    private final LoggingInterceptor loggingInterceptor;

    private final ImageJPARepository imageJPARepository;

    private final BusinessRulesService businessRulesService;


    public AIWorkflowService(LoggingInterceptor loggingInterceptor, ImageJPARepository imageJPARepository, BusinessRulesService businessRulesService) {
        this.loggingInterceptor = loggingInterceptor;
        this.imageJPARepository = imageJPARepository;
        this.businessRulesService = businessRulesService;
    }

    @Transactional
    public FirstTreatmentResponseDTO executeAITreatment(File image, Operation operation, String uid, String responseApiIA) throws IOException {

        log.info(" Process started ");
        log.info("Uid is : {}, and document is {} , and operation is ", uid, image.getName(), operation);

        FirstTreatmentResponseDTO firstTreatmentResponseDTO = new FirstTreatmentResponseDTO();
        HCDResponseDTO hcdResponseDTO = new HCDResponseDTO();
        Gson gson = new Gson();
        firstTreatmentResponseDTO.setFile(image);
        loggingInterceptor.loggStatusDocument(image.getName(), operation, MessageConstants.DOCUMENT_MESSAGE_OK, uid, responseApiIA);

        if (!MessageConstants.IA_RESPONSE_KO.equals(responseApiIA)) {
            NewAIResponseDTO newAIResponseDTO = gson.fromJson(responseApiIA, NewAIResponseDTO.class);
            if (newAIResponseDTO.getProcessReport() != null) {
                log.info("document error message : {} ", MessageConstants.ERROR_NOT_PA_MSG, "with uid", uid);
                hcdResponseDTO.setMessage(MessageConstants.ERROR_NOT_PA_MSG);
            } else {
                hcdResponseDTO.setMessage(MessageConstants.DOCUMENT_MESSAGE_OK);
                hcdResponseDTO.setUid(uid);
            }
            firstTreatmentResponseDTO.setHcdResponseDTO(hcdResponseDTO);
            firstTreatmentResponseDTO.setResponseAi(responseApiIA);
            loggingInterceptor.loggStatusDocument(image.getName(), operation, MessageConstants.DOCUMENT_MESSAGE_OK, uid, responseApiIA);
            return firstTreatmentResponseDTO;

        } else {
            if(OperationEnums.OPERATION_PG.getValue().equals(operation.getUid())){
                log.info("operation is : {}, with uid : {} so white for start job for send image to moderation ", operation.getUid(), uid);
                hcdResponseDTO.setMessage(MessageConstants.DOCUMENT_MESSAGE_OK);
            }
            else{
                log.info("document is {}, with uid {}   ", MessageConstants.DOCUMENT_MESSAGE_KO, uid);
                hcdResponseDTO.setMessage(MessageConstants.DOCUMENT_MESSAGE_KO);
            }
            hcdResponseDTO.setUid(uid);
            firstTreatmentResponseDTO.setHcdResponseDTO(hcdResponseDTO);
            firstTreatmentResponseDTO.setResponseAi(MessageConstants.IA_RESPONSE_KO);
            loggingInterceptor.loggStatusDocument(image.getName(), operation, MessageConstants.DOCUMENT_MESSAGE_KO, uid, responseApiIA);
            return firstTreatmentResponseDTO;
        }
    }

    @Transactional
    public void validateAITreatment(File image, Operation operation, FirstTreatmentResponseDTO firstTreatmentResponseDTO, int numberOfPage, String typeImage, String uid) throws IOException {

        try {
            ResponseApiImageDTO responseApiImageDTO = new ResponseApiImageDTO();
            HCDResponseDTO hcdResponseDTO = new HCDResponseDTO();
            buildClientRequest(firstTreatmentResponseDTO);
            boolean documentStatus = MessageConstants.ERROR_NOT_PA_MSG.equals(firstTreatmentResponseDTO.getHcdResponseDTO().getMessage());
            if (!MessageConstants.IA_RESPONSE_KO.equals(firstTreatmentResponseDTO.getResponseAi())) {
                if (!documentStatus) {
                    prepareImageForSaving(firstTreatmentResponseDTO, image, operation, numberOfPage, typeImage);
                       processIfValid(firstTreatmentResponseDTO.getResponseAi(),
                            firstTreatmentResponseDTO.getHcdResponseDTO().getUid(),
                            responseApiImageDTO,
                            firstTreatmentResponseDTO.getFile(),
                               operation,
                            hcdResponseDTO);
                    loggingInterceptor.loggStatusDocument(image.getName(), operation, MessageConstants.DOCUMENT_MESSAGE_OK, uid, firstTreatmentResponseDTO.getResponseAi());
                } else {
                    loggingInterceptor.loggStatusDocument(image.getName(), operation, MessageConstants.ERROR_NOT_PA_MSG, uid, firstTreatmentResponseDTO.getResponseAi());
                    log.info("Response AI is KO, document not pa stop processing ...");
                }

            } else {
                prepareImageForSaving(firstTreatmentResponseDTO, image, operation, numberOfPage, typeImage);
            }

        } catch (Exception e) {
            loggingInterceptor.loggStatusDocument(image.getName(), operation, "Error while processing document", uid, firstTreatmentResponseDTO.getResponseAi());
            e.printStackTrace();
            log.error("An unexpected error occurred while in second process the image : {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while in second process the image");
        }
    }

    private Image prepareImageForSaving(FirstTreatmentResponseDTO firstTreatmentResponseDTO, File image, Operation operation, int numberOfPage, String typeImage) {

        Image imageToSaveBeforeOcr = new Image();
        imageToSaveBeforeOcr.setUid(firstTreatmentResponseDTO.getHcdResponseDTO().getUid());
        imageToSaveBeforeOcr.setOperations(operation);
        imageToSaveBeforeOcr.setName(image.getName());
        boolean documentStatus = MessageConstants.IA_RESPONSE_KO.equals(firstTreatmentResponseDTO.getResponseAi());
        String statusModeration, status;
        if (!documentStatus) {
            statusModeration = (operation.getUid().equals(OperationEnums.OPERATION_TEST_HIGHCO.getValue())
                    || operation.getUid().equals(OperationEnums.OPERATION_TEST_PROCHECK.getValue())
                    || operation.getUid().contains("BOX")) ? DocumentStatusEnum.PROCESSED.getValue() : DocumentStatusEnum.PROCESSING.getValue();
            status = statusModeration.equals(DocumentStatusEnum.PROCESSED.getValue()) ? DocumentStatusEnum.PROCESSED.getMessage() : DocumentStatusEnum.PROCESSING.getMessage();
        } else {
            if(OperationEnums.OPERATION_PG.getValue().equals(operation.getUid())){
                statusModeration = DocumentStatusEnum.WHITING_PROCESSING.getValue();
                status = DocumentStatusEnum.WHITING_PROCESSING.getMessage();
            }else{
                statusModeration = (operation.getUid().equals(OperationEnums.OPERATION_TEST_HIGHCO.getValue())
                        || operation.getUid().equals(OperationEnums.OPERATION_TEST_PROCHECK.getValue())
                        || operation.getUid().contains("BOX")) ? DocumentStatusEnum.PROCESSED.getValue() : DocumentStatusEnum.PROCESSING.getValue();
                status = statusModeration.equals(DocumentStatusEnum.PROCESSED.getValue()) ? DocumentStatusEnum.PROCESSED.getMessage() : DocumentStatusEnum.NON_EXPLOITABLE.getMessage();
            }
        }
        imageToSaveBeforeOcr.setStatusModeration(statusModeration);
        imageToSaveBeforeOcr.setProcessReport(!documentStatus ? MessageConstants.NO_ERROR : MessageConstants.BAD_QUALITY_IMAGE);
        imageToSaveBeforeOcr.setStatus(status);
        imageToSaveBeforeOcr.setOriginImageType(typeImage);
        imageToSaveBeforeOcr.setPageCount(String.valueOf(numberOfPage));
        imageJPARepository.save(imageToSaveBeforeOcr);
        return imageToSaveBeforeOcr;

    }

    private ClientRequestDTO buildClientRequest(FirstTreatmentResponseDTO firstTreatmentResponseDTO) {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setImageName(FilenameUtils.removeExtension(firstTreatmentResponseDTO.getFile().getName()));
        clientRequestDTO.setImageType(FilenameUtils.getExtension(firstTreatmentResponseDTO.getFile().getName()));
        return clientRequestDTO;
    }


    public void processIfValid(String responseApi, String uid, ResponseApiImageDTO responseApiImageDTO, File imageFile, Operation operation, HCDResponseDTO hcdResponseDTO) {

        try {
            Image imageToSave = businessRulesService.moderationProcessor(responseApi, uid, responseApiImageDTO,imageFile,operation,hcdResponseDTO);
            hcdResponseDTO.setMessage(MessageConstants.DOCUMENT_MESSAGE_OK);
            hcdResponseDTO.setUid(uid);
            responseApiImageDTO.setHcdResponseDTO(hcdResponseDTO);
            log.info("Response IA is : {}", responseApiImageDTO);
            log.info("Document after treatment : {}", imageToSave);
            log.info(" (^-^)  Fin treatment  (^-^) ");
        }catch (Exception e) {
            log.error("Error during processing for Document: {}, with operation: {}", imageFile.getName(), operation, e);
            throw new InternalServerErrorException(" An unexpected error validation document ");
        }
    }



}
