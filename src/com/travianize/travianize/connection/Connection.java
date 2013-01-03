package com.travianize.travianize.connection;

import com.travianize.travianize.exception.LoadHttpPageException;
import java.net.UnknownHostException;

public class Connection extends HttpWorker {

    protected Connection(String host) throws UnknownHostException {

        super(host, 80);

    }

    public void get(String path, RequestData[] requestDatas) throws LoadHttpPageException {

        String dataString = "";

        if (requestDatas != null && requestDatas.length != 0) {
            dataString = "?";
            for (RequestData requestData : requestDatas) {
                dataString += requestData.name + "=" + requestData.value + "&";
            }
            dataString = dataString.substring(0, dataString.length() - 1);
            
        }

        makeConnect("GET", path + dataString, "");

    }

    public void post(String path, RequestData[] requestDatas) throws LoadHttpPageException {

        String dataString = "";

        if (requestDatas != null && requestDatas.length != 0) {

            for (RequestData requestData : requestDatas) {
                dataString += requestData.name + "=" + requestData.value + "&";
            }
            dataString = dataString.substring(0, dataString.length() - 1);
        }

        makeConnect("POST", path, dataString);


    }
}
