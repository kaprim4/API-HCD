package com.example.HCOData.mappers;

import com.beust.jcommander.internal.Lists;

import com.example.HCOData.constant.DocumentsPathConstants;
import com.example.HCOData.constant.MessageConstants;
import com.example.HCOData.exception.InternalServerErrorException;
import com.example.HCOData.response.AnomalyReport;
import com.example.HCOData.response.BareCodesDTO;
import com.example.HCOData.response.DataDTO;
import com.example.HCOData.response.ImageResponseDTO;
import com.example.HCOData.response.ImageResponseToCompareDTO;
import com.example.HCOData.response.ItemDTO;
import com.example.HCOData.response.ItemRuleDTO;
import com.example.HCOData.response.ItemRuleAndItemIsMatchedDTO;
import com.example.HCOData.response.PotentialDuplicateReceiptsDTO;
import com.example.HCOData.response.ProcessedRules;
import com.example.HCOData.response.ProductDTO;
import com.example.HCOData.response.ProductResponseDTO;
import com.example.HCOData.request.ProductResponseAiToCompareDTO;
import com.example.HCOData.response.RulesDTO;
import com.example.HCOData.response.ShopDTO;
import com.example.HCOData.enums.DocumentStatusEnum;
import com.example.HCOData.enums.OperationEnums;
import com.example.HCOData.model.*;
import com.example.HCOData.repository.*;
import com.example.HCOData.service.document.ProcessCheckDocumentDoubleService;
import com.example.HCOData.utils.ImageUtil;
import com.example.HCOData.validators.DateTimeValidator;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@Service
@Slf4j
public class imageMapper implements ImageMapperInterface {

    @Autowired
    private ImageJPARepository imageJPARepository;

    @Autowired
    private ProcessCheckDocumentDoubleService processCheckDocumentDoubleService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private OperationRepository operationRepository;

    private boolean isMatchedPriceRule = true;

    @Autowired
    FlagsRepository flagsRepository;

    @Autowired
    private OperationTermsRepository operationTermsRepositorty;

    @Autowired
    private OperationBrandCategoryRepository operationBrandCategoryRepository;


    @Override
    public ImageResponseDTO mapImageToImageDTO(Image image, String idOperation) {
        String status = Optional.ofNullable(image.getStatus()).orElse("");
        String statusModeration = Optional.ofNullable(image.getStatusModeration()).orElse("");
        String processReport = Optional.ofNullable(image.getProcessReport()).orElse("");
        String uid = Optional.ofNullable(image.getUid()).orElse("");
        String name = Optional.ofNullable(image.getName()).orElse("");
        String endpointUid = Optional.ofNullable(image.getEndpointUid()).orElse("");
        String campaignUid = idOperation == null ? "00000" : idOperation;
        String dominantLanguage = idOperation != null && idOperation.contains("BE") ? "bl-BL" : "fr-FR";

        boolean isProcessedStatus = status.equals(DocumentStatusEnum.HUMAN_PROCESSED.getMessage()) ||
                status.equals(DocumentStatusEnum.PROCESSED.getMessage());
        boolean isHumanProcessed = statusModeration.equals(DocumentStatusEnum.HUMAN_PROCESSED.getValue());

        if (isProcessedStatus && isHumanProcessed) {
            if (processReport.isEmpty() || "null".equals(processReport)) {
                processReport = MessageConstants.NO_ERROR;
            }
            if (MessageConstants.NO_ERROR.equals(processReport)) {
                return buildFullImageResponse(image, uid, campaignUid, dominantLanguage, processReport, endpointUid, name, status, idOperation);
            } else if (MessageConstants.BAD_QUALITY_IMAGE.equals(processReport)) {
                return buildBasicImageResponse(image, uid, campaignUid, dominantLanguage, processReport, endpointUid, name, status);
            }
        }
        return buildProcessingImageResponse();
    }

