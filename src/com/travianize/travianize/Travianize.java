package com.travianize.travianize;

import com.travianize.travianize.exception.LoadProgrammException;
import com.travianize.travianize.exception.LoginException;
import com.travianize.travianize.travian.Account;
import com.travianize.travianize.utils.Logger;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Travianize {

    public static final String VERSION = "0.0.3";
    public static final String APPLICATION_NAME = "Travianize";
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
    private Account[] accounts;

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

            String currentFileLine;
            List<AccountInfo> accountsInfoList = new ArrayList<AccountInfo>();

            while ((currentFileLine = reader.readLine()) != null) {

                String[] accountParts = currentFileLine.split(":");

                if (accountParts.length < 3) {
                    continue;
                }

                String password = "";

                for(int i=2;i<accountParts.length;i++){
                    password+=accountParts[i] + ":";
                }

                password = password.substring(0, password.length()-1);

                AccountInfo accountInfo = new AccountInfo(accountParts[0], accountParts[1], password);

                accountsInfoList.add(accountInfo);

            }

            return accountsInfoList.toArray(new AccountInfo[0]);

        } catch (IOException ex) {

            Logger.info("Can't read '" + accountsInfoFileName + "'. Check your permission.");
            return null;

        } finally {

            try {
                inputStream.close();
            } catch (IOException ex) {
            }

        }

    }

    public Travianize() throws LoadProgrammException {

        Logger.info(APPLICATION_NAME + " ver. " + VERSION);

        AccountInfo[] accountsInfo = loadAccountsInfo(accountsInfoFileName);

        if (accountsInfo == null) {

            Logger.info("Fatal error in loading accounts...");
            throw new LoadProgrammException();

        }

        if (accountsInfo.length == 0) {

            Logger.info("Can't find any accounts. Use 'host:login:password' to add new accounts");
            throw new LoadProgrammException();

        }

        Logger.info("Loaded accounts:");
        for (AccountInfo accountInfo : accountsInfo) {

            Logger.info(accountInfo.serverHostName + " " + accountInfo.login + " " + accountInfo.password);

        }

        accounts = new Account[accountsInfo.length];

        for (int i = 0; i < accountsInfo.length; i++) {
            try {
                accounts[i] = new Account(accountsInfo[i].serverHostName, accountsInfo[i].login, accountsInfo[i].password);
            } catch (LoginException ex) {
                Logger.info("Can't login as "+accountsInfo[i].login+" on server "+accountsInfo[i].serverHostName);
                accounts[i] = null;
            }

        }

    }

    public void start() {

        int updatedAccount = 0;

        do {
            updatedAccount = 0;
            for (Account account : accounts) {
                if(account != null){
                    if (!account.isComplite()) {
                        account.update();
                        updatedAccount++;
                    }
                }

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        } while (updatedAccount != 0);


    }

    public static void main(String[] args) {
        try {
            travianize = new Travianize();
            travianize.start();
        } catch (LoadProgrammException ex) {
            Logger.info("Fatal error in loading...");
        }

    }
}
