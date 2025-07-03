package com.readytoplanbe.myapp.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.readytoplanbe.myapp.domain.*;
import com.readytoplanbe.myapp.domain.enumeration.EntityType;
import com.readytoplanbe.myapp.repository.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.readytoplanbe.myapp.service.dto.AIResponseDTO;
import com.readytoplanbe.myapp.service.dto.BusinessPlanFinalDTO;
import com.readytoplanbe.myapp.web.rest.errors.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing {@link BusinessPlanFinal}.
 */
@Service
public class BusinessPlanFinalService {

    private final Logger log = LoggerFactory.getLogger(BusinessPlanFinalService.class);

    private final BusinessPlanFinalRepository businessPlanFinalRepository;
    private final ProductOrServiceRepository productRepository;
    private final TeamRepository teamRepository;
    private final MarketingRepository marketingRepository;
    private final AIGenerationService aiGenerationService;
    private  final CompanyRepository companyRepository;
    private final AIGenerationService aiService;
    private  final AIGeneratedResponseRepository aiGeneratedResponseRepository;

    public BusinessPlanFinalService(BusinessPlanFinalRepository businessPlanFinalRepository, ProductOrServiceRepository productRepository, TeamRepository teamRepository, MarketingRepository marketingRepository, AIGenerationService aiGenerationService, CompanyRepository companyRepository, AIGenerationService aiService, AIGeneratedResponseRepository aiGeneratedResponseRepository) {
        this.businessPlanFinalRepository = businessPlanFinalRepository;
        this.productRepository = productRepository;
        this.teamRepository = teamRepository;
        this.marketingRepository = marketingRepository;
        this.aiGenerationService = aiGenerationService;
        this.companyRepository = companyRepository;
        this.aiService = aiService;
        this.aiGeneratedResponseRepository = aiGeneratedResponseRepository;
    }
    public Optional<BusinessPlanFinal> getByCompanyId(String companyId) {
        return businessPlanFinalRepository.findByCompany_Id(companyId);
    }
    public BusinessPlanFinalDTO generateAIBusinessPlan(String companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

        List<AIResponseDTO> aiResponses = new ArrayList<>();

        // COMPANY
        aiGeneratedResponseRepository.findByEntityTypeAndEntityId(EntityType.COMPANY, company.getId())
            .ifPresentOrElse(
                ai -> aiResponses.add(new AIResponseDTO("COMPANY", ai.getAiResponse())),
                () -> aiResponses.add(new AIResponseDTO("COMPANY", "Aucune réponse IA trouvée."))
            );

        // PRODUCTS
        productRepository.findAllByCompany_Id(companyId).forEach(product -> {
            aiGeneratedResponseRepository.findByEntityTypeAndEntityId(EntityType.PRODUCT, product.getId())
                .ifPresentOrElse(
                    ai -> aiResponses.add(new AIResponseDTO("PRODUCT", ai.getAiResponse())),
                    () -> aiResponses.add(new AIResponseDTO("PRODUCT", "Aucune réponse IA trouvée pour le produit " + product.getNameProductOrService()))
                );
        });

        // TEAMS
        teamRepository.findAllByCompany_Id(companyId).forEach(team -> {
            aiGeneratedResponseRepository.findByEntityTypeAndEntityId(EntityType.TEAM, team.getId())
                .ifPresentOrElse(
                    ai -> aiResponses.add(new AIResponseDTO("TEAM", ai.getAiResponse())),
                    () -> aiResponses.add(new AIResponseDTO("TEAM", "Aucune réponse IA trouvée pour ce membre."))
                );
        });

        // MARKETING
        marketingRepository.findAllByCompany_Id(companyId).forEach(marketing -> {
            aiGeneratedResponseRepository.findByEntityTypeAndEntityId(EntityType.MARKETING, marketing.getId())
                .ifPresentOrElse(
                    ai -> aiResponses.add(new AIResponseDTO("MARKETING", ai.getAiResponse())),
                    () -> aiResponses.add(new AIResponseDTO("MARKETING", "Aucune réponse IA trouvée."))
                );
        });

        BusinessPlanFinalDTO dto = new BusinessPlanFinalDTO();
        dto.setTitle("Business Plan pour " + company.getEnterpriseName());
        dto.setCreationDate(Instant.now());
        dto.setAiResponses(aiResponses);

        return dto;
    }
    public BusinessPlanFinal generateBusinessPlan(Company company, BusinessPlanFinal businessPlanFinal) {
        // Récupérer les réponses IA liées à l’entreprise
        List<AIGeneratedResponse> responses = aiGeneratedResponseRepository.findByEntityIdAndEntityType(company.getId(), EntityType.COMPANY);

        // Regrouper le contenu par type
        Map<String, String> contentByType = responses.stream()
            .collect(Collectors.toMap(
                r -> r.getEntityType().name().toLowerCase(),
                AIGeneratedResponse::getAiResponse,
                (v1, v2) -> v1 + "\n" + v2
            ));

        // Construire le prompt
        String prompt = buildPromptFromModel(contentByType, company.getEnterpriseName());

        // Générer le texte avec Gemini
        String result = aiGenerationService.generateText(prompt);

        // Mettre à jour l’objet BusinessPlanFinal
        businessPlanFinal.setFinalContent(result);
        businessPlanFinal.setCreationDate(Instant.now());

        // ** Titre = nom de l’entreprise **
        businessPlanFinal.setTitle(company.getEnterpriseName());

        // Sauvegarder et retourner
        return businessPlanFinalRepository.save(businessPlanFinal);
    }

