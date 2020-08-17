package org.itstep;

import java.io.Serializable;

public class Task implements Serializable, Comparable<Task> {
    private final String description;
    private final Integer priority;
    private final String date;
    private boolean isDone;

    public Task(String description, int priority, String date, boolean isDone) {
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.isDone = isDone;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int compareTo(Task o) {
        if (isDone) {
            return o.date.compareTo(date);
        }
        if (o.priority >= priority) {
            return date.compareTo(o.date);
        }
        return o.priority - priority;
    }

    @Override
    public String toString() {
        return "|" + description + "| |Приоритет: " + priority + "| |дата: " + date +"|";
    }
}
