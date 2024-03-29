package com.travianize.travianize.connection;

import com.travianize.travianize.exception.LoadHttpPageException;
import com.travianize.travianize.exception.LoginException;
import com.travianize.travianize.exception.UpgradingAvailableException;
import com.travianize.travianize.parsers.LoginPageParser;
import com.travianize.travianize.parsers.UpgradingFieldPageParser;
import com.travianize.travianize.parsers.pages.LoginPage;
import com.travianize.travianize.parsers.pages.UpgradingFieldPage;
import com.travianize.travianize.travian.Account;
import com.travianize.travianize.utils.Logger;
import java.net.UnknownHostException;

public class TravianConnector extends TravianConnection{

    public TravianConnector(String host, Account account) throws UnknownHostException {

        super(host, account);

    }

    public void login(String login, String password) throws LoadHttpPageException, LoginException {

        getLogin(null);

        LoginPage page = LoginPageParser.parse(getLastRequestResult().getHtml());

        RequestData[] values = new RequestData[5];

        values[0] = new RequestData();
        values[0].name = "name";
        values[0].value = login;
        values[1] = new RequestData();
        values[1].name = "password";
        values[1].value = password;
        values[2] = new RequestData();
        values[2].name = "login";
        values[2].value = page.login;
        values[3] = new RequestData();
        values[3].name = "w";
        values[3].value = "1280:1024";
        values[4] = new RequestData();
        values[4].name = "s1";
        values[4].value = "Login";

        postDorf1(values);

        if(!isLogin(getLastRequestResult().getHtml())){
            throw new LoginException();
        }

        Logger.info("Success logined as "+login);

    }

    private void upgrading(int idField) throws LoadHttpPageException, UpgradingAvailableException, LoginException {

        getDorf1(null);

        if(!isLogin(getLastRequestResult().getHtml())){

            login(account.getLogin(), account.getPassword());
            getDorf1(null);

        }

        RequestData[] requestDatas = new RequestData[1];
        requestDatas[0] = new RequestData("id", idField + "");

        getBuild(requestDatas);

        String html = getLastRequestResult().getHtml();

        UpgradingFieldPage page = UpgradingFieldPageParser.parse(html);
        if(page.upgradingLink==null){
            throw new UpgradingAvailableException(page);
        }

        String[] getData = page.upgradingLink.split("\\?");
        String[] datas = getData[1].split("&");

        RequestData[] buildDatas = new RequestData[datas.length];
        for(int i=0;i<datas.length;i++){

            String[] buildStringDatas = datas[i].split("=");
            buildDatas[i] = new RequestData(buildStringDatas[0], buildStringDatas[1]);

        }

        getDorf1(buildDatas);

        Logger.info("Start upgrading field " + idField);

    }

    public void upgradingField(int idField) throws LoadHttpPageException, UpgradingAvailableException, LoginException {
        upgrading(idField);
    }

    public void upgradingBuilding(int idField)  throws LoadHttpPageException, UpgradingAvailableException, LoginException {
        upgrading(idField);
    }

    private boolean isLogin(String html) {

        return html.indexOf("<button type=\"submit\" value=\"Login\"") == -1;

    }
}
