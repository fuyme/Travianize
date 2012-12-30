package com.travianize.travianize.connection;

import com.travianize.travianize.exception.LoadHttpPageException;
import com.travianize.travianize.parsers.UpgradingFieldPageParser;
import com.travianize.travianize.parsers.pages.UpgradingFieldPage;
import com.travianize.travianize.utils.Logger;
import java.net.UnknownHostException;

public class TravianConnector extends Connection {

    public TravianConnector(String host) throws UnknownHostException {

        super(host);

    }

    public void login(String login, String password) throws LoadHttpPageException {

        get("/login.php", null);

        RequestData[] values = new RequestData[5];

        values[0] = new RequestData();
        values[0].name = "name";
        values[0].value = login;
        values[1] = new RequestData();
        values[1].name = "password";
        values[1].value = password;
        values[2] = new RequestData();
        //TODO: login value is const. Need real value
        values[2].name = "login";
        values[2].value = "123456";
        values[3] = new RequestData();
        values[3].name = "w";
        values[3].value = "1280:1024";
        values[4] = new RequestData();
        values[4].name = "s1";
        values[4].value = "Login";

        post("/dorf1.php", values);


    }

    public void upgradingField(int id) throws LoadHttpPageException {

        RequestData[] requestDatas = new RequestData[1];
        requestDatas[0] = new RequestData("id", id + "");

        get("/build.php", requestDatas);

        String html = getLastRequestResult().getHtml();

        UpgradingFieldPage page = UpgradingFieldPageParser.parse(html);

        get("/" + page.upgradingLink, null);

        Logger.info("Start upgrading field " + id);

    }
}
