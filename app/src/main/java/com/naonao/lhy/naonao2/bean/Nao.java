package com.naonao.lhy.naonao2.bean;



import android.app.PendingIntent;

import java.io.Serializable;
import java.util.Date;

public class Nao{
    public int id;
    public Date time;
    public String name;
    public String Title;
    public String Brief;
    public String bigPicture;
    public boolean isEnable;

    public Nao() {}

    public Nao(Date time, String name, String title, String brief, String bigPicture, boolean isEnable) {
        this.time = time;
        this.name = name;
        Title = title;
        Brief = brief;
        this.bigPicture = bigPicture;
        this.isEnable = isEnable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBrief() {
        return Brief;
    }

    public void setBrief(String brief) {
        Brief = brief;
    }

    public String getBigPicture() {
        return bigPicture;
    }

    public void setBigPicture(String bigPicture) {
        this.bigPicture = bigPicture;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
