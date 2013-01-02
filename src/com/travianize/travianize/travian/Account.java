package com.travianize.travianize.travian;

import com.travianize.travianize.connection.TravianConnector;
import com.travianize.travianize.exception.LoadHttpPageException;
import com.travianize.travianize.exception.UpgradingAvailableException;
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
import java.util.LinkedList;
import java.util.Queue;

public class Account {

    TravianConnector connection;
    //TODO: change to queue
    Queue<Task> tasks = new LinkedList<Task>();
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

        if (!tasks.isEmpty() && tasks.peek().time < (int) (System.currentTimeMillis() / 1000)) {
            //TODO: task isn't always upgrading field task
            UpgradingFieldTask task = (UpgradingFieldTask) tasks.peek();
            try {
                connection.upgradingField(task.idField);
            } catch (LoadHttpPageException ex) {
                Logger.info("Can't load page");
                return;
            } catch (UpgradingAvailableException ex) {
                task.time = (int) (System.currentTimeMillis() / 1000) + 600;
                return;
            }
            tasks.poll();
        }

    }

    private Queue<Task> loadTaskFromFile(String filename) {

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

            return new LinkedList<Task>();

        }

        DataInputStream inputStream = null;

        try {

            inputStream = new DataInputStream(new FileInputStream("./" + filename));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String currentFileLine;
            Queue<Task> tasksList = new LinkedList<Task>();

            while ((currentFileLine = reader.readLine()) != null) {

                UpgradingFieldTask task = new UpgradingFieldTask();
                task.idField = Integer.parseInt(currentFileLine);
                task.time = 0;
                tasksList.add(task);

            }

            return tasksList;

        } catch (IOException ex) {

            Logger.info("Can't read '" + filename + "'. Check your permission.");
            return new LinkedList<Task>();

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