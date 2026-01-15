package com.example.HCOData.service.document;

import com.example.HCOData.constant.Patterns;
import com.example.HCOData.response.NewAIResponseDTO;
import com.example.HCOData.enums.DocumentStatusEnum;
import com.example.HCOData.enums.ToModerationFlagsEnum;
import com.example.HCOData.model.Image;
import com.example.HCOData.model.NewBrand;
import com.example.HCOData.model.NewCategory;
import com.example.HCOData.model.ParametresPassageToModeration;
import com.example.HCOData.model.Product;
import com.example.HCOData.model.ProduitResponseAI;
import com.example.HCOData.model.ResponseAI;
import com.example.HCOData.repository.ParametersPassageModerationRepository;
import com.example.HCOData.utils.ImageUtil;
import com.example.HCOData.validators.AddressCityValidator;
import com.example.HCOData.validators.DateTimeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ProcessProductServiceImpl implements ProcessProductService {

    @Autowired
    private ParametersPassageModerationRepository parametersPassageModerationRepository;

    @Autowired
    private BrandCategoryService brandCategoryService;

    @Override
    public void addProductToImage(Image imageToSave, NewAIResponseDTO newAIResponseDTO, List<NewBrand> brands, List<NewCategory> categories, Set<String> moderationFlagsList) {

        ParametresPassageToModeration passageToModeration = parametersPassageModerationRepository.findByOperationUid(imageToSave.getOperations().getUid());
        String productsString = newAIResponseDTO.getProduits();
        List<Product> products = new ArrayList<>();
        log.info("products : {}", productsString);
        if (productsString == null) {
            imageToSave.setProducts(products);
            return;
        }
        String[] allProductsInformation = productsString.split("\\[TS]");
        String[] productNames = allProductsInformation[0].split("\\|");
        String[] productPrices = allProductsInformation.length > 1 ? allProductsInformation[1].split("\\|") : new String[0];
        String[] productQuantities = allProductsInformation.length > 2 ? allProductsInformation[2].split("\\|") : new String[0];
        if (passageToModeration != null && passageToModeration.isCoherenceLibQMontant()) {
            if (productNames.length != productPrices.length || productQuantities.length != productPrices.length) {
                moderationFlagsList.add(ToModerationFlagsEnum.COHERENCLIBQMONTANT.getValeur());
                imageToSave.setStatusModeration(DocumentStatusEnum.PROCESSING.getValue());
            }
        }
        for (int i = 0; i < productNames.length; i++) {
            Product product = new Product();
            String productName = productNames[i].trim();
            product.setRawLabel(ImageUtil.convertStringFromAccToReg(productName));
            product.setShortLabel(ImageUtil.convertStringFromAccToReg(productName));

            if (i < productPrices.length && DateTimeValidator.matches(productPrices[i], "\\d+\\.\\d+")) {
                BigDecimal price = new BigDecimal(Patterns.DECIMAL_FORMAT.format(Double.parseDouble(AddressCityValidator.getStringFromStringRegex(productPrices[i], "\\d+\\.\\d+"))).replace(",", "."));
                product.setPrice(price);
                product.setUnitPrice(price);
            } else {
                product.setPrice(BigDecimal.ZERO);
                product.setUnitPrice(BigDecimal.ZERO);
            }
            if (i < productQuantities.length && DateTimeValidator.matches(productQuantities[i], "[0-9]+")) {
                product.setQuantity(Long.parseLong(AddressCityValidator.getStringFromStringRegex(productQuantities[i], "[0-9]+")));
            } else {
                product.setQuantity(0L);
            }
            brandCategoryService.setProductCategoryAndBrand(product, moderationFlagsList);
            product.setImage(imageToSave);
            products.add(product);
        }
        log.info("product size : {}", products.size());
        imageToSave.setProducts(products);
    }

    @Override
    public ResponseAI mapProductResponse(ResponseAI responseAI, NewAIResponseDTO newAIResponseDTO) {

        List<ProduitResponseAI> productResponse = new ArrayList<>();
        String productResponseString = newAIResponseDTO.getProduits();
        if (productResponseString == null) {
            responseAI.setProduitResponseAIList(productResponse);
            return responseAI;
        }
        String[] allProductsInformation = productResponseString.split("\\[TS]");
        String[] productNames = allProductsInformation[0].split("\\|");
        String[] productPrices = allProductsInformation.length > 1 ? allProductsInformation[1].split("\\|") : new String[0];
        String[] productQuantities = allProductsInformation.length > 2 ? allProductsInformation[2].split("\\|") : new String[0];
        for (int i = 0; i < productNames.length; i++) {
            ProduitResponseAI product = new ProduitResponseAI();
            product.setLibelle(productNames[i].trim());
            product.setPrix(i < productPrices.length && productPrices[i] != null ? productPrices[i] : "0");
            product.setQuantity(i < productQuantities.length && productQuantities[i] != null ? productQuantities[i] : "0");
            product.setResponseAI(responseAI);
            productResponse.add(product);
        }
        responseAI.setProduitResponseAIList(productResponse);
        return responseAI;

    }
}