    private ImageResponseDTO buildFullImageResponse(Image image, String uid, String campaignUid, String dominantLanguage,
                                                    String processReport, String endpointUid, String name, String status, String idOperation) {

        ImageResponseDTO fullImageResponseDTO = new ImageResponseDTO();
        fullImageResponseDTO.setUid(uid);
        fullImageResponseDTO.setCreatedAt(DateTimeValidator.formatDate(image.getCreatedAt()));
        fullImageResponseDTO.setUpdatedAt(image.getUpdatedAt() == null ? DateTimeValidator.formatDate(image.getCreatedAt()) : DateTimeValidator.formatDate(image.getUpdatedAt()));
        fullImageResponseDTO.setCompanyUid("Highco-data");
        fullImageResponseDTO.setImageUrl(DocumentsPathConstants.FIRST_DESTINATION);
        fullImageResponseDTO.setDominantLanguage(dominantLanguage);
        fullImageResponseDTO.setCampaignUid(campaignUid);
        fullImageResponseDTO.setStatus(status);
        fullImageResponseDTO.setName(name);
        fullImageResponseDTO.setProcessReport(processReport);
        fullImageResponseDTO.setAnomalyReport(mapImageToAnomalyReport(image, idOperation));
        fullImageResponseDTO.setEndpointUid(endpointUid);
        fullImageResponseDTO.setBarcodes(mapBareCodesToBareCodesDTO(image));
        fullImageResponseDTO.setData(mapImageToDataDTO(image).block());
        fullImageResponseDTO.setProcessedRules(mapImageToProcessedRulesDTO(image, idOperation));
        called(image);
        return fullImageResponseDTO;
    }

    private ImageResponseDTO buildBasicImageResponse(Image image, String uid, String campaignUid, String dominantLanguage,
                                                     String processReport, String endpointUid, String name, String status) {

        ImageResponseDTO basicImageResponseDTO = new ImageResponseDTO();
        basicImageResponseDTO.setUid(uid);
        basicImageResponseDTO.setCreatedAt(DateTimeValidator.formatDate(image.getCreatedAt()));
        basicImageResponseDTO.setUpdatedAt(image.getUpdatedAt() == null ? DateTimeValidator.formatDate(image.getCreatedAt()) : DateTimeValidator.formatDate(image.getUpdatedAt()));
        basicImageResponseDTO.setCompanyUid("Highco-data");
        basicImageResponseDTO.setImageUrl(DocumentsPathConstants.FIRST_DESTINATION);
        basicImageResponseDTO.setDominantLanguage(dominantLanguage);
        basicImageResponseDTO.setCampaignUid(campaignUid);
        basicImageResponseDTO.setStatus(status);
        basicImageResponseDTO.setName(name);
        basicImageResponseDTO.setProcessReport(processReport);
        basicImageResponseDTO.setEndpointUid(endpointUid);
        called(image);
        return basicImageResponseDTO;
    }

    private ImageResponseDTO buildProcessingImageResponse() {
        ImageResponseDTO imageResponseDTO = new ImageResponseDTO();
        imageResponseDTO.setStatus(DocumentStatusEnum.PROCESSING.getMessage());
        return imageResponseDTO;
    }

    private void called(Image image) {
        try {
            image.setCalled(true);
            imageJPARepository.save(image);
            log.info("Json is recuperated successfully");
        } catch (Exception e) {
            log.info(" Error in process get Json");
            e.printStackTrace();
        }

    }

