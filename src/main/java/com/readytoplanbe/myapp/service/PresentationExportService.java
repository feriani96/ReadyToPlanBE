package com.readytoplanbe.myapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PresentationExportService {

    private static final Logger log = LoggerFactory.getLogger(PresentationExportService.class);

    private final BusinessPlanGeneratorService generatorService;
    private final ObjectMapper objectMapper;

    @Value("${custom.application.export.temp-dir}")
    private String tempDirPath;

    public PresentationExportService(BusinessPlanGeneratorService generatorService, ObjectMapper objectMapper) {
        this.generatorService = generatorService;
        this.objectMapper = objectMapper;
    }

    // Méthode pour exporter en PDF
    public File exportToPdf(String companyName, String presentationContent) throws IOException {
        log.info("Exporting presentation to PDF for company: {}", companyName);

        // Convertir le markdown en HTML d'abord
        String htmlContent = convertMarkdownToHtml(presentationContent);

        // Créer un fichier temporaire
        File pdfFile = File.createTempFile("presentation-", ".pdf", new File(tempDirPath));

        // Utiliser Flying Saucer pour générer le PDF
        try (OutputStream os = new FileOutputStream(pdfFile)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);
        } catch (com.lowagie.text.DocumentException e) {
            throw new IOException("Failed to generate PDF", e);
        }

        return pdfFile;
    }

    // Méthode pour exporter en PPTX
    public File exportToPptx(String companyName, String presentationContent) throws IOException {
        log.info("Exporting presentation to PPTX for company: {}", companyName);

        // Convertir le contenu en slides PPTX
        File pptxFile = File.createTempFile("presentation-", ".pptx", new File(tempDirPath));

        try (XMLSlideShow ppt = new XMLSlideShow()) {
            // Diviser le contenu en slides
            String[] slides = presentationContent.split("===SLIDE===");

            for (String slideContent : slides) {
                if (slideContent.trim().isEmpty()) continue;

                XSLFSlide slide = ppt.createSlide();
                XSLFTextShape title = slide.createTextBox();
                title.setAnchor(new Rectangle(50, 50, 900, 50));

                // Extraire le titre si présent
                String titleText = extractTitle(slideContent);
                String bodyText = slideContent.replaceFirst("^#\\s+.*\\n", "").trim();

                title.setText(titleText);

                XSLFTextShape content = slide.createTextBox();
                content.setAnchor(new Rectangle(50, 120, 900, 400));
                content.setText(bodyText);
            }

            try (FileOutputStream out = new FileOutputStream(pptxFile)) {
                ppt.write(out);
            }
        }

        return pptxFile;
    }

    private String convertMarkdownToHtml(String markdown) {
        // Configuration du parser Flexmark
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        // Parsing du markdown
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    private String extractTitle(String slideContent) {
        // Extraire le premier titre H1
        Pattern pattern = Pattern.compile("^#\\s+(.*)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(slideContent);
        return matcher.find() ? matcher.group(1) : "Slide";
    }
}
