package com.example.HCOData.service.document;

import com.example.HCOData.constant.DocumentsPathConstants;
import com.example.HCOData.constant.MessageConstants;
import com.example.HCOData.constant.UrlConstants;
import com.example.HCOData.enums.OperationEnums;
import com.example.HCOData.exception.ImageNotFoundException;
import com.example.HCOData.exception.NotPAErrorException;
import com.example.HCOData.mappers.ImageMapperInterface;
import com.example.HCOData.model.Image;
import com.example.HCOData.repository.ImageJPARepository;
import com.example.HCOData.repository.OperationRepository;
import com.example.HCOData.request.ClientRequestDTO;
import com.example.HCOData.response.FirstTreatmentResponseDTO;
import com.example.HCOData.response.HCDResponseDTO;
import com.example.HCOData.exception.InternalServerErrorException;
import com.example.HCOData.model.Operation;
import com.example.HCOData.response.ImageResponseDTO;
import com.example.HCOData.service.aitreatment.AITreatmentService;
import com.example.HCOData.service.aitreatment.AIWorkflowService;
import com.example.HCOData.utils.ImageUtil;
import com.example.HCOData.validators.ClientRequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProcessDocumentServiceImpl implements ProcessDocumentService {

    @Autowired
    private AITreatmentService aiTreatmentService;

    @Autowired
    private AIWorkflowService aIWorkflowService;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    ClientRequestValidator clientRequestValidator;

    @Autowired
    private ImageMapperInterface imageMapperInterface;

    @Autowired
    private ImageJPARepository imageJPARepository;


    @Override
    public HCDResponseDTO startProcess(String clientRequest, MultipartFile image) throws RuntimeException, IOException {
       try {
           ClientRequestDTO clientRequestDTO = getClientRequestDTO(clientRequest);
           File document = saveDocument(image, clientRequestDTO);
           String response = processAI(ImageUtil.convertImageToBase64(document), clientRequestDTO);
           clientRequestValidator.validateClientRequest(clientRequestDTO, image, clientRequestDTO.getIdOperation());
           Operation operation = operationRepository.getOperationByUid(clientRequestDTO.getIdOperation()).get();
           int numberOfPages = ImageUtil.getNumberPagesPdf(image);
           String imageType = ImageUtil.getTypeImage(image);
           HCDResponseDTO documentResponse = processImage(document, operation, numberOfPages, imageType, UUID.randomUUID().toString(), response).getHcdResponseDTO();
           if (documentResponse.getMessage().equals(MessageConstants.ERROR_NOT_PA_MSG)) {
               throw new NotPAErrorException(MessageConstants.ERROR_NOT_PA_MSG);
           }
           return documentResponse;
       }catch (ClientAbortException e){
           log.error("Client aborted connection: {} " , e.getMessage());
           throw new InternalServerErrorException("Client aborted connection");
       }catch (Exception e){
           log.error(e.getMessage());
           throw new InternalServerErrorException("An exception occurred while processing the image.");
       }

    }

    private static ClientRequestDTO getClientRequestDTO(String clientRequest) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ClientRequestDTO clientRequestDTO = mapper.readValue(clientRequest, ClientRequestDTO.class);
        return clientRequestDTO;
    }

    private String processAI(String imageToBase64, ClientRequestDTO clientRequestDTO) {
        long startProcessingTime = System.currentTimeMillis();
        log.info("Process start : {} ",startProcessingTime + " ms");
            String response;
            if(isPGOperation(clientRequestDTO)){
                response = MessageConstants.IA_RESPONSE_KO;
            }else{
                response = aiTreatmentService.processAIResult(imageToBase64, UrlConstants.API_AI_URL_SOURCE_SERVER, UrlConstants.API_AI_URL_BACKUP);
            }
            log.info("response IA : {} ", response);
            long endProcessingTime = System.currentTimeMillis();
            long duration = endProcessingTime - startProcessingTime;
            log.info("duration is : {} ",duration + " ms");
            return response;
    }

    private static boolean isPGOperation(ClientRequestDTO clientRequestDTO) {
        return OperationEnums.OPERATION_PG.getValue().equals(clientRequestDTO.getIdOperation());
    }

    private static File saveDocument(MultipartFile image, ClientRequestDTO clientRequestDTO) throws IOException {
        File imageFile = ImageUtil.multipartToFile(image, clientRequestDTO.getImageType());
        ImageUtil.sendImageToFolder(imageFile, DocumentsPathConstants.SECOND_DESTINATION);
        return imageFile;
    }

    @Override
    public FirstTreatmentResponseDTO processImage(File image, Operation operation, int numberOfPage, String typeImage, String uid, String responseIA) {

        log.info("Document is: {}, Operation is: {} ", image, operation.getUid());
        FirstTreatmentResponseDTO result;
        try {
            result = aIWorkflowService.executeAITreatment(image, operation, uid, responseIA);
            aIWorkflowService.validateAITreatment(image, operation, result, numberOfPage, typeImage, uid);

        } catch (IOException e) {
            log.error("Error during second treatment for Document: {}, with operation: {}", image.getName(), operation.getUid(), e);
            throw new InternalServerErrorException("An unexpected error occurred while processing the image");
        } catch (Exception e) {
            log.error("Error during processing for Document: {}, with operation: {}", image.getName(), operation.getUid(), e);
            throw new InternalServerErrorException("An unexpected error occurred while processing the image" + e.getMessage());
        }

        log.info("Processing completed for Document: {}", image.getName());
        return result;
    }

    @Transactional
    @Override
    public ImageResponseDTO findImageByUid(String uid) {
        log.info("[HCDServiceImpl] Fetching image with UID: {}", uid);
        try {
            Image image = imageJPARepository.findByUid(uid);
            if (image == null) {
                throw new ImageNotFoundException("Image with UID " + uid + " not found");
            }
            String operationId = Optional.ofNullable(image.getOperations())
                    .map(Operation::getUid)
                    .orElse(null);
            log.info("[HCDServiceImpl] Processing image with {}", operationId == null ? "no operation" : "operation UID: " + operationId);
            return imageMapperInterface.mapImageToImageDTO(image, operationId);

        } catch (ImageNotFoundException e) {
            log.error("uid not found", e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("An unexpected error occurred while fetching the image: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while fetching the image");
        }
    }


}
