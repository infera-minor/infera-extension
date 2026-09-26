package com.infera.extension.controller;

import com.infera.extension.dto.ResearchRequest;
import com.infera.extension.service.ResearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/research")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
/** Exposes browser-extension research operations over HTTP. */
public class ResearchController {

    private final ResearchService service;

    @PostMapping("/process")
    /**
     * Processes selected page content and returns the generated text.
     *
     * @param request text and requested operation supplied by the extension
     * @return generated Gemini text
     */
    public ResponseEntity<String> processContent(@RequestBody ResearchRequest request){
        String result = service.processRequest(request);
        return ResponseEntity.ok(result);
    }


}
