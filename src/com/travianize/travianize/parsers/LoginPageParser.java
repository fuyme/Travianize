package com.travianize.travianize.parsers;

import com.travianize.travianize.parsers.pages.LoginPage;

public class LoginPageParser {

    public static LoginPage parse(String html) {

        LoginPage page = new LoginPage();
        page.login = getLogin(html);
        return page;

    }

    private static String getLogin(String html) {

        String[] firstPartLogin = html.split("name=\"login\" value=\"");
        String[] secondPartLogin = firstPartLogin[1].split("\"");
        return secondPartLogin[0];

    }
}
