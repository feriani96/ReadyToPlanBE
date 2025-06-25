package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.domain.enumeration.ExportFormat;

import com.readytoplanbe.myapp.service.BusinessPlanService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class PresentationExportResource {

    private final BusinessPlanService businessPlanService;

    public PresentationExportResource(BusinessPlanService businessPlanService) {
        this.businessPlanService = businessPlanService;
    }

    @GetMapping("/business-plans/{companyName}/export")
    public ResponseEntity<org.springframework.core.io.Resource> exportPresentation(
        @PathVariable String companyName,
        @RequestParam(defaultValue = "PDF") ExportFormat format) throws IOException {

        org.springframework.core.io.Resource resource = businessPlanService.exportPresentation(companyName, format);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

}
