package com.travianize.travianize.travian;

import com.travianize.travianize.utils.Logger;

public class Account {

    public Account(String serverHostName, String login, String password) {

        Logger.info("Create account '"+login+"', password: '"+password+"' for host '"+serverHostName+"'");

    }

}
