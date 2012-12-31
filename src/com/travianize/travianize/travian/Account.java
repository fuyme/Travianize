package com.travianize.travianize.travian;

import com.travianize.travianize.Travianize;
import com.travianize.travianize.connection.TravianConnector;
import com.travianize.travianize.exception.LoadHttpPageException;
import com.travianize.travianize.travian.tasks.Task;
import com.travianize.travianize.travian.tasks.UpgradingFieldTask;
import com.travianize.travianize.utils.Logger;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class Account {

    TravianConnector connection;
    //TODO: change to queue
    ArrayList<Task> tasks = new ArrayList<Task>();
    private boolean complite = false;

    public Account(String serverHostName, String login, String password) {

        try {
            connection = new TravianConnector(serverHostName);

            try {
                Logger.info("Try login...");
                connection.login(login, password);
                Logger.info("Load tasks from file 'task_"+serverHostName+"_"+login+".txt'");
                tasks = loadTaskFromFile("task_"+serverHostName+"_"+login+".txt");

            } catch (LoadHttpPageException e) {
                //TODO: add exception
                Logger.info("Can't load page");
            }

            Logger.info("Success creation account '" + login + "', password: '" + password + "' for host '" + serverHostName + "'");

        } catch (UnknownHostException ex) {
            //TODO: add exception
            Logger.info("Unknown Host '" + serverHostName + "'");
        }

        Logger.info("Loaded tasks:");
        for(Task task: tasks){

            System.out.println(((UpgradingFieldTask)task).idField);

        }

    }

    public void update() {

        if(tasks.isEmpty()){
            complite = true;
        }
/*        if (tasks.size() > 0 && tasks.get(0).time < (int) (System.currentTimeMillis() / 1000)) {
            //tODO: task isn't always upgrading field task
            UpgradingFieldTask task = (UpgradingFieldTask) tasks.get(0);
            try {
                connection.upgradingField(task.idField);
            } catch (LoadHttpPageException ex) {
                Logger.info("Can't load page");
            }
            tasks.remove(0);
        }*/

    }

    private ArrayList<Task> loadTaskFromFile(String filename) {

        File accountsInfoFile = new File("./" + filename);

        if (!accountsInfoFile.exists()) {

            Logger.info("Can't find '" + filename + "' file.");
            Logger.info("Try create file...");

            try {
                accountsInfoFile.createNewFile();
                Logger.info("File has been created.");
            } catch (IOException ex) {
                Logger.info("Can't create file.");
            }

            return new ArrayList<Task>();

        }

        DataInputStream inputStream = null;

        try {

            inputStream = new DataInputStream(new FileInputStream("./" + filename));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String currentFileLine;
            ArrayList<Task> tasksList = new ArrayList<Task>();

            while ((currentFileLine = reader.readLine()) != null) {

                UpgradingFieldTask task = new UpgradingFieldTask();
                task.idField = Integer.parseInt(currentFileLine);
                task.time = 0;
                tasksList.add(task);

            }

            return tasksList;

        } catch (IOException ex) {

            Logger.info("Can't read '" + filename + "'. Check your permission.");
            return new ArrayList<Task>();

        } finally {

            try {
                inputStream.close();
            } catch (IOException ex) {}

        }

    }

    public boolean isComplite() {
        return complite;
    }
}