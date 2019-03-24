package com.naonao.lhy.naonao2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.naonao.lhy.naonao2.bean.Nao;

import org.litepal.LitePal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Nao> naoList;
    private NaoAdapter adapter;
    MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LitePal.getDatabase();

        application = (MyApplication) this.getApplication();
        naoList = application.getNaoList();

        RecyclerView recyslerview = (RecyclerView) findViewById(R.id.naoList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyslerview.setLayoutManager(layoutmanager);
        adapter = new NaoAdapter(this,naoList);

        recyslerview.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.main_menu_add:
                Intent newtime = new Intent(this,newTime.class);
                startActivity(newtime);
                break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        naoList = application.getNaoList();
        adapter.notifyDataSetChanged();
    }
}
