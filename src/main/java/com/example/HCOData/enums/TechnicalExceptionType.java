package com.example.HCOData.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum TechnicalExceptionType {

    ACCESS_DENIED_EX("20002", HttpStatus.INTERNAL_SERVER_ERROR,"access denied to database"),

    DATABASE_ERROR("20003", HttpStatus.INTERNAL_SERVER_ERROR,"access denied to database"),

    HOURLY_ERROR("20004", HttpStatus.INTERNAL_SERVER_ERROR,"une des période n'est pas remplise"),

    COUNTRY_DOES_NOT_EXIST("20005", HttpStatus.INTERNAL_SERVER_ERROR,"country does not exist"),

    PERSONNEL_DOES_NOT_EXIST("20006", HttpStatus.INTERNAL_SERVER_ERROR,"utilisateur non trouvée"),

    ROLE_DOES_NOT_EXIST("20008", HttpStatus.INTERNAL_SERVER_ERROR,"role does not exist"),

    PA_DOES_NOT_EXIST("20009", HttpStatus.BAD_REQUEST,"image does not exist"),

    MONTANT_TOTAL_PA("20010",HttpStatus.BAD_REQUEST,"vous avez mal renseigner le montant total de la PA"),

    MONTANT_PAYE("20011",HttpStatus.BAD_REQUEST,"vous avez mal renseigner le montant payé"),

    ARTICLE_COUNT("20012",HttpStatus.BAD_REQUEST,"vous avez mal reseigner le nombre d'article"),

    PRIX_PRODUCT("20013",HttpStatus.BAD_REQUEST,"vous avez mal reseigner le prix d'un produit"),

    QUANTITY_PRODUCT("20014",HttpStatus.BAD_REQUEST,"vous avez mal renseigner la quantity d'un produit"),

    BUY_DATE_FORMAT("20015",HttpStatus.BAD_REQUEST,"vous avez mal renseigner la date d'achat"),

    BUY_HOUR_FORMAT("20016",HttpStatus.BAD_REQUEST,"vous avez mal renseigner l'heure d'achat"),

    IMAGE_DEJA_EN_MODERATION("20017",HttpStatus.BAD_REQUEST,"cette PA est déja pris par un autre utilisateur, merci de rafraichir la page d'acceuil"),

    TIME_FORMAT_ERROR("20007", HttpStatus.INTERNAL_SERVER_ERROR,"time format IS NOT supported");

    @Getter
    private String message;
    @Getter
    private HttpStatus status;
    @Getter
    private String code;

     TechnicalExceptionType(String code, HttpStatus status, String message) {
        this.message = message;
        this.code=code;
        this.status=status;
    }
}
