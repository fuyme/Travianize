package com.travianize.travianize.travian;

import com.travianize.travianize.connection.TravianConnector;
import com.travianize.travianize.utils.Logger;
import java.net.UnknownHostException;
import java.util.logging.Level;

public class Account {

    TravianConnector connection;

    public Account(String serverHostName, String login, String password) {
        try {
            connection = new TravianConnector(serverHostName);

            Logger.info("Create account '"+login+"', password: '"+password+"' for host '"+serverHostName+"'");
        } catch (UnknownHostException ex) {
            Logger.info("Unknown Host '"+serverHostName+"'");
        }

    }

}
