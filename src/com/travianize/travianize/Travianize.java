package com.travianize.travianize;

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

public class Travianize {

    public static final String VERSION = "0.0.2";
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

                //TODO: ':' in password
                String[] accountParts = currentFileLine.split(":");

                if (accountParts.length < 3) {
                    continue;
                }

                AccountInfo accountInfo = new AccountInfo(accountParts[0], accountParts[1], accountParts[2]);

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

    public Travianize() {

        Logger.info(APPLICATION_NAME + " ver. " + VERSION);

        AccountInfo[] accountsInfo = loadAccountsInfo(accountsInfoFileName);

        if (accountsInfo == null) {

            Logger.info("Fatal error in loading accounts...");
            //TODO: Add exception. Programm must exit
            return;

        }

        if (accountsInfo.length == 0) {

            Logger.info("Can't find any accounts. Use 'host:login:password' to add new accounts");
            //TODO: Add exception. Programm must exit
            return;

        }

        Logger.info("Loaded accounts:");
        for (AccountInfo accountInfo : accountsInfo) {

            Logger.info(accountInfo.serverHostName + " " + accountInfo.login + " " + accountInfo.password);

        }

        accounts = new Account[accountsInfo.length];

        for (int i = 0; i < accountsInfo.length; i++) {
            //TODO: some error if data is wrong
            accounts[i] = new Account(accountsInfo[i].serverHostName, accountsInfo[i].login, accountsInfo[i].password);

        }

    }

    public void start() {

        while (true) {
            for (Account account : accounts) {
                account.update();
            }
        }

    }

    public static void main(String[] args) {

        travianize = new Travianize();
        travianize.start();

    }
}
