package com.travianize.travianize.connection;

import com.travianize.travianize.exception.LoadHttpPageException;
import java.net.UnknownHostException;

public class TravianConnection extends Connection {

    public TravianConnection(String host) throws UnknownHostException {

        super(host);

    }

    public void getLogin(RequestData[] requestDatas) throws LoadHttpPageException{

        get("/login.php",requestDatas);

    }

    public void postDorf1(RequestData[] requestDatas) throws LoadHttpPageException{

        post("/dorf1.php",requestDatas);

    }

    public void getDorf1(RequestData[] requestDatas) throws LoadHttpPageException{

        get("/dorf1.php",requestDatas);

    }

    public void getBuild(RequestData[] requestDatas) throws LoadHttpPageException{

        get("/build.php",requestDatas);

    }

}
