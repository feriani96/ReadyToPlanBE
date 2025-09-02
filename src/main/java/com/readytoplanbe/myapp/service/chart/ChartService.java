package com.readytoplanbe.myapp.service.chart;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.ChartColor;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartService {

    public byte[] createChart(String jsonData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataNode = objectMapper.readTree(jsonData);

        String type = dataNode.path("type").asText("bar");

        switch (type.toLowerCase()) {
            case "timeline":
                return createTimelineChart(dataNode);
            case "pie":
            case "camembert":
                return createPieChart(dataNode);
            case "bar":
            default:
                return createBarChart(dataNode);
        }
    }

    private byte[] createBarChart(JsonNode dataNode) throws IOException {
        String title = dataNode.path("title").asText("Graphique en Barres");
        JsonNode categoriesNode = dataNode.path("categories");
        JsonNode valuesNode = dataNode.path("values");

        List<String> categories = new ArrayList<>();
        if (categoriesNode.isArray()) {
            categoriesNode.forEach(node -> categories.add(node.asText()));
        }

        List<Double> values = new ArrayList<>();
        if (valuesNode.isArray()) {
            valuesNode.forEach(node -> values.add(node.asDouble()));
        }

        if (categories.isEmpty() || values.isEmpty() || categories.size() != values.size()) {
            throw new IllegalArgumentException("Les données JSON sont invalides ou incomplètes pour un graphique en barres.");
        }

        // Créer le graphique avec XChart
        CategoryChart chart = new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title(title)
            .xAxisTitle("Catégories")
            .yAxisTitle("Valeurs")
            .build();

        // Configurer le style du graphique
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setSeriesColors(new Color[]{
            ChartColor.BLUE.getColor(),
            ChartColor.RED.getColor(),
            ChartColor.GREEN.getColor(),
            ChartColor.LIGHT_GREY.getColor(),
            ChartColor.BLACK.getColor()
        });
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);

        chart.addSeries("Série 1", categories, values.stream().map(Number.class::cast).collect(Collectors.toList()));

        // Convertir le graphique en image PNG
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BitmapEncoder.saveBitmap(chart, os, BitmapEncoder.BitmapFormat.PNG);
        return os.toByteArray();
    }

    private byte[] createTimelineChart(JsonNode dataNode) throws IOException {
        String title = dataNode.path("title").asText("Chronologie");
        JsonNode dataArray = dataNode.path("data");

        List<Integer> years = new ArrayList<>();
        List<String> events = new ArrayList<>();

        if (dataArray.isArray()) {
            for (JsonNode item : dataArray) {
                years.add(item.path("year").asInt());
                events.add(item.path("event").asText());
            }
        }

        if (years.isEmpty()) {
            throw new IllegalArgumentException("Données JSON invalides pour une chronologie.");
        }

        // Créer un graphique de barres horizontales pour la timeline
        CategoryChart chart = new CategoryChartBuilder()
            .width(800)
            .height(400 + years.size() * 40) // Ajuster la hauteur dynamiquement
            .title(title)
            .xAxisTitle("Année")
            .yAxisTitle("Événement")
            .build();

        // Configurer le style
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setAvailableSpaceFill(0.5);
        chart.getStyler().setOverlapped(true);

        // Pour chaque événement, créer une barre avec l'année comme valeur
        List<Double> yearValues = years.stream()
            .map(year -> year.doubleValue())
            .collect(Collectors.toList());

        chart.addSeries("Timeline", events, yearValues);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BitmapEncoder.saveBitmap(chart, os, BitmapEncoder.BitmapFormat.PNG);
        return os.toByteArray();
    }

    private byte[] createPieChart(JsonNode dataNode) throws IOException {
        String title = dataNode.path("title").asText("Graphique Circulaire");
        JsonNode dataArray = dataNode.path("data");
        JsonNode labelsNode = dataNode.path("labels");
        JsonNode valuesNode = dataNode.path("values");

        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        // Essayer d'abord le format avec labels/values
        if (labelsNode.isArray() && valuesNode.isArray()) {
            labelsNode.forEach(node -> labels.add(node.asText()));
            valuesNode.forEach(node -> values.add(node.asDouble()));
        }
        // Sinon essayer le format data[{label, value}]
        else if (dataArray.isArray()) {
            for (JsonNode item : dataArray) {
                labels.add(item.path("label").asText());
                values.add(item.path("value").asDouble());
            }
        }

        if (labels.isEmpty() || values.isEmpty() || labels.size() != values.size()) {
            throw new IllegalArgumentException("Données JSON invalides pour un graphique circulaire.");
        }

        // Créer le graphique circulaire
        PieChart chart = new PieChartBuilder()
            .width(800)
            .height(600)
            .title(title)
            .build();

        // Ajouter les données
        for (int i = 0; i < labels.size(); i++) {
            chart.addSeries(labels.get(i), values.get(i));
        }

        // Configurer le style
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BitmapEncoder.saveBitmap(chart, os, BitmapEncoder.BitmapFormat.PNG);
        return os.toByteArray();
    }
}