    @Override
    public ImageResponseDTO mapPostImageToImageDTO(Image image, String idOperation) {

        ImageResponseDTO imageResponseDTO = new ImageResponseDTO();
        imageResponseDTO.setUid(image.getUid());
        imageResponseDTO.setCreatedAt(image.getCreatedAt().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
        imageResponseDTO.setCompanyUid("Highco-data");
        imageResponseDTO.setImageUrl(DocumentsPathConstants.FIRST_DESTINATION);
        imageResponseDTO.setDominantLanguage(idOperation.contains("BE") ? "bl-BL" : "fr-FR");
        imageResponseDTO.setCampaignUid(idOperation == null ? "00000" : idOperation);
        imageResponseDTO.setStatus(image.getStatus() == null ? "" : image.getStatus());
        imageResponseDTO.setName(image.getName() == null ? "" : image.getName());
        imageResponseDTO.setProcessReport(image.getProcessReport() == null ? "" : image.getProcessReport());
        log.info("after setProcessReport");
        imageResponseDTO.setEndpointUid(image.getEndpointUid() == null ? "" : image.getEndpointUid());
        imageResponseDTO.setBarcodes(mapBareCodesToBareCodesDTO(image));
        log.info("after setProcessReport 1111");
        imageResponseDTO.setData(mapImageToDataDTO(image).block());
        log.info("after setProcessReport 22222");
        imageResponseDTO.setProcessedRules(mapImageToProcessedRulesDTO(image, idOperation));
        log.info("FIN DE TREATMENT DOCUMENTS : {} ", imageResponseDTO);
        return imageResponseDTO;
    }


    public ProcessedRules mapImageToProcessedRulesDTO(Image image, String idOperation) {
        try {
            log.info("mapImageToProcessedRulesDTO 3333333");
            Operation operation = operationRepository.getOperationByUid(idOperation).get();
            log.info("mapImageToProcessedRulesDTO 4444444 : {}", operation.getUid());
            ProcessedRules processedRules = new ProcessedRules();
            processedRules.setName(operation.getUid());
            processedRules.setTerm("");
            RulesDTO.dateRule dateRule = mapImageToDateRule(operation, image);
            RulesDTO.SignRule signRule = mapImageToSignRule(operation, image);
            RulesDTO.PriceRule priceRule = mapImageToPriceRule(operation, image);
            log.info("mapImageToProcessedRulesDTO 55555 : {}", operation.getUid());

            ItemRuleAndItemIsMatchedDTO itemRule = mapImageToItemRule(operation, image);
            processedRules.setDateRule(dateRule);
            processedRules.setSignRule(signRule);
            processedRules.setPriceRule(priceRule);
            processedRules.setItemRuleDTO(itemRule != null ? itemRule.getItemRuleDTO() : null);
            if (itemRule != null) {
                processedRules.setItemsPriceMatched(itemRule.isItemIsMatchedPrice());
                processedRules.setQuantityItemMatched(itemRule.isQuantityMatched());
                processedRules.setTotalPriceOfMatchedItems(itemRule.getTotalPriceItemMatched());
            } else {
                processedRules.setTotalPriceOfMatchedItems(null);
            }
            processedRules.setAllRulesMatched(
                    (dateRule != null && dateRule.isMatched()) &&
                            (signRule != null && signRule.isMatched()) &&
                            (priceRule != null && priceRule.isMatched()) &&
                            (itemRule != null && itemRule.isItemIsMatchedPrice())
            );
            return processedRules;
        } catch (Exception e) {
            log.error("Error pendant le treatment de document", e);
            e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        }

    }


    public RulesDTO.dateRule mapImageToDateRule(Operation operation, Image image) {
        RulesDTO.dateRule rule = null;
        if (operation.getMin() != null || operation.getMax() != null) {
            rule = new RulesDTO.dateRule();
            rule.setMin(operation.getMin());
            rule.setMax(operation.getMax());
            String date = image.getBuyDate();
            if (DateTimeValidator.checkIfDateExistEntreStartAndEndDate(operation.getMin(), operation.getMax(), date)) {
                rule.setMatched(true);
            }
        }

        return rule;
    }

    public RulesDTO.PriceRule mapImageToPriceRule(Operation operation, Image image) {
        RulesDTO.PriceRule rule = null;

        if (operation.getEligibleOnly() != null || operation.getTotalAmount() != null || (operation.getQuantityRequired() != null && operation.getQuantityRequired().longValue() != 0 ||
                mapImageToItemRule(operation, image).isItemIsMatchedPrice())) {
            rule = new RulesDTO.PriceRule();
            rule.setEligibleOnly(operation.getEligibleOnly());
            rule.setTotalAmount(operation.getTotalAmount());
            rule.setMatched(mapImageToItemRule(operation, image).isItemIsMatchedPrice());
        }

        return rule;
    }

    @Transactional
    public ItemRuleAndItemIsMatchedDTO mapImageToItemRule(Operation operation, Image image) {
        ItemRuleAndItemIsMatchedDTO itemRuleAndItemIsMatchedDTO = new ItemRuleAndItemIsMatchedDTO();
        boolean isItemIsMatched = false;
        Set<RulesDTO.ORDTO> ORDTOS = new HashSet<>();
        Set<String> itemMatched = new HashSet<>();
        List<String> itemMatchedList = new ArrayList<>();
        BigDecimal totalPriceOfMatchedItem = new BigDecimal(0.00);
        ItemRuleDTO rule = new ItemRuleDTO();
        rule.setQuantityRequired(operation.getQuantityRequired());
        rule.setStrategy(operation.getStrategy());
        log.info("In map Image To ItemRule");
        if (operation.getTerm().equals(OperationEnums.OPERATION_VEEPPE.getValue())) {
            List<Product> products = image.getProducts();
            List<OperationBrandCategory> operationBrandCategory = operationBrandCategoryRepository.findByOperation_uid(operation.getUid());
            for (OperationBrandCategory brandCategory : operationBrandCategory) {
                Long quantityMatched = 0L;
                RulesDTO.ORDTO ordto = new RulesDTO.ORDTO();
                ordto.setTerm(brandCategory.getBrand() + " | " + brandCategory.getCategory());
                List<String> listOfItemMatched = new ArrayList<>();
                for (Product product : products) {
                    if (product.getCategory().contains(brandCategory.getCategory().trim()) && product.getBrand().contains(brandCategory.getBrand().trim())) {
                        if (operation.getEligibleOnly() != null && product.getUnitPrice().compareTo(operation.getEligibleOnly()) == -1) {
                            isMatchedPriceRule = false;
                        }
                        if (product.getId() != null) {
                            itemMatched.add(product.getId().toString());
                        } else {
                            itemMatched.add("0");
                        }
                        isItemIsMatched = true;
                        quantityMatched++;
                        ordto.setMatched(true);
                        rule.setMatched(true);
                        ORDTOS.add(ordto);
                        if (product.getId() != null && isItemIsMatched) {
                            listOfItemMatched.add(product.getId().toString());
                            totalPriceOfMatchedItem = totalPriceOfMatchedItem.add(product.getUnitPrice());

                        }

                    }

                }
                ordto.setItemsMatched(listOfItemMatched);
                ordto.setQuantityMatched(quantityMatched);

            }

        } else {
            log.info("In map Image To ItemRule in else");
            List<OperationTerms> operationTermes = getKeyWords(operation.getUid());

            for (OperationTerms listOfOperationTerms : operationTermes) {
                Long quantityMatched = 0L;
                RulesDTO.ORDTO ordto = new RulesDTO.ORDTO();
                ordto.setTerm(listOfOperationTerms.getName());
                List<String> listOfItemMatched = new ArrayList<>();
                for (Product product : image.getProducts()) {
                    if (StringUtils.containsIgnoreCase(product.getRawLabel().replace(" ", "").trim(), ImageUtil.convertStringFromAccToReg(listOfOperationTerms.getName()).replace(" ", "").trim())) {
                        if (operation.getEligibleOnly() != null && product.getUnitPrice().compareTo(operation.getEligibleOnly()) == -1) {
                            isMatchedPriceRule = false;
                        }
                        if (product.getId() != null) {
                            itemMatched.add(product.getId().toString());
                        } else {
                            itemMatched.add("0");
                        }
                        isItemIsMatched = true;
                        quantityMatched++;
                        ordto.setMatched(true);
                        rule.setMatched(true);
                        ORDTOS.add(ordto);

                        if (product.getId() != null && isItemIsMatched) {
                            listOfItemMatched.add(product.getId().toString());
                            totalPriceOfMatchedItem = totalPriceOfMatchedItem.add(product.getUnitPrice());

                        }
                    }
                }
                ordto.setItemsMatched(listOfItemMatched);
                ordto.setQuantityMatched(quantityMatched);

            }


        }

        rule.setItemsMatched(itemMatchedList);
        rule.setOR(Lists.newArrayList(ORDTOS));

        if (operation.getUid().equals("023427") && itemMatched.size() < 2) {
            isItemIsMatched = false;
        }
        rule.setMatched(isItemIsMatched);
        for (String itemMatchedElement : itemMatched) {
            itemMatchedList.add(itemMatchedElement);
        }

        if (rule.getItemsMatched().size() == 0 && rule.getOR().size() == 0) {
            rule = null;
        }

        if (operation.getQuantityRequired() != null) {
            if (rule != null && rule.getItemsMatched().size() > operation.getQuantityRequired().longValue()) {
                itemRuleAndItemIsMatchedDTO.setQuantityMatched(true);
            }
        } else {
            System.out.println("Quantity null");
        }

        List<Long> longList = new ArrayList<>();
        for (String str : itemMatchedList) {
            longList.add(Long.parseLong(str));
        }

        BigDecimal totalOfPriceMatched = productJpaRepository.sumPricesByIds(longList);
        itemRuleAndItemIsMatchedDTO.setItemRuleDTO(rule);
        itemRuleAndItemIsMatchedDTO.setItemIsMatchedPrice(isMatchedPriceRule);

        if (totalOfPriceMatched == null || totalOfPriceMatched.equals(BigDecimal.ZERO)) {
            if (rule == null) {
                itemRuleAndItemIsMatchedDTO.setTotalPriceItemMatched(null);
            } else {
                itemRuleAndItemIsMatchedDTO.setTotalPriceItemMatched(BigDecimal.ZERO);
            }
        } else {
            itemRuleAndItemIsMatchedDTO.setTotalPriceItemMatched(totalOfPriceMatched);
        }

        if (totalOfPriceMatched == null || (operation.getTotalAmount() != null && totalOfPriceMatched.compareTo(operation.getTotalAmount()) == -1)) {
            isMatchedPriceRule = false;
        }

        if (operation.getTotalAmount() == null) {
            isMatchedPriceRule = false;
        }
        if (operation.getEligibleOnly() == null) {
            isMatchedPriceRule = false;
        } else {
            itemRuleAndItemIsMatchedDTO.setItemIsMatchedPrice(true);
        }

        itemRuleAndItemIsMatchedDTO.setItemMatchedBoolean(isItemIsMatched);
        return itemRuleAndItemIsMatchedDTO;

    }

    @Transactional(readOnly = true)
    public List<OperationTerms> getKeyWords(String uid) {
        List<OperationTerms> keyWords = operationTermsRepositorty.findByOperation_uid(uid);
        if (keyWords == null) {
            return null;
        } else {
            return keyWords;
        }

    }

    @Transactional
    public RulesDTO.SignRule mapImageToSignRule(Operation operation, Image image) {
        String sign = image.getSign();
        RulesDTO.SignRule rule = new RulesDTO.SignRule();
        List<SignOperation> signOperationList = operationRepository.findAllSignOperation(operation.getUid());
        if (operation.isCheckSign()) {
            if (!signOperationList.isEmpty()) {
                boolean matched = signOperationList.stream()
                        .anyMatch(operationElement ->
                                operationElement.getName().equalsIgnoreCase(sign) || operation.getUid().equals(operationElement.getOperation()));
                rule.setMatched(matched);
            } else {
                rule.setMatched(false);
            }
        } else {
            rule.setMatched(true);
        }

        rule.setSignList(signOperationList.stream().map(SignOperation::getName).collect(Collectors.toList()));
        return rule;
    }


    public AnomalyReport mapImageToAnomalyReport(Image image, String idOperation) {
        AnomalyReport anomalyReport = new AnomalyReport();
        Operation operation = getOperation(idOperation);

        if (operation != null && operation.isCheckDoublon()) {
            return mapPotentialDuplicateReceipt(anomalyReport, processCheckDocumentDoubleService.checkIfTcExist(idOperation, image).getPotentialDuplicateReceipts());
        } else {
//            anomalyReport.setVerifyDouble(false);
//            return anomalyReport;
            return null;
        }
    }


    public Operation getOperation(String idOperation) {
        return operationRepository.getOperationByUid(idOperation).get();
    }

    public AnomalyReport mapPotentialDuplicateReceipt(AnomalyReport anomalyReport, Map<String, Set<String>> mapDuplicateReceipt) {
        Set<String> setFlags = new HashSet<>();
        List<PotentialDuplicateReceiptsDTO> potentialDuplicateReceiptsDTOS = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : mapDuplicateReceipt.entrySet()) {
            PotentialDuplicateReceiptsDTO potentialDuplicateReceiptsDTO = new PotentialDuplicateReceiptsDTO();
            potentialDuplicateReceiptsDTO.setUid(entry.getKey());
            potentialDuplicateReceiptsDTO.setFlags(new ArrayList<>(entry.getValue()));
            for (String flag : entry.getValue()) {
                setFlags.add(flag);
            }
            potentialDuplicateReceiptsDTOS.add(potentialDuplicateReceiptsDTO);
        }
        anomalyReport.setPotentialDuplicateReceipts(potentialDuplicateReceiptsDTOS);
        anomalyReport.setFlags(new ArrayList<>(setFlags));
        return anomalyReport;

    }

