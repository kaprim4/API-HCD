package com.example.HCOData.service.businessrules;


import com.example.HCOData.constant.MessageConstants;
import com.example.HCOData.constant.Patterns;
import com.example.HCOData.enums.DocumentStatusEnum;
import com.example.HCOData.enums.ExistFileFlagsEnum;
import com.example.HCOData.enums.OperationEnums;
import com.example.HCOData.enums.ToModerationFlagsEnum;
import com.example.HCOData.mappers.AiMapper;
import com.example.HCOData.mappers.ImageMapperInterface;
import com.example.HCOData.model.BareCodes;
import com.example.HCOData.model.Image;
import com.example.HCOData.model.MotifModeration;
import com.example.HCOData.model.Operation;
import com.example.HCOData.model.OperationTerms;
import com.example.HCOData.model.ParametresPassageToModeration;
import com.example.HCOData.model.Product;
import com.example.HCOData.model.ResponseAI;
import com.example.HCOData.repository.ImageJPARepository;
import com.example.HCOData.repository.ImageResponseAiRepository;
import com.example.HCOData.repository.MotifModerationRepository;
import com.example.HCOData.repository.OperationTermsRepository;
import com.example.HCOData.repository.ParametersPassageModerationRepository;
import com.example.HCOData.request.ModerationContextDTO;
import com.example.HCOData.request.ResponseApiImageDTO;
import com.example.HCOData.response.AnomalyReport;
import com.example.HCOData.response.HCDResponseDTO;
import com.example.HCOData.response.ImageResponseDTO;
import com.example.HCOData.response.ItemRuleDTO;
import com.example.HCOData.response.NewAIResponseDTO;
import com.example.HCOData.response.ProcessedRules;
import com.example.HCOData.service.document.BrandCategoryService;
import com.example.HCOData.service.document.ProcessProductService;
import com.example.HCOData.utils.ImageUtil;
import com.example.HCOData.validators.AddressCityValidator;
import com.example.HCOData.validators.DateTimeValidator;
import com.example.HCOData.validators.PriceValidator;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class BusinessRulesServiceImpl implements BusinessRulesService {


    private final AiMapper aiMapper;


    private final ImageJPARepository imageJPARepository;


    private final ProcessProductService processProductService;


    private final BrandCategoryService brandCategoryService;


    private final ParametersPassageModerationRepository parametersPassageModerationRepository;


    private final ImageMapperInterface imageMapperInterface;


    private final OperationTermsRepository operationTermsRepository;


    private final MotifModerationRepository motifModerationRepository;


    private final ImageResponseAiRepository imageResponseAiRepository;

    public BusinessRulesServiceImpl(AiMapper aiMapper, ImageJPARepository imageJPARepository, ProcessProductService processProductService, BrandCategoryService brandCategoryService, ParametersPassageModerationRepository parametersPassageModerationRepository, ImageMapperInterface imageMapperInterface, OperationTermsRepository operationTermsRepository, MotifModerationRepository motifModerationRepository, ImageResponseAiRepository imageResponseAiRepository) {
        this.aiMapper = aiMapper;
        this.imageJPARepository = imageJPARepository;
        this.processProductService = processProductService;
        this.brandCategoryService = brandCategoryService;
        this.parametersPassageModerationRepository = parametersPassageModerationRepository;
        this.imageMapperInterface = imageMapperInterface;
        this.operationTermsRepository = operationTermsRepository;
        this.motifModerationRepository = motifModerationRepository;
        this.imageResponseAiRepository = imageResponseAiRepository;
    }

    private void saveImageWithBadQuality(Operation operation, Image imageToSave){
        if(isOperationTest(operation)){
            markAsProcessed(imageToSave);
        }
        else{
            markAsBadQuality(imageToSave);
        }
        imageToSave.setProcessReport(MessageConstants.BAD_QUALITY_IMAGE);
        imageJPARepository.save(imageToSave);
    }

    private void markAsBadQuality(Image imageToSave){
        imageToSave.setStatusModeration(DocumentStatusEnum.PROCESSING.getValue());
        imageToSave.setStatus(DocumentStatusEnum.NON_EXPLOITABLE.getMessage());
    }

    private boolean isOperationTest(Operation operation) {

        boolean isTestOperation = operation.getUid() == OperationEnums.OPERATION_TEST_HIGHCO.getValue()
                || operation.getUid().equals(OperationEnums.OPERATION_TEST_PROCHECK)
                || operation.getUid().contains("BOX");

       return isTestOperation;
    }

    @Override
    public Image moderationProcessor(String responseApi, String uid, ResponseApiImageDTO responseApiImageDTO, File imageFile, Operation operation, HCDResponseDTO hcdResponseDTO) {
        Set<String> moderationFlagsList = new HashSet<>();
        NewAIResponseDTO newAIResponseDTO = getNewAIResponseObject(responseApi);
        ResponseAI responseAI = aiMapper.mapResponseAiToResponseAIObject(newAIResponseDTO);
        responseAI.setProcessReport(MessageConstants.NO_ERROR);
        responseAI.setOperations(operation);
        responseAI.setDateReception(LocalDateTime.now());
        Image imageToSave = imageJPARepository.findByUid(uid);
        String ean = newAIResponseDTO.getEan();
        if (newAIResponseDTO != null && newAIResponseDTO.getProcessReport() != null) {
            saveImageWithBadQuality(operation, imageToSave);
        } else {
            log.info(" RESPONSE IA OK SO IMAGE TO START PROCESSING ");
            processProductService.addProductToImage(imageToSave, newAIResponseDTO, brandCategoryService.getAllBrand(), brandCategoryService.getAllCategory(), moderationFlagsList);
            imageToSave.setTotal(
                    new BigDecimal(
                            Patterns.DECIMAL_FORMAT.format(PriceValidator.parseToBigDecimal(newAIResponseDTO.getMontant_paye())).replace(",", ".")
                    )
            );
            imageToSave.setTotalMax(
                    new BigDecimal(
                            Patterns.DECIMAL_FORMAT.format(PriceValidator.parseToBigDecimal(newAIResponseDTO.getMontant_totale())).replace(",", ".")
                    )
            );
            ImageUtil.generateMD5(imageFile, imageToSave);
            populateImageDetails(newAIResponseDTO, imageToSave, imageFile, ean, operation, moderationFlagsList, responseApiImageDTO, responseAI);

        }
        return imageToSave;
    }

    private void populateImageDetails(NewAIResponseDTO newAIResponseDTO, Image imageToSave, File imageFile, String ean, Operation operation, Set<String> moderationFlagsList, ResponseApiImageDTO responseApiImageDTO, ResponseAI responseAI) {
        String eurPA = (newAIResponseDTO.getHeure() != null) ? newAIResponseDTO.getHeure().trim() : null;
        String hourResult = DateTimeValidator.convertHour(eurPA);
        String datePA = newAIResponseDTO.getDate().trim();
        imageToSave.setAddress(newAIResponseDTO.getAddress());
        BareCodes bareCodes = new BareCodes();
        imageToSave.setPayment(new ArrayList<>());
        List<BareCodes> listOfBareCodes = new ArrayList<>();
        bareCodes.setData(ean == null ? "0" : ean);
        bareCodes.setImage(imageToSave);
        listOfBareCodes.add(bareCodes);
        imageToSave.setProcessReport(MessageConstants.NO_ERROR);
        imageToSave.setBarecodes(listOfBareCodes);
        imageToSave.setCampaignUid(operation.getUid());
        imageToSave.setImageType(FilenameUtils.getExtension(imageFile.getName()));
        imageToSave.setBuyHour(hourResult.trim());
        imageToSave.setCity(AddressCityValidator.getStringFromStringRegex(newAIResponseDTO.getVille().trim(), Patterns.PATTERN_CITY) == null ? "" : AddressCityValidator.getStringFromStringRegex(newAIResponseDTO.getVille().trim(), Patterns.PATTERN_CITY));
        imageToSave.setPostalCode(AddressCityValidator.getStringFromStringRegex(newAIResponseDTO.getVille().trim(), Patterns.PATTERN_POSTAL_CODE) == null ? "" : AddressCityValidator.getStringFromStringRegex(newAIResponseDTO.getVille().trim(), Patterns.PATTERN_POSTAL_CODE));
        imageToSave.setSign(newAIResponseDTO.getName().trim());
        log.info("Quantity: {} ", newAIResponseDTO.getQuantity());
        String qty = Optional.ofNullable(newAIResponseDTO.getQuantity())
                .filter(q -> !q.equals("null") && !q.isEmpty())
                .orElse("0");
        try {
            imageToSave.setArticleCount(Long.parseLong(qty));
            log.info(" Quantity has been inserted ");
        } catch (NumberFormatException e) {
            log.warn("Invalid quantity format: {}", qty);
            imageToSave.setArticleCount(0L);
        }
        ParametresPassageToModeration passageToModeration = parametersPassageModerationRepository.findByOperationUid(operation.getUid());
        ModerationContextDTO context = buildModerationContext(newAIResponseDTO, imageToSave, operation, moderationFlagsList, responseApiImageDTO, responseAI, datePA, passageToModeration);
        processModerationRules(context);
    }

    private static ModerationContextDTO buildModerationContext(NewAIResponseDTO newAIResponseDTO, Image imageToSave, Operation operation, Set<String> moderationFlagsList, ResponseApiImageDTO responseApiImageDTO, ResponseAI responseAI, String datePA, ParametresPassageToModeration passageToModeration) {
        return ModerationContextDTO.builder()
                .operation(operation)
                .datePA(datePA)
                .moderationFlagsList(moderationFlagsList)
                .imageToSave(imageToSave)
                .newAIResponseDTO(newAIResponseDTO)
                .responseApiImageDTO(responseApiImageDTO)
                .responseAI(responseAI)
                .passageToModeration(passageToModeration)
                .build();
    }

    public void processModerationRules(ModerationContextDTO context) {
        if (context.getOperation().isPassDirectToModeration()) {
            processDirectModeration(context);
        }else{
            processModerationChecks(context);
        }

    }

    private void processDirectModeration(ModerationContextDTO context) {
        log.info(" Send to moderation ");
        Image imageToSave =  context.getImageToSave();
        ResponseAI responseIA = context.getResponseAI();
        ResponseApiImageDTO responseApiImageDTO = context.getResponseApiImageDTO();
        responseApiImageDTO.setImage(context.getImageToSave());
        String dateFormat = DateTimeValidator.validateDate(context.getDatePA());
        imageToSave.setBuyDate(dateFormat);
        imageToSave.setStatus(DocumentStatusEnum.PROCESSING.getMessage());
        imageToSave.setStatusModeration(DocumentStatusEnum.PROCESSING.getValue());
        imageJPARepository.save(imageToSave);
        responseIA.setUid(imageToSave.getUid());
        imageResponseAiRepository.save(context.getResponseAI());
    }

    private void processModerationChecks(ModerationContextDTO context) {
        log.info("Processing moderation checks");
        if (context.getPassageToModeration() == null) {
            markAsProcessed(context.getImageToSave());
            return;
        }
        performValidations(context);
        processImageResponse(context);
        processAnomalyReport(context);
        processProductTerms(context);
        finalizeImageStatus(context);
        saveImage(context);
    }

    private void saveImage(ModerationContextDTO context) {
        log.info("status : And operation is : {}, {} ", context.getImageToSave().getStatus(),context.getOperation().getUid());
        Image imageToSave = context.getImageToSave();
        ResponseAI responseAI = context.getResponseAI();
        responseAI.setUid(imageToSave.getUid());
        imageResponseAiRepository.save(responseAI);
        saveMotifs(context.getModerationFlagsList(), imageToSave.getId());
    }

    private void finalizeImageStatus(ModerationContextDTO context) {
        Image imageToSave = context.getImageToSave();
        Operation operation = context.getOperation();
        boolean isProcessed = imageToSave.getStatus() != DocumentStatusEnum.PROCESSING.getMessage()
                && imageToSave.getStatusModeration() != DocumentStatusEnum.PROCESSING.getValue();

        boolean isTestOperation = operation.getUid().equals(OperationEnums.OPERATION_TEST_HIGHCO.getValue())
                || operation.getUid().equals(OperationEnums.OPERATION_TEST_PROCHECK.getValue())
                || operation.getUid().contains("BOX");

        DocumentStatusEnum status = isProcessed || isTestOperation ?
                DocumentStatusEnum.PROCESSED : DocumentStatusEnum.PROCESSING;

        imageToSave.setStatus(status.getMessage());
        imageToSave.setStatusModeration(status.getValue());
    }

    private void processProductTerms(ModerationContextDTO context) {
        Operation operation = context.getOperation();
        if (operation != null) {
            log.warn("Operation is null, skipping product validation");
            return;
        }
        List<OperationTerms> operationTerms = operationTermsRepository.findByOperation_uid(operation.getUid());
        context.getImageToSave().getProducts()
                .forEach(product -> validateProduct(context, product, operationTerms));
    }

    private void validateProduct(ModerationContextDTO context, Product product, List<OperationTerms> operationTerms) {
        validatePriceAndQuantity(context, product);
        validateOperationTerms(product, operationTerms, context.getOperation());
    }

    private void validateOperationTerms(Product product, List<OperationTerms> operationTerms, Operation operation) {
        operationTerms.stream().filter(term -> product.getRawLabel().contains(term.getName()))
                .forEach(term -> validateProductPrice(product, operation));
    }

    private boolean hasInvalidPriceOrQuantity(Product product) {
        return product.getUnitPrice().equals(0L) || "0".equals(product.getQuantity());
    }
    private void validateProductPrice(Product product, Operation operation) {
        String offerMin = operation.getOffreMin();
        String offerMax = operation.getOffreMax();
        if (offerMin == null || offerMax == null) {
            log.debug("Skipping price validation - min/max offers not set");
            return;
        }
        if(!isPriceWithInBounds(product.getPrice(), offerMin, offerMax, operation.getOffre())){
            log.info("Product price outside allowed bounds: {}", product.getRawLabel());
        }
    }

    private boolean isPriceWithInBounds(BigDecimal price, String min, String max, String offer) {
        return PriceValidator.checkPriceBetweenMinMax(min, max, price, offer);
    }

    private void validatePriceAndQuantity(ModerationContextDTO context, Product product) {
        if (hasInvalidPriceOrQuantity(product)) {
            if(context.getPassageToModeration().isUnitPriceOrQuantity()){
                log.info("Invalid unit price or quantity detected for product: {}", product.getRawLabel());
                addModerationFlag(context, ToModerationFlagsEnum.UNITPRICEORQUANTITY);
            }else{
                log.info("Price validation skipped - unit price validation not enabled");
            }
        }
    }

    private void performValidations(ModerationContextDTO context) {
        ParametresPassageToModeration params = context.getPassageToModeration();
        checkBuyDateValidations(context, params);
        checkCoherenceValidations(context, params);
        checkShopValidations(context, params);
        checkPriceTotalValidations(context, params);
        checkPricePayedValidations(context, params);
    }

    private void processImageResponse(ModerationContextDTO context) {
        ImageResponseDTO imageResponseDTO = imageMapperInterface.mapPostImageToImageDTO(
                context.getImageToSave(),
                context.getOperation().getUid()
        );


        if (imageResponseDTO.getProcessedRules() != null) {
            processRules(context, imageResponseDTO.getProcessedRules());
        }
    }

    private void processRules(ModerationContextDTO context, ProcessedRules rules) {
        log.info("Processing rules: {}", rules);
        if (rules == null) {
            return;
        }
        ItemRuleDTO itemRule = rules.getItemRuleDTO();
        if (itemRule == null) {
            itemRule = new ItemRuleDTO();
            rules.setItemRuleDTO(itemRule);
        }
        ParametresPassageToModeration params = context.getPassageToModeration();
        if (params.isCoherenceLibelle() && (rules.getItemRuleDTO() == null) || !rules.getItemRuleDTO().isMatched()) {
            addModerationFlag(context, ToModerationFlagsEnum.COHERENCELIBELLE);
        }
        if (params.isCoherenceMontantTotal() && !rules.isItemsPriceMatched()) {
            addModerationFlag(context, ToModerationFlagsEnum.COHERENCEMONTANTTOTAL);
        }
        if (params.isCoherenceProduit() && !rules.isQuantityItemMatched()) {
            addModerationFlag(context, ToModerationFlagsEnum.COHERENCEPRODUIT);
        }
    }

    private void processAnomalyReport(ModerationContextDTO context) {
        if(context == null || context.getImageResponseDTO() == null){
            return;
        }
        else{
            AnomalyReport anomalyReport = context.getImageResponseDTO().getAnomalyReport();
            if (anomalyReport == null) {
                return;
            }
            checkAnomalyFlags(context, anomalyReport);
        }

    }

    private void checkAnomalyFlags(ModerationContextDTO context, AnomalyReport anomalyReport) {
        ParametresPassageToModeration params = context.getPassageToModeration();
        if (params.isHighFileSimilarity()) {
            if (anomalyReport.getFlags().contains(ExistFileFlagsEnum.IDENTICAL_EXISTING_FILE.name())) {
                addModerationFlag(context, ToModerationFlagsEnum.HIGHFILESIMILARITY);
            }
        }
        if (params.isSameShopMoment()) {
            if (anomalyReport.getFlags().contains(ExistFileFlagsEnum.SAME_SHOP_SAME_MOMENT.name())) {
                addModerationFlag(context, ToModerationFlagsEnum.SAMESHOPMOMENT);
            }
        }
        if (params.isExistingProductList()) {
            if (anomalyReport.getFlags().contains(ExistFileFlagsEnum.EXISTING_PRODUCT_LIST.name())) {
                addModerationFlag(context, ToModerationFlagsEnum.EXISTINGPRODUCTLIST);
            }
        }
    }


    private void checkPricePayedValidations(ModerationContextDTO context, ParametresPassageToModeration params) {
        if (params.isMontantPaye()) {
            if (context.getImageToSave().getTotal() == null || context.getImageToSave().getTotal().compareTo(new BigDecimal("0.0")) == 0 || context.getImageToSave().getTotal().compareTo(context.getImageToSave().getTotalMax()) > 0) {
                addModerationFlag(context, ToModerationFlagsEnum.MONTANTTOTAL);
            }
        }
    }

    private void checkPriceTotalValidations(ModerationContextDTO context, ParametresPassageToModeration params) {
        if (params.isMontantTotal()) {
            if (context.getImageToSave().getTotalMax() == null || context.getImageToSave().getTotalMax().compareTo(new BigDecimal("0.0")) == 0) {
                addModerationFlag(context, ToModerationFlagsEnum.MONTANTTOTAL);
            }
        }
    }

    private void checkShopValidations(ModerationContextDTO context, ParametresPassageToModeration params) {
        if (params.isShop()) {
            if (context.getImageToSave().getSign().isEmpty() || context.getImageToSave() == null) {
                addModerationFlag(context, ToModerationFlagsEnum.SHOP);
            }
        }
    }

    private void checkCoherenceValidations(ModerationContextDTO context, ParametresPassageToModeration params) {
        if (params.isCoherenceArticle()) {
            int quantityProduct = calculateNumberProducts(context.getImageToSave().getProducts());
            if (!String.valueOf(quantityProduct).equals(context.getNewAIResponseDTO().getQuantity())) {
                addModerationFlag(context, ToModerationFlagsEnum.COHERENCEARTICLE);
            }
        }
    }

    private void checkBuyDateValidations(ModerationContextDTO context, ParametresPassageToModeration params) {
        if (params.isBuyDate() && !DateTimeValidator.checkIsValidate(context.getDatePA())) {
            addModerationFlag(context, ToModerationFlagsEnum.BUYDATEFORMAT);
        }
        if (params.isBuyDate() && !DateTimeValidator.checkIsValidate(context.getDatePA())) {
            addModerationFlag(context, ToModerationFlagsEnum.BUYDATE);
        }
    }

    private void addModerationFlag(ModerationContextDTO context, ToModerationFlagsEnum toModerationFlagsEnum) {
        context.getModerationFlagsList().add(toModerationFlagsEnum.getValeur());
        markProcessing(context.getImageToSave());
    }

    private void markProcessing(Image imageToSave) {
        imageToSave.setStatusModeration(DocumentStatusEnum.PROCESSING.getValue());
        imageToSave.setStatus(DocumentStatusEnum.PROCESSING.getMessage());
    }

    private void markAsProcessed(Image imageToSave) {
        imageToSave.setStatusModeration(DocumentStatusEnum.PROCESSED.getValue());
        imageToSave.setStatus(DocumentStatusEnum.PROCESSED.getMessage());
    }

    private int calculateNumberProducts(List<Product> products) {
        return products.stream()
                .mapToInt(product -> product.getQuantity().intValue())
                .sum();
    }


    private NewAIResponseDTO getNewAIResponseObject(String responseApi) {
        Gson gson = new Gson();
        NewAIResponseDTO newAIResponseDTO = gson.fromJson(responseApi, NewAIResponseDTO.class);
        return newAIResponseDTO;
    }

    private void saveMotifs(Set<String> moderationFlagsList, Long imageId) {
        for (String motifName : moderationFlagsList) {
            MotifModeration motifModeration = new MotifModeration(motifName, imageId);
            motifModerationRepository.save(motifModeration);
        }
    }


}
