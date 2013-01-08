package com.travianize.travianize.parsers;

import com.travianize.travianize.parsers.pages.UpgradingFieldPage;
import com.travianize.travianize.utils.Logger;
import com.travianize.travianize.utils.RegexpUtils;
import java.util.ArrayList;
import java.util.List;

public class UpgradingFieldPageParser {

    public static String getUpgradingLink(String html){

        List<List<String>> linkRegx = new ArrayList<List<String>>();

        RegexpUtils.preg_match_all("/class=\"build\" onclick=\"window\\.location\\.href = '(.+?)'; return false;\">/", html, linkRegx);

        if(linkRegx.size()<1 || linkRegx.get(0).size()<2){
            return null;
        }

        String link = linkRegx.get(0).get(1);

        return link.replaceAll("&amp;", "&");


    }

    private static int[] getRequiredResourses(String html) {
        List<List<String>> resoursesDivRegx = new ArrayList<List<String>>();

        RegexpUtils.preg_match_all("/<div class=\"showCosts\">(.+?)<\\/div>/", html, resoursesDivRegx);
        if(resoursesDivRegx.size()<1 || resoursesDivRegx.get(0).size()<2){
            return null;
        }
        String resoursesDiv = resoursesDivRegx.get(0).get(1);

        List<List<String>> resoursesRegx = new ArrayList<List<String>>();
        RegexpUtils.preg_match_all("/<img class=\"r[0-9]{1}\" src=\"img\\/x\\.gif\" alt=\"[A-Za-z]{1,}\" \\/>(.+?)<\\/span>/", resoursesDiv, resoursesRegx);

        if(resoursesRegx.size()!=4){
            Logger.info("Find "+resoursesRegx.size()+" types. Need 4");
            return null;
        }

        int[] resourses = new int[4];

        for (int i = 0; i < 4; i++) {
            try{
                resourses[i] = Integer.parseInt(resoursesRegx.get(i).get(1));
            }catch(NumberFormatException e){
                Logger.info("Can't parse resourse amount");
                resourses[i] = 0;
            }
        }

        return resourses;

    }


    public static UpgradingFieldPage parse(String html){

        UpgradingFieldPage page = new UpgradingFieldPage();

        page.upgradingLink = getUpgradingLink(html);
        page.requiredResourses = getRequiredResourses(html);

        return page;

    }
}
