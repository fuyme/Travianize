package com.travianize.travianize.connection;

import com.travianize.travianize.exception.LoadHttpPageException;
import com.travianize.travianize.parsers.Dorf1PageParser;
import com.travianize.travianize.parsers.pages.Dorf1Page;
import com.travianize.travianize.travian.Account;
import java.net.UnknownHostException;

public class TravianConnection extends Connection {

    private Account account;

    public TravianConnection(String host, Account account) throws UnknownHostException {

        super(host);
        this.account=account;

    }

    public void getLogin(RequestData[] requestDatas) throws LoadHttpPageException{

        get("/login.php",requestDatas);


    }

    public void postDorf1(RequestData[] requestDatas) throws LoadHttpPageException{

        post("/dorf1.php",requestDatas);
        Dorf1Page page = Dorf1PageParser.parse(getLastRequestResult().getHtml());
        account.updateFromDorf1Page(page);

    }

    public void getDorf1(RequestData[] requestDatas) throws LoadHttpPageException{

        get("/dorf1.php",requestDatas);
        Dorf1Page page = Dorf1PageParser.parse(getLastRequestResult().getHtml());
        account.updateFromDorf1Page(page);

    }

    public void getBuild(RequestData[] requestDatas) throws LoadHttpPageException{

        get("/build.php",requestDatas);

    }

}