    public AnomalyReport mapPotentialDuplicateReceiptPost(Image image, AnomalyReport anomalyReport, Map<String, Set<String>> mapOfPotentielDuplicateReceipt) {
        Set<String> setFlags = new HashSet<>();
        List<PotentialDuplicateReceiptsDTO> potentialDuplicateReceiptsDTOS = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : mapOfPotentielDuplicateReceipt.entrySet()) {
            PotentialDuplicateReceiptsDTO potentialDuplicateReceiptsDTO = new PotentialDuplicateReceiptsDTO();
            String path = getCodeId(entry.getKey());
            potentialDuplicateReceiptsDTO.setUid(path);
            potentialDuplicateReceiptsDTO.setFlags(new ArrayList<>(entry.getValue()));
            for (String flag : entry.getValue()) {
                setFlags.add(flag);
            }
            potentialDuplicateReceiptsDTOS.add(potentialDuplicateReceiptsDTO);
        }
        anomalyReport.setPotentialDuplicateReceipts(potentialDuplicateReceiptsDTOS);
        anomalyReport.setFlags(new ArrayList<>(setFlags));
        List<PotentialDuplicateReceiptsDTO> potentialDuplicateReceipts = anomalyReport.getPotentialDuplicateReceipts();
        potentialDuplicateReceipts.forEach(f -> {
            if (f.getFlags() != null) {
                f.getFlags().forEach(t -> {
                    if (!flagsRepository.existsByImageUid(image.getUid())) {
                        Flags flag = new Flags();
                        flag.setImageUidFlag(f.getUid());
                        flag.setName(t);
                        flag.setImageUid(image.getUid());
                        flag.setDuplcate(false);
                        flagsRepository.save(flag);
                    } else {
                        log.info("FLAG ALREADY EXISTS BY ", image.getUid());
                    }

                });
            } else {
                log.info("FLAG IS NULL");
            }
        });
        return anomalyReport;
    }


    public String getCodeId(String uid) {
        return extractUID(imageJPARepository.getNameImage(uid));
    }

    public static String extractUID(String filename) {
        if (filename != null && filename.length() >= 44) {
            return filename.substring(8, 44);
        } else {
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex != -1) {
                return filename.substring(0, dotIndex);
            }
            return null;
        }
    }

    public Mono<DataDTO> mapImageToDataDTO(Image image) {
        return Mono.just(image)
                .flatMap(img -> {
                    DataDTO dto = new DataDTO();
                    dto.setBuyHour(getValidBuyHour(img.getBuyHour()));
                    dto.setBuyDate(getValidBuyDate(img.getBuyDate()));
                    dto.setArticleCount(img.getArticleCount());
                    dto.setTotal(img.getTotal());
                    dto.setTotalMax(img.getTotalMax());
                    List<Payement> paymentList = Optional.ofNullable(img.getPayment()).orElseGet(Collections::emptyList);
                    dto.setPayment(paymentList.stream().map(Payement::getName).collect(Collectors.toList()));
                    dto.setLocale(Optional.ofNullable(img.getLocale()).orElse(""));
                    dto.setShop(mapImageToShopDto(img));

                    return Flux.fromIterable(img.getProducts())
                            .parallel()
                            .runOn(Schedulers.parallel())
                            .map(this::mapImageToItemDTO)
                            .sequential()
                            .collectList()
                            .doOnNext(dto::setItems)
                            .then(Mono.just(dto));
                })
                .doOnSuccess(dto -> {
                    long endMapImageToDataDTOTime = System.currentTimeMillis();
                });
    }

    private ItemDTO mapImageToItemDTO(Product product) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(Optional.ofNullable(product.getId()).map(Object::toString).orElse(""));
        itemDTO.setRawLabel(product.getRawLabel().replace("\n", ""));
        itemDTO.setShortLabel(product.getShortLabel().replace("\n", ""));
        itemDTO.setQuantity(product.getQuantity());
        itemDTO.setUnitPrice(product.getUnitPrice().setScale(2, RoundingMode.HALF_UP));
        itemDTO.setPrice(product.getPrice().setScale(2, RoundingMode.HALF_UP));
        itemDTO.setPackageUnity(Optional.ofNullable(product.getPackageUnity()).orElse(""));
        itemDTO.setMass(product.getMass());
        itemDTO.setFlags(Collections.emptyList());
        itemDTO.setProduct(mapImageToProductDTO(product));
        return itemDTO;
    }

    private String getValidBuyHour(String buyHour) {
        return (buyHour == null || buyHour.equals("00:00:00") || buyHour.contains("nul")) ? "" : buyHour;
    }

    private String getValidBuyDate(String buyDate) {
        return (buyDate == null || buyDate.equals("0000-00-00")) ? "0000-00-00" : buyDate;
    }

    public ShopDTO mapImageToShopDto(Image image) {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setCity(Optional.ofNullable(image.getCity()).orElse(""));
        shopDTO.setSign(Optional.ofNullable(image.getSign()).orElse(""));
        shopDTO.setSubSign(Optional.ofNullable(image.getSubSign()).orElse(""));
        shopDTO.setPhone(Optional.ofNullable(image.getPhone()).orElse(""));
        shopDTO.setAddress(Optional.ofNullable(image.getAddress()).orElse(""));
        shopDTO.setPostalCode(Optional.ofNullable(image.getPostalCode()).orElse(""));
        shopDTO.setGeoLocation(Optional.ofNullable(image.getGeoLocation()).orElse(""));
        return shopDTO;
    }

    public ProductDTO mapImageToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setBrand(product.getBrand() == null ? "" : product.getBrand());
        productDTO.setCategory(product.getCategory() == null ? "" : product.getCategory());
        productDTO.setBrandLine(product.getBrandLine() == null ? "" : product.getBrandLine());
        productDTO.setBrandUid(product.getBrandUid() == null ? "" : product.getBrandUid());
        productDTO.setCategoryUid(product.getCategoryUid() == null ? "" : product.getCategoryUid());
        productDTO.setGtin(product.getGtin() == null ? "" : product.getGtin());
        return productDTO;
    }

    public List<BareCodesDTO> mapBareCodesToBareCodesDTO(Image image) {
        return image.getBarecodes().stream()
                .map(this::createBareCodesDTO)
                .collect(Collectors.toList());
    }

    private BareCodesDTO createBareCodesDTO(BareCodes bareCode) {
        BareCodesDTO bareCodeDTO = new BareCodesDTO();
        bareCodeDTO.setType("Codabar");
        String data = Optional.ofNullable(bareCode.getData())
                .filter(d -> !d.contains("$") && !d.equals("0"))
                .orElse("");
        bareCodeDTO.setData(data);
        return bareCodeDTO;
    }

    public List<PotentialDuplicateReceiptsDTO> mapPotentialDuplicateReceipts(Image image) {
        List<PotentialDuplicateReceiptsDTO> listOfPotentielDuplicateReceiptsDTO = new ArrayList<>();
        List<PotentialDuplicateReceipts> listOfPotentielDuplicateReceipts = image.getPotentialDuplicateReceipts();
        for (PotentialDuplicateReceipts potentialDuplicateReceipts : listOfPotentielDuplicateReceipts) {
            PotentialDuplicateReceiptsDTO potentialDuplicateReceiptsDTO = new PotentialDuplicateReceiptsDTO();
            potentialDuplicateReceiptsDTO.setUid(potentialDuplicateReceipts.getUid());
            potentialDuplicateReceiptsDTO.setFlags(potentialDuplicateReceipts.getFlags().stream().map(flags -> flags.getName()).collect(Collectors.toList()));
            listOfPotentielDuplicateReceiptsDTO.add(potentialDuplicateReceiptsDTO);
        }
        return listOfPotentielDuplicateReceiptsDTO;
    }

    public ProductResponseAiToCompareDTO mapProductToProductResponseAI(Product product) {
        ProductResponseAiToCompareDTO produitResponseAI = new ProductResponseAiToCompareDTO();
        produitResponseAI.setPrice(product.getUnitPrice().toString());
        produitResponseAI.setLibel(product.getRawLabel());
        produitResponseAI.setQuantity(product.getQuantity().toString());
        return produitResponseAI;
    }


    public ImageResponseToCompareDTO mapResponseToImageComparable(ResponseAI responseAI, String idOperation,
                                                                  List<ProduitResponseAI> productResponseAIs) {
        ImageResponseToCompareDTO imageResponseToCompareDTO = new ImageResponseToCompareDTO();
        List<ProductResponseDTO> products = new ArrayList<>();
        imageResponseToCompareDTO.setCity(responseAI.getVille());
        imageResponseToCompareDTO.setAddress(responseAI.getAddress());
        imageResponseToCompareDTO.setBuyDate(responseAI.getDate());
        imageResponseToCompareDTO.setBuyHour(responseAI.getHeure());
        imageResponseToCompareDTO.setSign(responseAI.getName());
        if (responseAI.getQuantity() != null) {
            imageResponseToCompareDTO.setArticleCount(responseAI.getQuantity());
        } else {
            imageResponseToCompareDTO.setArticleCount("null");
        }

        imageResponseToCompareDTO.setTotal(responseAI.getMontant_totale());
        imageResponseToCompareDTO.setTotalMax(responseAI.getMontant_paye());
        imageResponseToCompareDTO.setProcessReport(responseAI.getProcessReport());
        for (ProduitResponseAI product : productResponseAIs) {
            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            productResponseDTO.setProductName(product.getLibelle());
            productResponseDTO.setProductPrice(product.getPrix());
            productResponseDTO.setProductQuantity(product.getQuantity());
            products.add(productResponseDTO);
        }
        imageResponseToCompareDTO.setProducts(products);
        return imageResponseToCompareDTO;
    }

    public ImageResponseToCompareDTO mapImageToImageComparable(Image image, List<Product> productOp) {
        ImageResponseToCompareDTO imageResponseToCompareDTO = new ImageResponseToCompareDTO();
        List<ProductResponseDTO> products = new ArrayList<>();
        imageResponseToCompareDTO.setImageUrl(image.getImageUrl());
        imageResponseToCompareDTO.setCity(image.getCity());
        imageResponseToCompareDTO.setAddress(image.getAddress());
        imageResponseToCompareDTO.setLocale(image.getLocale());
        imageResponseToCompareDTO.setPhone(image.getPhone());
        imageResponseToCompareDTO.setBuyDate(image.getBuyDate());
        imageResponseToCompareDTO.setBuyHour(image.getBuyHour());
        imageResponseToCompareDTO.setName(image.getName());
        imageResponseToCompareDTO.setSign(image.getSign());
        if (image.getArticleCount() != null) {
            imageResponseToCompareDTO.setArticleCount(image.getArticleCount().toString());
        } else {
            imageResponseToCompareDTO.setArticleCount("null");
        }

        imageResponseToCompareDTO.setTotal(image.getTotal() == null ? "null" : image.getTotal().toString());
        imageResponseToCompareDTO.setTotalMax(image.getTotalMax() == null ? "null" : image.getTotalMax().toString());
        imageResponseToCompareDTO.setDominantLanguage(image.getDominantLanguage());
        imageResponseToCompareDTO.setCampaignUid(image.getCampaignUid());
        imageResponseToCompareDTO.setEndpointUid(image.getEndpointUid());
        imageResponseToCompareDTO.setGeoLocation(image.getGeoLocation());
        imageResponseToCompareDTO.setStatus(image.getStatus());
        imageResponseToCompareDTO.setPostalCode(image.getPostalCode());
        imageResponseToCompareDTO.setProcessReport(image.getProcessReport());
        for (Product product : productOp) {
            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            productResponseDTO.setProductName(product.getRawLabel());
            productResponseDTO.setProductPrice(product.getPrice() == null ? "null" : product.getPrice().toString());
            productResponseDTO.setProductQuantity(product.getQuantity() == null ? "null" : product.getQuantity().toString());
            productResponseDTO.setProductBrand(product.getBrand());
            productResponseDTO.setProductCategory(product.getCategory());
            products.add(productResponseDTO);
        }
        imageResponseToCompareDTO.setProducts(products);
        return imageResponseToCompareDTO;

    }
}
