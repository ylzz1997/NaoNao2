package com.naonao.lhy.naonao2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.naonao.lhy.naonao2.bean.Nao;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class newTime extends AppCompatActivity {
    final static int ACTION_CHOOSE = 1;
    final static int ACTION_CROP = 2;


    private static String IMAGE_FILE_LOCATION = "file://" + Environment.getExternalStorageDirectory().getPath() + "/";
    private Uri imageUri;
    private boolean isImage =false;
    private Calendar calendar = null;
    private int nowyear;
    private int nowday;
    private AlarmManager alarmManager;

    Button Timebtn;
    Button DateBtn;
    Button upImage;
    Button okbtn;
    ImageView bigImage;
    EditText name;
    EditText title;
    EditText breif;

    Nao nao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_time);
        MyApplication application = (MyApplication) this.getApplication();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        IMAGE_FILE_LOCATION += application.getNext()+".jpg";
        imageUri = Uri.parse(IMAGE_FILE_LOCATION);
        Log.d("ces", "onCreate: "+IMAGE_FILE_LOCATION);
        Timebtn = findViewById(R.id.TimeBtn);
        DateBtn = findViewById(R.id.DateBtn);
        upImage = findViewById(R.id.upImage);
        bigImage = findViewById(R.id.bigpictureKan);
        okbtn = findViewById(R.id.yesBtn);
        name = findViewById(R.id.newName);
        title = findViewById(R.id.newTitle);
        breif = findViewById(R.id.newBrief);

        calendar = Calendar.getInstance();
        nowyear = calendar.get(Calendar.YEAR);
        nowday = calendar.get(Calendar.DAY_OF_MONTH);
        nao = new Nao();


        DateBtn.setOnClickListener((v)->{
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String mon;
                    String day;

                    if((month+1)/10 == 0){
                        mon = "0"+(month+1);
                    }else {
                        mon = ""+(month+1);

                    }

                    if(dayOfMonth/10== 0){
                        day = "0"+dayOfMonth;
                    }else {
                        day = ""+dayOfMonth;

                    }

                   DateBtn.setText(year+"-"+mon+"-"+day);
                }
            }, nowyear, calendar.get(Calendar.MONTH), nowday).show();
        });

        Timebtn.setOnClickListener((v)->{
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String hour = "00" ;
                    String min = "00";
                    if(hourOfDay/10 == 0){
                        hour = "0"+hourOfDay;
                    }else{
                        hour = hourOfDay+"";
                    }

                    if(minute/10 == 0){
                        min = "0"+minute;
                    }else{
                        min = minute+"";
                    }
                    Timebtn.setText(hour+":"+min);
                }
            },0,0, true).show();
        });

        upImage.setOnClickListener((v)->{
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
            startActivityForResult(intent,ACTION_CHOOSE);
        });

        okbtn.setOnClickListener((v)->{
            Date date = null;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                date = df.parse(DateBtn.getText().toString()+" "+Timebtn.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            nao.time = date;
            nao.name = name.getText().toString();
            nao.Title = title.getText().toString();
            nao.Brief = breif.getText().toString();
            nao.isEnable = true;
            nao.id = application.getNext();
            application.getNaoList().add(nao);


            Intent servitnt = new Intent(newTime.this,Notice.class);
            servitnt.putExtra("id",application.getNext());
            PendingIntent pi = PendingIntent.getService(newTime.this, application.getNext(),servitnt, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.set(AlarmManager.RTC_WAKEUP,date.getTime(),pi);
            application.setNext(application.getNext()+1);
            Intent it = new Intent(this,MainActivity.class);
            startActivity(it);
            finish();
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_CHOOSE:
                    startPhotoZoom(data.getData());
                    break;
                case ACTION_CROP:
                    try {
                        bigImage.setImageBitmap(BitmapFactory.decodeStream( getContentResolver().openInputStream(imageUri)));
                        nao.bigPicture=IMAGE_FILE_LOCATION;
                        isImage = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 200);

        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);

        startActivityForResult(intent, ACTION_CROP);
    }
}
