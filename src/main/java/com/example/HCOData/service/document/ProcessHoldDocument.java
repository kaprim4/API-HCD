package com.example.HCOData.service.document;


import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface ProcessHoldDocument {

	Map<String,String> restoreHoldDocuments();
	Map<String,String> ReprocessedDocuments(MultipartFile file);
	 void processPendingValidations();


}
