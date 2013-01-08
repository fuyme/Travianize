package com.travianize.travianize.parsers;

import com.travianize.travianize.parsers.pages.Dorf1Page;
import com.travianize.travianize.utils.Logger;
import com.travianize.travianize.utils.RegexpUtils;
import java.util.ArrayList;
import java.util.List;

public class Dorf1PageParser {

    private static int[] getProductions(String html) {

        List<List<String>> productionTableRegx = new ArrayList<List<String>>();
        RegexpUtils.preg_match_all("/<table id=\"production\"(.+?)<\\/table>/", html, productionTableRegx);

        if (productionTableRegx.size() < 1 || productionTableRegx.get(0).size() < 1) {
            Logger.info("Can't find production table");
            return null;
        }
        String productionTable = productionTableRegx.get(0).get(0);
        List<List<String>> productionRegx = new ArrayList<List<String>>();
        RegexpUtils.preg_match_all("/<td class=\"num\">(.+?)<\\/td>/", productionTable, productionRegx);

        int[] productions = new int[4];
        for (int i = 0; i < 4; i++) {

            if (productionRegx.size() < i + 1 || productionRegx.get(i).size() < 2) {
                Logger.info("Can't find production " + i);
                return null;
            }
            try {
                productions[i] = Integer.parseInt(productionRegx.get(i).get(1).trim());
            } catch (Exception e) {
                Logger.info("Production " + i + " has not integer value");
                return null;
            }

        }

        return productions;


    }

    private static int[] getResources(String html) {

        List<List<String>> resourceRegx = new ArrayList<List<String>>();
        RegexpUtils.preg_match_all("/<span id=\"l[0-9]\" class=\"value \">(.+?)/[0-9]{1,}<\\/span>/", html, resourceRegx);

        int[] resurses = new int[4];

        for (int i = 0; i < 4; i++) {

            if (resourceRegx.size() < i + 1 || resourceRegx.get(i).size() < 2) {
                Logger.info("Can't find resource " + i);
                return null;
            }

            try {
                resurses[i] = Integer.parseInt(resourceRegx.get(i).get(1));
            } catch (Exception e) {
                Logger.info("Resource " + i + " has not integer value");
                return null;
            }
        }

        return resurses;

    }

    private static int[] getBuildingProductionParse(String html) {

        int[] buildingProduction;

        List<List<String>> buildingProductionDivRegx = new ArrayList<List<String>>();
        RegexpUtils.preg_match_all("/<div class=\"boxes buildingList\">(.+?)<\\/table>/", html, buildingProductionDivRegx);

        if (buildingProductionDivRegx.size() < 1 || buildingProductionDivRegx.get(0).size() < 1) {
            return new int[0];
        }

        String buildingProductionDiv = buildingProductionDivRegx.get(0).get(0);
        List<List<String>> buildingProductionRegx = new ArrayList<List<String>>();

        RegexpUtils.preg_match_all("/<span id=\"timer[0-9]\">(.+?)<\\/span>/", buildingProductionDiv, buildingProductionRegx);
        buildingProduction = new int[buildingProductionRegx.size()];
        for (int i = 0; i < buildingProductionRegx.size(); i++) {
            try{
                String[] stringRemainingTimes = buildingProductionRegx.get(i).get(1).split(":");
                int remainingTime = Integer.parseInt(stringRemainingTimes[0]) * 3600 + Integer.parseInt(stringRemainingTimes[1]) * 60 + Integer.parseInt(stringRemainingTimes[2]);
                buildingProduction[i] = remainingTime;
            }catch(NumberFormatException e){
                Logger.info("Time format is wrong");
                buildingProduction[i] = 300;
            }
        }

        return buildingProduction;

    }

    public static Dorf1Page parse(String html) {

        Dorf1Page page = new Dorf1Page();

        page.productions = getProductions(html);
        page.resources = getResources(html);
        page.buildingProductions = getBuildingProductionParse(html);

        return page;

    }
}