package com.example.HCOData.service.document;

import com.example.HCOData.model.NewBrand;
import com.example.HCOData.model.NewCategory;
import com.example.HCOData.model.Product;
import com.example.HCOData.model.RechercheBrand;
import com.example.HCOData.model.RechercheCategory;
import com.example.HCOData.response.RechercheBrandCategoryDTO;
import com.example.HCOData.enums.ToModerationFlagsEnum;

import com.example.HCOData.repository.NewBrandJPARepository;
import com.example.HCOData.repository.NewCategoryJPARepository;
import com.example.HCOData.repository.NewCategoryRepository;
import com.example.HCOData.repository.RechercheBrandJPARepository;
import com.example.HCOData.repository.RechercheCategoryJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class BrandCategoryServiceImpl implements BrandCategoryService{

    @Autowired
    private NewBrandJPARepository newBrandJPARepository;

    @Autowired
    private NewCategoryJPARepository newCategoryJPARepository;

    @Autowired
    private RechercheCategoryJPARepository rechercheCategoryJPARepository;

    @Autowired
    private RechercheBrandJPARepository rechercheBrandJPARepository;

    @Autowired
    private NewCategoryRepository newCategoryRepository;

    @Autowired
    private NewBrandJPARepository newBrandRepository;

    
    
    public  void brandCtegoryProcces(Product product) {
   	 List<NewBrand> newBrands = newBrandJPARepository.findAll();
   	 List<NewCategory> newCategorys = newCategoryJPARepository.findAll();
   	 
   	 for(NewBrand brand:newBrands) {
   		 if(product.getRawLabel().contains(brand.getBrand_name())) {
   			product.setBrand(brand.getBrand_name());
   			product.setBrandUid(brand.getBrand_uid());
   		 }else {
   			product.setBrand("NULL > NULL");
   			product.setBrandUid("45111afb-ac3c-440b-becd-2464e5796674");
   		 }
   		 
   	 }
   	for(NewCategory caegory:newCategorys) {
  		 if(product.getRawLabel().contains(caegory.getCategory_name())) {
  			product.setCategory(caegory.getCategory_name());
  			product.setCategoryUid(caegory.getCategory_uid());
  		 }else {
  			product.setCategory("");
  			product.setCategoryUid("");
  		 }
  		 
  	 }

	}
    @Override
    @Transactional
    public void setProductCategoryAndBrand(Product product,Set<String> moderationFlagsList) {

        List<RechercheBrand> rechercheBrand = rechercheBrandJPARepository.findAllRechercheBrand();
        Map<Integer,List<RechercheBrand>> hashMapRechercheBrand = getRechercheBrandMap(rechercheBrand);
        RechercheBrandCategoryDTO.RechercheBrandDTO rechercheBrandDTO = findBrandProduct(hashMapRechercheBrand,product);
        if(rechercheBrandDTO.isDuplicate()){
        	Optional<NewBrand> optionalObject = newBrandJPARepository.findNewBrand(rechercheBrandDTO.getRechercheBrand().getBrand_uid());     	 
        	String uidBrand;
        	if (optionalObject.isPresent()) {       	
        	 uidBrand = optionalObject.get().getBrand_uid();
        	}else {
        		uidBrand="45111afb-ac3c-440b-becd-2464e5796674";
        	}
            RechercheBrand rechercheBrandFirstElement = rechercheBrandDTO.getRechercheBrand();
            String categoryUid = rechercheBrandFirstElement.getCategory_uid();
            product.setCategoryUid(categoryUid);
            product.setBrandUid(uidBrand);
            Optional<NewBrand> newBrandOptional = newBrandJPARepository.findByBrandUid(uidBrand);
            Optional<NewCategory> newCategoryOptional = newCategoryJPARepository.findByCategoryUid(categoryUid);
            if(newBrandOptional.isPresent()){
                NewBrand newBrand = newBrandOptional.get();
                product.setBrand(newBrand.getBrand_name());
            }else {
                product.setBrand("");

            }

            if(newCategoryOptional.isPresent()){
                NewCategory newCategory = newCategoryOptional.get();
                product.setCategory(newCategory.getCategory_name());
            }
            else{
                product.setCategory("");
            }
        }
        else{
            List<RechercheCategory> rechercheCategory = rechercheCategoryJPARepository.findAllRechercheCategory();
            Map<Integer,List<RechercheCategory>> hashMapRechercheCategory = getRechercheCategoryMap(rechercheCategory);
            RechercheBrandCategoryDTO.RechercheCategoryDTO rechercheCategoryDTO = findCategoryProduct(hashMapRechercheCategory,product);
            if(rechercheCategoryDTO.isDuplicate()){
                RechercheCategory rechercheCategoryFirstElement = rechercheCategoryDTO.getRechercheCategory();
                String brandUid = rechercheCategoryFirstElement.getBrand_uid();
                String categoryUid = rechercheCategoryFirstElement.getCategory_uid();
                product.setCategoryUid(categoryUid);
                Optional<NewBrand> optionalObject = newBrandJPARepository.findNewBrand(brandUid);
                String uidBrand;
            	if (optionalObject.isPresent()) {       	
            	 uidBrand = optionalObject.get().getBrand_uid();
            	}else {
            		uidBrand="45111afb-ac3c-440b-becd-2464e5796674";
            	}
                product.setBrandUid(uidBrand);
                Optional<NewBrand> newBrandOptional = newBrandJPARepository.findByBrandUid(uidBrand);//Bug
                Optional<NewCategory> newCategoryOptional = newCategoryJPARepository.findByCategoryUid(categoryUid);
                if(newBrandOptional.isPresent()){
                    NewBrand newBrand = newBrandOptional.get();
                    product.setBrand(newBrand.getBrand_name());
                }else {
                    product.setBrand("");
                }

                if(newCategoryOptional.isPresent()){
                    NewCategory newCategory = newCategoryOptional.get();
                    product.setCategory(newCategory.getCategory_name());
                }
                else{
                    product.setCategory("");
                }

            }
            else{
                moderationFlagsList.add(ToModerationFlagsEnum.PRODUITOFFRESANSCATMARQUE.getValeur());
                product.setCategoryUid("");
                product.setBrandUid("45111afb-ac3c-440b-becd-2464e5796674");
                product.setCategory("");
                product.setBrand("NULL > NULL");
            }
        }


    }

    @Override
    @Transactional
    public List<NewBrand> getAllBrand() {
        return newBrandRepository.findAll();
    }

    @Override
    @Transactional
    public List<NewCategory> getAllCategory() {
        return newCategoryRepository.findAll();
    }

    public Map<Integer,List<RechercheBrand>> getRechercheBrandMap(List<RechercheBrand> rechercheBrandList){
        Map<Integer,List<RechercheBrand>> hashMapRechercheBrand = new HashMap<>();
        for(RechercheBrand rechercheBrand : rechercheBrandList){
            if(hashMapRechercheBrand.containsKey(rechercheBrand.getTreatmentOrder())){
                List<RechercheBrand> libelBrandList = hashMapRechercheBrand.get(rechercheBrand.getTreatmentOrder());
                if(libelBrandList.get(0).getLabel().length()<rechercheBrand.getLabel().length()){
                    libelBrandList.add(0,rechercheBrand);
                }else{
                    libelBrandList.add(rechercheBrand);
                }

                hashMapRechercheBrand.put(rechercheBrand.getTreatmentOrder(),libelBrandList);
            }else{          
                List<RechercheBrand> labelBrandList = new ArrayList<>();
                labelBrandList.add(rechercheBrand);
                hashMapRechercheBrand.put(rechercheBrand.getTreatmentOrder(),labelBrandList);
            }
        }
        return hashMapRechercheBrand;
    }

    public Map<Integer,List<RechercheCategory>> getRechercheCategoryMap(List<RechercheCategory> rechercheCategoryList){
        Map<Integer,List<RechercheCategory>> hashMapRechercheCategory = new HashMap<>();
        for(RechercheCategory rechercheCategory : rechercheCategoryList){
            if(hashMapRechercheCategory.containsKey(rechercheCategory.getTreatmentOrder())){
                List<RechercheCategory> libelBrandList = hashMapRechercheCategory.get(rechercheCategory.getTreatmentOrder());
                if(libelBrandList.get(0).getLabel().length()<rechercheCategory.getLabel().length()){
                    libelBrandList.add(0,rechercheCategory);
                }
                else{
                    libelBrandList.add(rechercheCategory);
                }
                hashMapRechercheCategory.put(rechercheCategory.getTreatmentOrder(),libelBrandList);
            }
            else{
                List<RechercheCategory> labelBrandList = new ArrayList<>();
                labelBrandList.add(rechercheCategory);
                hashMapRechercheCategory.put(rechercheCategory.getTreatmentOrder(),labelBrandList);
            }
        }
        return hashMapRechercheCategory;
    }

    public RechercheBrandCategoryDTO.RechercheCategoryDTO findCategoryProduct(Map<Integer,List<RechercheCategory>> mapRechercheCategory, Product product){
        RechercheBrandCategoryDTO.RechercheCategoryDTO rechercheCategoryDTO = new RechercheBrandCategoryDTO.RechercheCategoryDTO();
        for(Map.Entry<Integer,List<RechercheCategory>> entry: mapRechercheCategory.entrySet()){
            if(setCategoryProduct(entry.getValue(),rechercheCategoryDTO,product).isDuplicate()){
                break;
            }
        }
        return rechercheCategoryDTO;
    }

    public RechercheBrandCategoryDTO.RechercheBrandDTO findBrandProduct(Map<Integer,List<RechercheBrand>> mapRechercheCategory, Product product){
        RechercheBrandCategoryDTO.RechercheBrandDTO rechercheBrandDTO = new RechercheBrandCategoryDTO.RechercheBrandDTO();
        for(Map.Entry<Integer,List<RechercheBrand>> entry: mapRechercheCategory.entrySet()){
            if(setBrandProduct(entry.getValue(),rechercheBrandDTO,product).isDuplicate()){
                break;
            }
        }
        return rechercheBrandDTO;
    }

    public RechercheBrandCategoryDTO.RechercheBrandDTO setBrandProduct(List<RechercheBrand> rechercheCategories, RechercheBrandCategoryDTO.RechercheBrandDTO rechercheBrandDTO, Product product){
        for(RechercheBrand rechercheBrandElement:rechercheCategories){
                String brandLabel = rechercheBrandElement.getLabel();
            if(checkIfLabelCategoryAndLabelProductMatch(brandLabel,product.getRawLabel())){
                rechercheBrandDTO.setRechercheBrand(rechercheBrandElement);
                rechercheBrandDTO.setDuplicate(true);
                break;
            }

        }
        return rechercheBrandDTO;
    }



    public RechercheBrandCategoryDTO.RechercheCategoryDTO setCategoryProduct(List<RechercheCategory> rechercheCategories, RechercheBrandCategoryDTO.RechercheCategoryDTO rechercheCategoryDTO, Product product){
        for(RechercheCategory rechercheCategory:rechercheCategories){
            String categoryLabel = rechercheCategory.getLabel();
            if(checkIfLabelCategoryAndLabelProductMatch(categoryLabel,product.getShortLabel())){
                rechercheCategoryDTO.setRechercheCategory(rechercheCategory);
                rechercheCategoryDTO.setDuplicate(true);
                break;
            }

        }
        return rechercheCategoryDTO;
    }

    public boolean checkIfLabelCategoryAndLabelProductMatch(String categoryLabel,String labelProduct){
        if(labelProduct != null){
            String[] labelList = labelProduct.split(" ");
            if(categoryLabel.length()<4){
                if(labelProduct.contains(" "+categoryLabel+" ")){
                    return true;
                }
                if(labelList[0].equals(categoryLabel)){
                    return true;
                }
                if(labelList[labelList.length-1].equals(categoryLabel)){
                    return true;
                }
            }

            else{
                if(categoryLabel.split(" ").length==1) {
                        if (labelProduct.contains(" " + categoryLabel + " ")) {
                            return true;
                        }
                        if (labelList[0].equals(categoryLabel)) {
                            return true;
                        }
                        if (labelList[labelList.length - 1].equals(categoryLabel)) {
                            return true;
                        }

                }
                else{
                    if(labelProduct.contains(categoryLabel)){
                        return true;
                    }else{
                        checkPercentage(categoryLabel,labelProduct);
                    }
                }
            }
        }else{
            return false;
        }
        return false;

    }

    public boolean checkPercentage(String categoryLabel,String labelProduct){
        String[] categoryLabelList = categoryLabel.split(" ");
        String[] labelProductList = labelProduct.split(" ");
        List<String> intersectionListOfCategoryLabelAndLabel = new ArrayList<>();
        for(String categoryLabelElement:categoryLabelList){
            if(Arrays.asList(labelProductList).contains(categoryLabelElement)){
                intersectionListOfCategoryLabelAndLabel.add(categoryLabelElement);
            }
        }
        if(categoryLabelList.length ==2){
            if(intersectionListOfCategoryLabelAndLabel.size() == labelProductList.length){
                return true;
            }
        }
        else {
            if((float)intersectionListOfCategoryLabelAndLabel.size()/ labelProductList.length>= 0.8f || intersectionListOfCategoryLabelAndLabel.size() == labelProductList.length-1){
                return true;
            }
            else{
                return false;
            }
        }

        return false;
    }

    }





