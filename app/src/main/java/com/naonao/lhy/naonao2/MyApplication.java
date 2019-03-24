package com.naonao.lhy.naonao2;

import com.naonao.lhy.naonao2.bean.Nao;
import java.util.ArrayList;

public class MyApplication extends org.litepal.LitePalApplication {
    private int next = 0;
    private ArrayList<Nao> naoList = new ArrayList<Nao>();

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public ArrayList<Nao> getNaoList() {
        return naoList;
    }

    public void setNaoList(ArrayList<Nao> naoList) {
        this.naoList = naoList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        next = naoList.size();
    }
}
