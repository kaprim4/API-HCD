package com.example.HCOData.validators;

import com.example.HCOData.constant.MessageConstants;
import com.example.HCOData.repository.OperationRepository;
import com.example.HCOData.request.ClientRequestDTO;
import com.example.HCOData.response.ClientDocumentParametersResponseDTO;
import com.example.HCOData.exception.ImageBadRequestException;
import com.example.HCOData.exception.OperationNotFoundException;
import com.example.HCOData.service.document.OperationService;
import com.example.HCOData.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@Slf4j
public class ClientRequestValidator {

    @Autowired
    OperationService operationService;

    @Autowired
    OperationRepository operationRepository;


    private void buildClientDocumentResponseDto(MultipartFile image, File docFile) {
        int numberOfPages = ImageUtil.getNumberPagesPdf(image);
        String imageType = ImageUtil.getTypeImage(image);
        ClientDocumentParametersResponseDTO.builder()
                .imageUid(UUID.randomUUID().toString())
                .imageType(imageType)
                .numberOfPages(numberOfPages)
                .image(docFile)
                .build();
    }


    public void validateClientRequest(ClientRequestDTO clientRequestDTOObject, MultipartFile image, String operation) {

        if (image == null || image.isEmpty()) {
            throw new ImageBadRequestException(MessageConstants.ERROR_EMPTY_OR_NULL_IMAGE_FILE_MSG);
        }

        if (clientRequestDTOObject == null) {
            throw new ImageBadRequestException(MessageConstants.ERROR_NULL_CLIENT_REQUEST_MSG);
        }

        String imageName = clientRequestDTOObject.getImageName();
        String idOperation = clientRequestDTOObject.getIdOperation();
        String imageType = clientRequestDTOObject.getImageType();

        if (isNullOrEmpty(imageName)) {
            throw new ImageBadRequestException(MessageConstants.ERROR_EMPTY_DOC_NAME_MSG);
        }
        if (!operationRepository.existsByUid(operation)) {
            throw new OperationNotFoundException(MessageConstants.ERROR_OP_NOT_FOUND_MSG);
        }

        if (isNullOrEmpty(idOperation)) {
            throw new ImageBadRequestException(MessageConstants.ERROR_EMPTY_OP_MSG);
        }

        if (isNullOrEmpty(imageType)) {
            throw new ImageBadRequestException(MessageConstants.ERROR_EMPTY_IMAGE_TYPE_MSG);
        }

        if (!image.getOriginalFilename().equals(imageName)) {
            throw new ImageBadRequestException(MessageConstants.ERROR_FILE_NAME_NOT_MATCH_MSG);
        }

        if (operation == null) {
            throw new OperationNotFoundException(MessageConstants.ERROR_OP_NOT_FOUND_MSG);
        }
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
