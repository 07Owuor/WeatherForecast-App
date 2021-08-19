package com.tonny.mm.Models;

import java.io.Serializable;

/**
 * Created by Nitrozeus on 19/08/2021.
 */

public class Task implements Serializable {

    String title, task_date, status;

    public Task() {
    }

    public Task(String title, String task_date, String status) {
        this.title = title;
        this.task_date = task_date;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
