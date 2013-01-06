package com.travianize.travianize.travian;

import com.travianize.travianize.connection.TravianConnector;
import com.travianize.travianize.exception.LoadHttpPageException;
import com.travianize.travianize.exception.UpgradingAvailableException;
import com.travianize.travianize.parsers.pages.Dorf1Page;
import com.travianize.travianize.parsers.pages.UpgradingFieldPage;
import com.travianize.travianize.travian.tasks.Task;
import com.travianize.travianize.travian.tasks.UpgradingBuildingTask;
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

    private String login;
    private String password;

    private int resourcesLastUpdate, productionsLastUpdate, buildingProductionsLastUpdate;
    private int[] resources;
    private int[] productions;
    private int[] buildingProductions;

    public Account(String serverHostName, String login, String password) {

        this.login = login;
        this.password = password;

        try {
            connection = new TravianConnector(serverHostName, this);

            try {
                Logger.info("Try login...");
                connection.login(login, password);
                Logger.info("Load tasks from file 'task_" + serverHostName + "_" + login + ".txt'");
                tasks = loadTaskFromFile("task_" + serverHostName + "_" + login + ".txt");

            } catch (LoadHttpPageException e) {
                //TODO: add exception
                Logger.info("Can't load page");
            }

            Logger.info("Success creation account '" + login + "', password: '" + password + "' for host '" + serverHostName + "'");

        } catch (UnknownHostException ex) {
            //TODO: add exception
            Logger.info("Unknown Host '" + serverHostName + "'");
        }

    }

    public void update() {

        if (tasks.isEmpty()) {
            complite = true;
        }

        if (!tasks.isEmpty() && tasks.peek().time < (int) (System.currentTimeMillis() / 1000)) {
            //TODO: task isn't always upgrading field task
            Task task = tasks.peek();
            try {

                if (task instanceof UpgradingFieldTask) {
                    connection.upgradingField(((UpgradingFieldTask) task).idField);
                } else if (task instanceof UpgradingBuildingTask) {
                    connection.upgradingBuilding(((UpgradingBuildingTask) task).idField);
                }
            } catch (LoadHttpPageException ex) {
                Logger.info("Can't load page");
                return;
            } catch (UpgradingAvailableException ex) {
                if (buildingProductions != null && buildingProductions.length != 0) {
                    int time = buildingProductions[buildingProductions.length - 1];
                    task.time = buildingProductionsLastUpdate + time + 10;
                    Logger.info("Already upgrading filed. Wait " + time + " sec");
                } else {

                    UpgradingFieldPage page = ex.getUpgradingFieldPage();
                    if (page.requiredResourses != null && resources != null && productions != null) {
                        int time = 0;

                        for (int i = 0; i < 4; i++) {
                            int resorceTime = (int) Math.floor((page.requiredResourses[i] - resources[i]) / (double) productions[i] * 3600) + resourcesLastUpdate;

                            if (time < resorceTime) {
                                time = resorceTime;
                            }
                        }
                        Logger.info("Wait resource to " + time + " ");
                        task.time = time + 60;
                    } else {
                        task.time = task.time + 300;
                    }
                }

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
                String[] stringTask = currentFileLine.split(":");
                if (stringTask[0].equals("f")) {
                    UpgradingFieldTask task = new UpgradingFieldTask();
                    task.idField = Integer.parseInt(stringTask[1]);
                    task.time = 0;
                    tasksList.add(task);
                } else {
                    UpgradingBuildingTask task = new UpgradingBuildingTask();
                    task.idField = Integer.parseInt(stringTask[1]);
                    task.time = 0;
                    tasksList.add(task);
                }

            }

            return tasksList;

        } catch (IOException ex) {

            Logger.info("Can't read '" + filename + "'. Check your permission.");
            return new LinkedList<Task>();

        } finally {

            try {
                inputStream.close();
            } catch (IOException ex) {
            }

        }

    }

    public boolean isComplite() {
        return complite;
    }

    public void updateFromDorf1Page(Dorf1Page page) {

        if (page.resources != null) {
            this.resources = page.resources;
            this.resourcesLastUpdate = (int) (System.currentTimeMillis() / 1000);
        }

        if (page.productions != null) {
            this.productions = page.productions;
            this.productionsLastUpdate = (int) (System.currentTimeMillis() / 1000);
        }

        if (page.buildingProductions != null) {
            this.buildingProductions = page.buildingProductions;
            this.buildingProductionsLastUpdate = (int) (System.currentTimeMillis() / 1000);
        }
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}