package com.travianize.travianize.travian.tasks;

public class Task implements Comparable<Task> {

    public int time;

    public int compareTo(Task task) {
        return time-task.time;
    }


}
