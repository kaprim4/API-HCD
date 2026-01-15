package com.example.HCOData.service.aitreatment;

import com.example.HCOData.request.IARequestDTO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class AIService {


    public String sendDocumentToApi(String imageBase64Encoded, String apiAiUrl) {
        log.info("Calling AI API: URL = {}", apiAiUrl);

        try {
            IARequestDTO clientRequest = new IARequestDTO();
            clientRequest.setImageByte(imageBase64Encoded);

            String json = new Gson().toJson(clientRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(apiAiUrl, entity, String.class);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("API call failed: Status code = {}, Response body = {}", e.getStatusCode(), e.getResponseBodyAsString());
            return "KO";
        } catch (ResourceAccessException e) {
            log.error("API call failed due to resource access issue: {}", e.getMessage());
            return "KO";
        } catch (Exception e) {
            log.error("Unexpected error during API call: {}", e.getMessage());
            return "KO";
        }
    }

//    public String sendDocumentToApi(String imageBase64Encoded, String apiAiUrl) {
//        String result = "KO";
//        try {
//            // Configure timeout settings
////            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
////            requestFactory.setConnectTimeout(2000);
////            requestFactory.setReadTimeout(2000);
//
//            // Prepare request
//            IARequestDTO clientRequest = new IARequestDTO();
//            clientRequest.setImageByte(imageBase64Encoded);
//
//            String json = new Gson().toJson(clientRequest);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> entity = new HttpEntity<>(json, headers);
//
//            // Create RestTemplate with timeout configuration
//            RestTemplate restTemplate = new RestTemplate();
//
//            result = restTemplate.postForObject(apiAiUrl, entity, String.class);
//
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//            log.error("API call failed: Status code = {}, Response body = {}",
//                    e.getStatusCode(), e.getResponseBodyAsString());
//        } catch (ResourceAccessException e) {
//            log.error("API call failed due to timeout or resource access issue: {}",
//                    e);
//        } catch (Exception e) {
//            log.error("Unexpected error during API call: {}", e);
//        }
//        return result;
//    }


}
