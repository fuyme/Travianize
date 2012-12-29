package com.travianize.travianize;

public class Travianize {

    private static Travianize travianize;

    public Travianize(){

        System.out.println("Travianize");

    }

    public void start() {

        //...

    }

    public static void main(String[] args){

        travianize = new Travianize();
        travianize.start();

    }

}
