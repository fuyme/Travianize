package com.travianize.travianize.exception;

import com.travianize.travianize.parsers.pages.UpgradingFieldPage;

public class UpgradingAvailableException extends Exception{
    private static final long serialVersionUID = 1124523544564231657L;
    private UpgradingFieldPage upgradingFieldPage;

    public UpgradingAvailableException(UpgradingFieldPage page){

        this.upgradingFieldPage = page;

    }

    public UpgradingFieldPage getUpgradingFieldPage() {
        return upgradingFieldPage;
    }
}
