package com.travianize.travianize;

import com.travianize.travianize.utils.Logger;

public class Travianize {

    private static Travianize travianize;

    public Travianize(){

        Logger.info("Travianize");

    }

    public void start() {

        //...

    }

    public static void main(String[] args){

        travianize = new Travianize();
        travianize.start();

    }

}
