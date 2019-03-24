package com.naonao.lhy.naonao2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.naonao.lhy.naonao2.bean.Nao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class NaoAdapter extends RecyclerView.Adapter<NaoAdapter.ViewHolder>{

    private ArrayList<Nao> mData;
    private Context context;

    public NaoAdapter(Context context,ArrayList<Nao> mData){
        this.mData=mData;
        this.context=context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView date;
        TextView title;
        ToggleButton naoSwitch;

        public ViewHolder(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.list_title);
            naoSwitch = itemView.findViewById(R.id.nao_switch);
        }
    }


    @NonNull
    @Override
    public NaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_nao_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NaoAdapter.ViewHolder holder, int position) {
        Date dateTime = mData.get(position).time;
        DateFormat tdf = new SimpleDateFormat("HH:mm");
        DateFormat ddf = new SimpleDateFormat("yyyy-MM-dd E");
        holder.date.setText(ddf.format(dateTime));
        holder.time.setText(tdf.format(dateTime));
        holder.title.setText(mData.get(position).name);
        holder.naoSwitch.setChecked(mData.get(position).isEnable);

        holder.naoSwitch.setOnCheckedChangeListener((v,checked)->{
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            if(checked){
                Intent servitnt = new Intent(context,Notice.class);
                servitnt.putExtra("id",mData.get(position).id);
                PendingIntent pi = PendingIntent.getService(context,mData.get(position).id,servitnt, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP,dateTime.getTime(),pi);
            }else {
                Intent servitnt = new Intent(context,Notice.class);
                PendingIntent pi = PendingIntent.getService(context,mData.get(position).id,servitnt, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pi);
            }
            mData.get(position).isEnable = checked;
            String naobunao;
            naobunao = checked?"准备闹你了":"停止吵闹";
            Toast.makeText(context,"提醒"+mData.get(position).name+naobunao+"了！",Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
