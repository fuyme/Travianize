package com.travianize.travianize.parsers;

import com.travianize.travianize.exception.UpgradingAvailableException;
import com.travianize.travianize.parsers.pages.UpgradingFieldPage;
import com.travianize.travianize.utils.RegexpUtils;
import java.util.ArrayList;
import java.util.List;

public class UpgradingFieldPageParser {

    public static String getUpgradingLink(String html){

        List<List<String>> linkRegx = new ArrayList<List<String>>();

        RegexpUtils.preg_match_all("/class=\"build\" onclick=\"window\\.location\\.href = '(.+?)'; return false;\">/", html, linkRegx);
        //TODO: add check: is elements exist
        if(linkRegx.size()<1 || linkRegx.get(0).size()<2){
            return null;
        }

        String link = linkRegx.get(0).get(1);

        return link.replaceAll("&amp;", "&");


    }

    public static UpgradingFieldPage parse(String html){

        UpgradingFieldPage page = new UpgradingFieldPage();

        page.upgradingLink = getUpgradingLink(html);

        return page;

    }
}
