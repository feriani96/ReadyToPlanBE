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
    public ResponseEntity<byte[]> exportPresentation(
        @PathVariable String companyName,
        @RequestParam(defaultValue = "PDF") ExportFormat format) throws IOException {

        byte[] content = businessPlanService.exportPresentation(companyName, format);

        String filename = "presentation." + format.toString().toLowerCase();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(content);
    }

}
