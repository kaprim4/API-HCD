package com.example.HCOData.mappers;

import com.example.HCOData.response.ResponseExistsDocumentDTO;
import com.example.HCOData.model.Image;

public class DocumentMapper {

    public static ResponseExistsDocumentDTO mapImageWhoHaveSameShopSameMoment(Image image) {
        ResponseExistsDocumentDTO imageWhoHaveSameShopSameMoment = new ResponseExistsDocumentDTO();
        imageWhoHaveSameShopSameMoment.setDate(image.getBuyDate());
        imageWhoHaveSameShopSameMoment.setCity(image.getCity());
        imageWhoHaveSameShopSameMoment.setSign(image.getSign());
        imageWhoHaveSameShopSameMoment.setOperation(image.getOperations().getUid());
        imageWhoHaveSameShopSameMoment.setHour(image.getBuyHour());
        imageWhoHaveSameShopSameMoment.setUid(image.getUid());
        return imageWhoHaveSameShopSameMoment;
    }
    public static ResponseExistsDocumentDTO mapImageWithSameData(Image image) {
        ResponseExistsDocumentDTO imageWithSameData = new ResponseExistsDocumentDTO();
        imageWithSameData.setDate(image.getBuyDate());
        imageWithSameData.setCity(image.getCity());
        imageWithSameData.setAddress(image.getAddress());
        imageWithSameData.setTotal(image.getTotal());
        imageWithSameData.setTotalMax(image.getTotalMax());
        imageWithSameData.setArticleCount(image.getArticleCount());
        imageWithSameData.setMd5(image.getImageMD5());
        imageWithSameData.setSign(image.getSign());
        imageWithSameData.setOperation(image.getOperations().getUid());
        imageWithSameData.setHour(image.getBuyHour());
        imageWithSameData.setUid(image.getUid());
        return imageWithSameData;
    }
}