    public ByteArrayOutputStream generatePdf(BusinessPlanFinal plan, Company company) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.addTitle("Business Plan PDF");
        document.add(new Paragraph("Nom de l'entreprise : " + company.getEnterpriseName()));
        document.add(new Paragraph("Titre du Business Plan : " + plan.getTitle()));
        document.add(new Paragraph("Contenu final : " + plan.getFinalContent()));

        document.close();
        return outputStream;
    }

    private String buildPromptFromModel(Map<String, String> sections, String companyName) {
        return "Tu es un expert en création de business plan professionnel. Génère un business plan structuré pour l'entreprise \"" + companyName + "\" selon ce plan :\n\n"
            + "1. Synthèse\n"
            + sections.getOrDefault("summary", "") + "\n\n"

            + "2. Description de l’entreprise\n"
            + sections.getOrDefault("COMPANY", "") + "\n\n"

            + "3. L’opportunité\n"
            + sections.getOrDefault("PRODUCT", "") + "\n\n"

            + "4. Analyse du secteur d’activité\n"
            + sections.getOrDefault("marketing", "") + "\n\n"

            + "5. Étude et définition du marché cible\n"
            + sections.getOrDefault("MARKETING", "") + "\n\n"

            + "6. Équipe de management\n"
            + sections.getOrDefault("TEAM", "") + "\n\n"

            + "7. Plan des opérations\n"
            + "(À compléter à partir du contexte de l’entreprise)\n\n"

            + "8. Stratégie marketing\n"
            + sections.getOrDefault("MARKETING", "") + "\n\n"

            + "9. Plan et échéancier de mise en œuvre\n"
            + "(À générer selon le secteur)\n\n"

            + "10. Plan de financement\n"
            + "(À générer en fonction des investissements et prévisions)\n\n"

            + "11. Conclusion\n"
            + "Résume les points clés et incite à l’investissement.\n\n"

            + "Génère un document fluide, clair et professionnel.";
    }

    public List<BusinessPlanFinalDTO> findAllByCompany(String companyId) {
        return businessPlanFinalRepository.findAllByCompany_Id(companyId)
            .stream()
            .map(plan -> {
                BusinessPlanFinalDTO dto = new BusinessPlanFinalDTO();

                dto.setTitle(plan.getTitle());
                dto.setCreationDate(plan.getCreationDate());
                dto.setCompanyId(plan.getCompany().getId());
                return dto;
            })
            .collect(Collectors.toList());

    }
    public List<BusinessPlanFinalDTO> findAll() {
        return businessPlanFinalRepository.findAll().stream()
            .map(plan -> {
                BusinessPlanFinalDTO dto = new BusinessPlanFinalDTO();
                dto.setTitle(plan.getTitle());
                dto.setCreationDate(plan.getCreationDate());
                dto.setFinalContent(plan.getFinalContent());
                dto.setId(plan.getId());

                if (plan.getCompany() != null) {
                    String companyId = plan.getCompany().getId();
                    dto.setCompanyId(companyId);

                    // récupérer tous les IDs d’entités liées à cette company
                    List<String> allEntityIds = new ArrayList<>();
                    allEntityIds.addAll(productRepository.findAllByCompanyId(companyId).stream().map(ProductOrService::getId).collect(Collectors.toList()));
                    allEntityIds.addAll(teamRepository.findAllByCompanyId(companyId).stream().map(Team::getId).collect(Collectors.toList()));
                    allEntityIds.addAll(marketingRepository.findAllByCompanyId(companyId).stream().map(Marketing::getId).collect(Collectors.toList()));

                    allEntityIds.add(companyId); // inclure la company elle-même

                    // récupérer toutes les réponses IA liées
                    List<AIGeneratedResponse> aiList = aiGeneratedResponseRepository.findAllByEntityIdIn(allEntityIds);

                    List<AIResponseDTO> aiDtos = aiList.stream()
                        .map(ai -> new AIResponseDTO(ai.getEntityType().name(), ai.getAiResponse()))
                        .collect(Collectors.toList());

                    dto.setAiResponses(aiDtos);
                } else {
                    dto.setCompanyId(null);
                    dto.setAiResponses(Collections.emptyList());
                }

                return dto;
            })
            .collect(Collectors.toList());
    }

    public File generatePdfForCompany(String companyId) throws IOException {
        BusinessPlanFinal businessPlan = businessPlanFinalRepository.findByCompany_Id(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Aucun BusinessPlanFinal trouvé pour cette entreprise"));

        String companyName = businessPlan.getCompany().getEnterpriseName();
        String content = businessPlan.getFinalContent(); // ou selon ton champ réel

        // Chemin temporaire du fichier
        String fileName = "BusinessPlan-" + companyName + "-" + LocalDate.now() + ".pdf";
        File pdfFile = new File(System.getProperty("java.io.tmpdir"), fileName);

        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();
            document.add(new Paragraph("Business Plan de l'entreprise: " + companyName));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(content));
            document.close();
        } catch (DocumentException e) {
            throw new IOException("Erreur lors de la génération du PDF", e);
        }

        return pdfFile;
    }



    /*
    public BusinessPlanFinal generatePlanFromCompany(String companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
        Set<ProductOrService> products = productRepository.findAllByCompany_Id(companyId);
        Set<Team> teams = teamRepository.findAllByCompany_Id(companyId);
        Set<Marketing> marketings = marketingRepository.findAllByCompany_Id(companyId);

        BusinessPlanFinal plan = new BusinessPlanFinal();
        plan.setCompany(company);
        plan.setProducts(products);
        plan.setTeams(teams);
        plan.setMarketings(marketings);
        plan.setTitle("Business Plan pour " + company.getEnterpriseName());
        plan.setCreationDate(Instant.now());

        return businessPlanFinalRepository.save(plan);
    }*/

    //
    //
    //
    //public List<AIGeneratedResponse> getAllResponsesForCompany(String companyId) {
    //  List<String> entityIds = new ArrayList<>();
    //entityIds.addAll(productRepository.findAllByCompanyId(companyId).stream().map(ProductOrService::getId).toList());
    //entityIds.addAll(teamRepository.findAllByCompanyId(companyId).stream().map(Team::getId).toList());
    //entityIds.addAll(marketingRepository.findAllByCompanyId(companyId).stream().map(Marketing::getId).toList());


    //return aiGenerationService.findByEntityIdIn(entityIds);
    //}

    /**
     * Save a businessPlanFinal.
     *
     * @param businessPlanFinal the entity to save.
     * @return the persisted entity.
     */
    public BusinessPlanFinal save(BusinessPlanFinal businessPlanFinal) {
        log.debug("Request to save BusinessPlanFinal : {}", businessPlanFinal);
        return businessPlanFinalRepository.save(businessPlanFinal);
    }

    /**
     * Update a businessPlanFinal.
     *
     * @param businessPlanFinal the entity to save.
     * @return the persisted entity.
     */
    public BusinessPlanFinal update(BusinessPlanFinal businessPlanFinal) {
        log.debug("Request to update BusinessPlanFinal : {}", businessPlanFinal);
        return businessPlanFinalRepository.save(businessPlanFinal);
    }

    /**
     * Partially update a businessPlanFinal.
     *
     * @param businessPlanFinal the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BusinessPlanFinal> partialUpdate(BusinessPlanFinal businessPlanFinal) {
        log.debug("Request to partially update BusinessPlanFinal : {}", businessPlanFinal);

        return businessPlanFinalRepository
            .findById(businessPlanFinal.getId())
            .map(existingBusinessPlanFinal -> {
                if (businessPlanFinal.getTitle() != null) {
                    existingBusinessPlanFinal.setTitle(businessPlanFinal.getTitle());
                }
                if (businessPlanFinal.getDescription() != null) {
                    existingBusinessPlanFinal.setDescription(businessPlanFinal.getDescription());
                }
                if (businessPlanFinal.getCreationDate() != null) {
                    existingBusinessPlanFinal.setCreationDate(businessPlanFinal.getCreationDate());
                }
                if (businessPlanFinal.getCompany() != null) {
                    existingBusinessPlanFinal.setCompany(businessPlanFinal.getCompany());
                }
                if (businessPlanFinal.getProducts() != null) {
                    existingBusinessPlanFinal.setProducts(businessPlanFinal.getProducts());
                }
                if (businessPlanFinal.getTeams() != null) {
                    existingBusinessPlanFinal.setTeams(businessPlanFinal.getTeams());
                }
                if (businessPlanFinal.getMarketings() != null) {
                    existingBusinessPlanFinal.setMarketings(businessPlanFinal.getMarketings());
                }

                return existingBusinessPlanFinal;
            })
            .map(businessPlanFinalRepository::save);
    }




    /**
     * Get one businessPlanFinal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<BusinessPlanFinal> findOne(String id) {
        log.debug("Request to get BusinessPlanFinal : {}", id);
        return businessPlanFinalRepository.findById(id);
    }

    /**
     * Delete the businessPlanFinal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete BusinessPlanFinal : {}", id);
        if (!businessPlanFinalRepository.existsById(id)) {
            throw new EntityNotFoundException("BusinessPlanFinal not found with id " + id);
        }
        businessPlanFinalRepository.deleteById(id);
    }
}
