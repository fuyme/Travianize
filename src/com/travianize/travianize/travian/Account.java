package com.travianize.travianize.travian;

import com.travianize.travianize.connection.TravianConnector;
import com.travianize.travianize.exception.LoadHttpPageException;
import com.travianize.travianize.travian.tasks.Task;
import com.travianize.travianize.travian.tasks.UpgradingFieldTask;
import com.travianize.travianize.utils.Logger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;

public class Account {

    TravianConnector connection;
    ArrayList<Task> tasks = new ArrayList<Task>();

    public Account(String serverHostName, String login, String password) {
        try {
            connection = new TravianConnector(serverHostName);
            try{
                connection.login(login, password);

            }catch(LoadHttpPageException e){
                Logger.info("Can't load page");
            }
            Logger.info("Create account '"+login+"', password: '"+password+"' for host '"+serverHostName+"'");
        } catch (UnknownHostException ex) {
            Logger.info("Unknown Host '"+serverHostName+"'");
        }

        for(int i=1;i<19;i++){
            UpgradingFieldTask task = new UpgradingFieldTask();
            task.idField = i;
            task.time = (int)(System.currentTimeMillis()/1000) +(i*300);
            tasks.add(task);
        }

        Collections.sort(tasks);

    }

    public void update(){

        if(tasks.size()>0 && tasks.get(0).time<(int)(System.currentTimeMillis()/1000)){
            UpgradingFieldTask task = (UpgradingFieldTask)tasks.get(0);
            try {
                connection.upgradingField(task.idField);
            } catch (LoadHttpPageException ex) {
                Logger.info("Can't load page");
            }
            tasks.remove(0);
        }

    }

}
