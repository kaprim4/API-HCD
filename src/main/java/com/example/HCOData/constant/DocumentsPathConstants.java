package com.example.HCOData.constant;

import com.example.HCOData.utils.ImageUtil;

public class DocumentsPathConstants {

    public static final String DOCUMENTS_SECOND_DESTINATION_WINDOWS = "C:/Users/CEKA/Desktop/DOCUMENT PERSO/2-DOCKER_FOLDER/1-DOCUMENT/2-DOCUMENT_ARCHIVE";
    public static final String DOCUMENTS_SECOND_DESTINATION_LINUX = "/home/seconde_destination_documents";

    public static final String DOCUMENTS_FIRST_DESTINATION_WINDOWS = "C:/Users/CEKA/Desktop/DOCUMENT PERSO/2-DOCKER_FOLDER/1-DOCUMENT/1-DOCUMENT_STORAGE";
    public static final String DOCUMENTS_FIRST_DESTINATION_LINUX = "/home/destination_documents";

    public static final String DOCUMENTS_NAS_WINDOWS = "Z:/Data Modération/Images deja traiter/SECONDE_FOLDER";
    public static final String DOCUMENTS_NAS_LINUX = "/home/MNT_NAS";

    public static final String FIRST_DESTINATION = ImageUtil.getValidPath(DOCUMENTS_FIRST_DESTINATION_WINDOWS, DOCUMENTS_FIRST_DESTINATION_LINUX);
    public static final String SECOND_DESTINATION = ImageUtil.getValidPath(DOCUMENTS_SECOND_DESTINATION_WINDOWS, DOCUMENTS_SECOND_DESTINATION_LINUX);
    public static final String NAS_DESTINATION = ImageUtil.getValidPath(DOCUMENTS_NAS_WINDOWS, DOCUMENTS_NAS_LINUX);
}
