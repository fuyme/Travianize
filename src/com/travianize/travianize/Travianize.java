package com.travianize.travianize;

import com.travianize.travianize.utils.Logger;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Travianize {

    private static final String VERSION = "0.0.1";
    private static final String accountsInfoFileName = "accounts.conf";
    private static Travianize travianize;

    private class AccountInfo {

        public final String login;
        public final String password;
        public final String serverHostName;

        private AccountInfo(String serverHostName, String login, String password) {

            this.serverHostName = serverHostName;
            this.login = login;
            this.password = password;

        }
    }

    private AccountInfo[] loadAccountsInfo(String accountsInfoFileName) {

        File accountsInfoFile = new File("./" + accountsInfoFileName);
        if (!accountsInfoFile.exists()) {

            Logger.info("Can't find '" + accountsInfoFileName + "' file.");
            Logger.info("Try create file...");

            try {
                accountsInfoFile.createNewFile();
                Logger.info("File has been created.");
            } catch (IOException ex) {
                Logger.info("Can't create file.");
            }

            return new AccountInfo[0];

        }

        DataInputStream inputStream = null;

        try {

            inputStream = new DataInputStream(new FileInputStream("./" + accountsInfoFileName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String currectFileLine;

            while ((currectFileLine = reader.readLine()) != null) {

                System.out.println(currectFileLine);

            }

            return new AccountInfo[0];

        } catch (IOException ex) {

            Logger.info("Can't read '" + accountsInfoFileName + "'. Check your permission.");
            return null;

        } finally {

            try {
                inputStream.close();
            } catch (IOException ex) {}

        }

    }

    public Travianize() {

        Logger.info("Travianize ver." + VERSION);

        AccountInfo[] accountsInfo = loadAccountsInfo(accountsInfoFileName);

        if(accountsInfo == null){

            Logger.info("Fatal error in loading accounts...");
            //TODO: Add exception. Programm must exit
            return;

        }

        if(accountsInfo.length == 0){

            Logger.info("Can't find any accounts. Use 'host:login:password' to add new accounts");
            //TODO: Add exception. Programm must exit
            return;

        }

        Logger.info("Loaded accounts:");
        for(AccountInfo accountInfo: accountsInfo){

            Logger.info(accountInfo.serverHostName+" "+accountInfo.login+" "+accountInfo.password);

        }

    }

    public void start() {
        //...
    }

    public static void main(String[] args) {

        travianize = new Travianize();
        travianize.start();

    }
}
