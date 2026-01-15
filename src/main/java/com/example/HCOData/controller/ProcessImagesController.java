package com.example.HCOData.controller;


import java.io.IOException;
import java.util.Map;

import com.example.HCOData.service.aitreatment.AITreatmentService;
import com.example.HCOData.validators.ClientRequestValidator;
import com.example.HCOData.service.document.ProcessDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.HCOData.response.ImageResponseDTO;
import com.example.HCOData.repository.ImageJPARepository;
import com.example.HCOData.service.document.ProcessHoldDocument;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@Slf4j
public class ProcessImagesController {

    @Autowired
    private ProcessHoldDocument processHoldDocument;

    @Autowired
    ClientRequestValidator clientRequestValidator;

    @Autowired
    ProcessDocumentService processDocumentService;

    @Autowired
    ImageJPARepository imageJPARepository;

    @PostMapping("/postImage")
    public ResponseEntity<?> postDocument(@RequestParam("body") String clientRequest, @RequestParam("file") MultipartFile image) throws IOException {
        return new ResponseEntity<>(processDocumentService.startProcess(clientRequest, image),HttpStatus.OK);
    }

    @GetMapping("/findImage/{uid}")
    public ResponseEntity<?> findDocumentByUid(@PathVariable("uid") String uid) {
        ImageResponseDTO imageResponseDTO = processDocumentService.findImageByUid(uid);
        return new ResponseEntity<>(imageResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/reprocessed-documents")
    public ResponseEntity<Map<String, String>> ReprocessedDocuments(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(processHoldDocument.ReprocessedDocuments(file), HttpStatus.OK);
    }

    @GetMapping("/moderation-restore-hold-documents")
    public ResponseEntity<Map<String, String>> restoreHoldDocuments() {
        return new ResponseEntity<>(processHoldDocument.restoreHoldDocuments(), HttpStatus.OK);
    }

}
