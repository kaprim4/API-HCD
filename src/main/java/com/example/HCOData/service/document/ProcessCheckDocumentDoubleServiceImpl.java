package com.example.HCOData.service.document;

import com.example.HCOData.response.ResponseExistsDocumentDTO;
import com.example.HCOData.enums.ExistFileFlagsEnum;
import com.example.HCOData.response.CheckPotentialDuplicateReceiptsDTO;
import com.example.HCOData.mappers.DocumentMapper;
import com.example.HCOData.model.BareCodes;
import com.example.HCOData.model.Image;
import com.example.HCOData.repository.ImageJPARepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class ProcessCheckDocumentDoubleServiceImpl implements ProcessCheckDocumentDoubleService {


    @Autowired
    private ImageJPARepository imageJPARepository;

    private static Set<ResponseExistsDocumentDTO> buildImageWhoHaveSameShopSameMoment(List<Image> images) {
        Set<ResponseExistsDocumentDTO> imagesMap = new HashSet<>();
        images.forEach(image ->imagesMap.add(DocumentMapper.mapImageWhoHaveSameShopSameMoment(image)));
        return imagesMap;
    }
    private static Set<ResponseExistsDocumentDTO> buildImagesWithSameData(List<Image> images) {
        Set<ResponseExistsDocumentDTO> imagesMap = new HashSet<>();
        images.forEach(image ->imagesMap.add(DocumentMapper.mapImageWithSameData(image)) );
        return imagesMap;
    }

    @Override
    @Transactional
    public CheckPotentialDuplicateReceiptsDTO checkIfTcExist(String idOperation, Image imageToSave) {

        CheckPotentialDuplicateReceiptsDTO checkPotentialDuplicateReceiptsDTO = new CheckPotentialDuplicateReceiptsDTO();
        List<String> bareCodeImageToSave = imageToSave.getBarecodes().stream().map(BareCodes::getData).collect(Collectors.toList());

        Map<String, Set<String>> mapDuplicateReceipts = new HashMap<>();
        Set<Image> imageWhoHaveSameCodeBarre;

        if (bareCodeImageToSave.size() > 0 && !bareCodeImageToSave.get(0).equals("0") && !bareCodeImageToSave.get(0).isEmpty() && !bareCodeImageToSave.get(0).contains("$")) {
            imageWhoHaveSameCodeBarre = imageJPARepository.findImageByEan(bareCodeImageToSave.get(0), imageToSave.getUid(), idOperation);
        } else {
            imageWhoHaveSameCodeBarre = new HashSet<>();
        }

        List<Image> imageWhoHaveSameShopSameMoment  = imageJPARepository.findImagesWithSameShopSameMoment(imageToSave.getSign(), imageToSave.getCity(), imageToSave.getBuyDate(), imageToSave.getBuyHour(), imageToSave.getUid(), idOperation);
        List<Image> imagesWithSameData = imageJPARepository.findImageWithSameData(imageToSave.getBuyDate(), imageToSave.getCity(), imageToSave.getBuyHour(), imageToSave.getAddress(), imageToSave.getTotal(), imageToSave.getTotalMax(), imageToSave.getArticleCount(), imageToSave.getSign(), imageToSave.getUid(), idOperation, imageToSave.getImageMD5());
        Set<String> imageWithSameProductList = imageJPARepository.findImageWithSameProducts(idOperation, imageToSave.getUid(), imageToSave.getProducts().size());

        Set<ResponseExistsDocumentDTO> setImageWhoHaveSameShopSameMoment = buildImageWhoHaveSameShopSameMoment(imageWhoHaveSameShopSameMoment);
        Set<ResponseExistsDocumentDTO> setImagesWithSameData = buildImagesWithSameData(imagesWithSameData);

        for (Image imageWhoHavSameCodeBarre : imageWhoHaveSameCodeBarre) {
            if (mapDuplicateReceipts.containsKey(imageWhoHavSameCodeBarre.getUid())) {
                Set<String> existingSet = mapDuplicateReceipts.get(imageWhoHavSameCodeBarre.getUid());
                existingSet.add(ExistFileFlagsEnum.BARCODE_ALREADY_EXISTS.name());
                mapDuplicateReceipts.put(imageWhoHavSameCodeBarre.getUid(), existingSet);
            } else {
                Set<String> setOfStrings = new HashSet<>();
                setOfStrings.add(ExistFileFlagsEnum.BARCODE_ALREADY_EXISTS.name());
                mapDuplicateReceipts.put(imageWhoHavSameCodeBarre.getUid(), setOfStrings);
            }
        }

        for (ResponseExistsDocumentDTO imageWhoHavSameCodeBarre : setImageWhoHaveSameShopSameMoment) {
            if (mapDuplicateReceipts.containsKey(imageWhoHavSameCodeBarre.getUid())) {
                Set<String> existingSet = mapDuplicateReceipts.get(imageWhoHavSameCodeBarre.getUid());
                 existingSet.add(ExistFileFlagsEnum.SAME_SHOP_SAME_MOMENT.name());
                mapDuplicateReceipts.put(imageWhoHavSameCodeBarre.getUid(), existingSet);
            } else {
                Set<String> setOfStrings = new HashSet<>();
                 setOfStrings.add(ExistFileFlagsEnum.SAME_SHOP_SAME_MOMENT.name());
                mapDuplicateReceipts.put(imageWhoHavSameCodeBarre.getUid(), setOfStrings);
            }
        }

        for (ResponseExistsDocumentDTO imageWhoHavSameCodeBarre : setImagesWithSameData) {
            if (mapDuplicateReceipts.containsKey(imageWhoHavSameCodeBarre.getUid())) {
                Set<String> existingSet = mapDuplicateReceipts.get(imageWhoHavSameCodeBarre.getUid());
                existingSet.add(ExistFileFlagsEnum.IDENTICAL_EXISTING_FILE.name());
                existingSet.add(ExistFileFlagsEnum.EXISTING_MD5.name());
                mapDuplicateReceipts.put(imageWhoHavSameCodeBarre.getUid(), existingSet);
            } else {
                Set<String> setOfStrings = new HashSet<>();
                setOfStrings.add(ExistFileFlagsEnum.IDENTICAL_EXISTING_FILE.name());
                setOfStrings.add(ExistFileFlagsEnum.EXISTING_MD5.name());
                mapDuplicateReceipts.put(imageWhoHavSameCodeBarre.getUid(), setOfStrings);
            }
        }

        for (String imageWithSameProduct : imageWithSameProductList) {
            if (!imageToSave.getUid().equals(imageWithSameProduct)) {
                if (mapDuplicateReceipts.containsKey(imageWithSameProduct)) {
                    Set<String> existingSet = mapDuplicateReceipts.get(imageWithSameProduct);
                    existingSet.add(ExistFileFlagsEnum.EXISTING_PRODUCT_LIST.name());
                    mapDuplicateReceipts.put(imageWithSameProduct, existingSet);
                } else {
                    Set<String> setOfStrings = new HashSet<>();
                     setOfStrings.add(ExistFileFlagsEnum.EXISTING_PRODUCT_LIST.name());
                    mapDuplicateReceipts.put(imageWithSameProduct, setOfStrings);
                }
            }
        }
        log.info("duplicate receipts is : {} ", mapDuplicateReceipts);

        if (!mapDuplicateReceipts.isEmpty()) {
            checkPotentialDuplicateReceiptsDTO.setDuplicate(true);
        }
        checkPotentialDuplicateReceiptsDTO.setPotentialDuplicateReceipts(mapDuplicateReceipts);
        return checkPotentialDuplicateReceiptsDTO;
    }


}
