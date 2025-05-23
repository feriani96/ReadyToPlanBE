package com.readytoplanbe.myapp.service.dto;

import com.readytoplanbe.myapp.domain.enumeration.Country;
import com.readytoplanbe.myapp.domain.enumeration.Currency;
import com.readytoplanbe.myapp.domain.enumeration.Languages;

import java.time.LocalDate;

public class BusinessPlanInputDTO {
    private String companyName;
    private String companyDescription;
    private LocalDate companyStartDate;
    private Country country;
    private Languages languages;
    private Double anticipatedProjectSize;
    private Currency currency;

    // Partie 1
    private String companyDescriptionDetails;
    private String riskOverview;
    private String marketAnalysis;
    private String userTarget;
    private String investmentNeeds;

    // Partie 2
    private String companyValuation;
    private String stressTestData;
    private String legalClauses;

    // Partie 3
    private String roadmap;
    private String foundingTeam;
    private String strategicPartners;
    private String kpis;

    // Partie 4
    private String acquisitionStrategies;
    private String cacVsLtv;
    private String marketingChannels;

    // Partie 5
    private String techArchitecture;
    private String techAdvantages;
    private String criticalDependencies;

    // Getters et Setters générés

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }
    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public LocalDate getCompanyStartDate() {
        return companyStartDate;
    }
    public void setCompanyStartDate(LocalDate companyStartDate) {
        this.companyStartDate = companyStartDate;
    }

    public Country getCountry() {
        return country;
    }
    public void setCountry(Country country) {
        this.country = country;
    }

    public Languages getLanguages() {
        return languages;
    }
    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public Double getAnticipatedProjectSize() {
        return anticipatedProjectSize;
    }
    public void setAnticipatedProjectSize(Double anticipatedProjectSize) {
        this.anticipatedProjectSize = anticipatedProjectSize;
    }

    public Currency getCurrency() {
        return currency;
    }
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    // Partie 1
    public String getCompanyDescriptionDetails() {
        return companyDescriptionDetails;
    }
    public void setCompanyDescriptionDetails(String companyDescriptionDetails) {
        this.companyDescriptionDetails = companyDescriptionDetails;
    }

    public String getRiskOverview() {
        return riskOverview;
    }
    public void setRiskOverview(String riskOverview) {
        this.riskOverview = riskOverview;
    }

    public String getMarketAnalysis() {
        return marketAnalysis;
    }
    public void setMarketAnalysis(String marketAnalysis) {
        this.marketAnalysis = marketAnalysis;
    }

    public String getUserTarget() {
        return userTarget;
    }
    public void setUserTarget(String userTarget) {
        this.userTarget = userTarget;
    }

    public String getInvestmentNeeds() {
        return investmentNeeds;
    }
    public void setInvestmentNeeds(String investmentNeeds) {
        this.investmentNeeds = investmentNeeds;
    }

    // Partie 2
    public String getCompanyValuation() {
        return companyValuation;
    }
    public void setCompanyValuation(String companyValuation) {
        this.companyValuation = companyValuation;
    }

    public String getStressTestData() {
        return stressTestData;
    }
    public void setStressTestData(String stressTestData) {
        this.stressTestData = stressTestData;
    }

    public String getLegalClauses() {
        return legalClauses;
    }
    public void setLegalClauses(String legalClauses) {
        this.legalClauses = legalClauses;
    }

    // Partie 3
    public String getRoadmap() {
        return roadmap;
    }
    public void setRoadmap(String roadmap) {
        this.roadmap = roadmap;
    }

    public String getFoundingTeam() {
        return foundingTeam;
    }
    public void setFoundingTeam(String foundingTeam) {
        this.foundingTeam = foundingTeam;
    }

    public String getStrategicPartners() {
        return strategicPartners;
    }
    public void setStrategicPartners(String strategicPartners) {
        this.strategicPartners = strategicPartners;
    }

    public String getKpis() {
        return kpis;
    }
    public void setKpis(String kpis) {
        this.kpis = kpis;
    }

    // Partie 4
    public String getAcquisitionStrategies() {
        return acquisitionStrategies;
    }
    public void setAcquisitionStrategies(String acquisitionStrategies) {
        this.acquisitionStrategies = acquisitionStrategies;
    }

    public String getCacVsLtv() { return cacVsLtv; }
    public void setCacVsLtv(String cacVsLtv) {
        this.cacVsLtv = cacVsLtv;
    }

    public String getMarketingChannels() {
        return marketingChannels;
    }
    public void setMarketingChannels(String marketingChannels) {
        this.marketingChannels = marketingChannels;
    }

    // Partie 5
    public String getTechArchitecture() {
        return techArchitecture;
    }
    public void setTechArchitecture(String techArchitecture) {
        this.techArchitecture = techArchitecture;
    }

    public String getTechAdvantages() {
        return techAdvantages;
    }
    public void setTechAdvantages(String techAdvantages) {
        this.techAdvantages = techAdvantages;
    }

    public String getCriticalDependencies() {
        return criticalDependencies;
    }
    public void setCriticalDependencies(String criticalDependencies) {
        this.criticalDependencies = criticalDependencies;
    }

    public BusinessPlanInputDTO(BusinessPlanDTO businessPlanDTO) {
        this.companyName = businessPlanDTO.getCompanyName();
        this.companyDescription = businessPlanDTO.getCompanyDescription();
        this.companyStartDate = businessPlanDTO.getCompanyStartDate();
        this.country = businessPlanDTO.getCountry();
        this.languages = businessPlanDTO.getLanguages();
        this.anticipatedProjectSize = businessPlanDTO.getAnticipatedProjectSize();
        this.currency = businessPlanDTO.getCurrency();
    }

}
