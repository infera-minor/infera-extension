package com.infera.extension.controller;

import com.infera.extension.dto.ResearchRequest;
import com.infera.extension.service.ResearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/research")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ResearchController {

    private final ResearchService service;

    public ResponseEntity<String> processContent(@RequestBody ResearchRequest request){
        String result = service.processRequest(request);
        return ResponseEntity.ok(result);
    }


}
