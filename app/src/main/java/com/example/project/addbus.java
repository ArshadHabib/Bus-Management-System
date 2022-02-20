package com.example.project;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class addbus extends AppCompatActivity {
    String id ;
    String departure ;
    String arrival ;
    String date ;
    String total_seats;
    //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    //StrictMode.setThreadPolicy(policy);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbus);
        EditText busid = findViewById(R.id.id);
        EditText from = findViewById(R.id.from);
        EditText to = findViewById(R.id.to);
        EditText dt = findViewById(R.id.date);
        EditText seats = findViewById(R.id.seats);
        Button addbus = findViewById(R.id.addbus);
        Button back1 =findViewById(R.id.button2);
        DBHelper DB = new DBHelper(this);
        addbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = busid.getText().toString();
                String departure = from.getText().toString();
                String arrival = to.getText().toString();
                String date = dt.getText().toString();
                String total_seats = seats.getText().toString();
                if (id.equals("") || departure.equals("") || arrival.equals("") || date.equals("") || total_seats.equals(""))
                {
                    Toast.makeText(addbus.this, "Please enter all fields", Toast.LENGTH_SHORT).show(); }
                else
                {
                    Boolean checkid= DB.checkid(id);
                    if (checkid==false){
                        Boolean insert = DB.insertBus(id,departure,arrival,date,total_seats);
                        if (insert==true){
                            Toast.makeText(addbus.this, "Bus has been added", Toast.LENGTH_SHORT).show();
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try  {
                                        HttpParams params = new BasicHttpParams();
                                        HttpConnectionParams.setConnectionTimeout(params, 5000);
                                        HttpClient httpclient = new DefaultHttpClient(params);
                                        HttpPost httppost = new HttpPost("http://192.168.43.26/kat/insert.php");
                                        BasicNameValuePair bus_id = new BasicNameValuePair("bus_id", id);
                                        BasicNameValuePair bus_departure = new BasicNameValuePair("bus_departure", departure);
                                        BasicNameValuePair bus_destination = new BasicNameValuePair("bus_destination", arrival);
                                        BasicNameValuePair bus_date = new BasicNameValuePair("bus_date", date);
                                        BasicNameValuePair bus_seats = new BasicNameValuePair("bus_seats", total_seats);
                                        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                                        nameValuePairList.add(bus_id);
                                        nameValuePairList.add(bus_departure);
                                        nameValuePairList.add(bus_destination);
                                        nameValuePairList.add(bus_date);
                                        nameValuePairList.add(bus_seats);
                                        try {
                                            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                                            httppost.setEntity(urlEncodedFormEntity);
                                            HttpResponse response = httpclient.execute(httppost);
                                        } catch (Exception e) {
                                            String a = e.toString();
                                            Toast t = Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT);
                                            t.show(); }
                                    } catch (Exception e) {
                                        e.printStackTrace(); } }});
                            thread.start();
                            String notification_msg="Bus has been added Successfully";
                            NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.ic_not_message)
                                    .setContentTitle("Bus Addition")
                                    .setContentText(notification_msg)
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH);
                            NotificationManager notificationManager=(NotificationManager)getSystemService(
                                    Context.NOTIFICATION_SERVICE);
                            Intent intent = new Intent(getApplicationContext(), adminpanel.class);
                            PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent
                                    ,PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(pendingIntent);
                            NotificationChannel channel = new NotificationChannel(
                                    "1",
                                    "Kat",
                                    NotificationManager.IMPORTANCE_HIGH
                            );
                            if (notificationManager != null) {
                                notificationManager.createNotificationChannel(channel); }
                            builder.setChannelId("1");
                            notificationManager.notify(0,builder.build());
                            startActivity(intent); }
                        else {
                            Toast.makeText(addbus.this, "ERROR", Toast.LENGTH_SHORT).show(); } }
                    else {
                        String notification_msg="Bus has NOT been added";
                        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_not_message)
                                .setContentTitle("Bus Addition")
                                .setContentText(notification_msg)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_HIGH);
                        NotificationManager notificationManager=(NotificationManager)getSystemService(
                                Context.NOTIFICATION_SERVICE);
                        Intent intent = new Intent(getApplicationContext(), adminpanel.class);
                        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent
                                ,PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);
                        NotificationChannel channel = new NotificationChannel(
                                "1",
                                "Kat",
                                NotificationManager.IMPORTANCE_HIGH);
                        if (notificationManager != null) {
                            notificationManager.createNotificationChannel(channel); }
                        builder.setChannelId("1");
                        notificationManager.notify(0,builder.build());
                        Toast.makeText(addbus.this, "ID already exists", Toast.LENGTH_SHORT).show();
                    } } }});
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), adminpanel.class);
                startActivity(intent);
            }}); }}