package com.travianize.travianize.connection;

import com.travianize.travianize.exception.LoadHttpPageException;
import com.travianize.travianize.utils.Logger;
import java.net.UnknownHostException;


public class TravianConnector extends Connection{

    public TravianConnector(String host) throws UnknownHostException{

        super(host);

    }

}
