package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Signup extends AppCompatActivity {

    DBHelper DB;
    FirebaseDatabase firebase;
    DatabaseReference reference;
    int SELECT_IMAGE_CODE=1;
    Uri uri=null;
    ImageView image;
    Boolean insert=false;
    Boolean user_already=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebase=FirebaseDatabase.getInstance();
        reference=firebase.getReference("users");

        image=(ImageView) findViewById(R.id.dp);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Title"), SELECT_IMAGE_CODE);
            }
        });
        Button btnsignup = findViewById(R.id.btn1);
        Button btnsignin = findViewById(R.id.btn2);
        EditText et1 = findViewById(R.id.et1);
        EditText et2 = findViewById(R.id.et2);
        EditText et3 = findViewById(R.id.et3);
        EditText et4 = findViewById(R.id.et4);

        DB = new DBHelper(this);


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uri!=null)
                {
                    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
                    String fullname = et1.getText().toString();
                    String email = et2.getText().toString();
                    String username = et3.getText().toString();
                    String password = et4.getText().toString();

                    if (fullname.equals("") ||email.equals("")||username.equals("")||password.equals("")||uri==null) {
                        Toast.makeText(Signup.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        Boolean checkuser = DB.checkusername(username);
                        if (checkuser==false){
                            //checkuser=true;
                            //insert = DB.insertData(fullname,email,username,password);
                            user_already=true;
                        }
                        else {
                            Toast.makeText(Signup.this, "User Already exists please signin", Toast.LENGTH_SHORT).show();
                            user_already=false;
                            return;
//                            Intent intent = new Intent(getApplicationContext(), Login.class);
//                            startActivity(intent);
                        }
                    }
                    if(user_already) {
                        user_already=true;
                        storageReference = storageReference.child("abc/" + username + ".jpg");
                        storageReference.putFile(uri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();

                                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                String dp = uri.toString();
                                                insert = DB.insertData(fullname,email,username,password);
                                                User_data user = new User_data(fullname, email, username, password, dp);
                                                reference.child(username).setValue(user);
                                                if (insert == true) {
                                                    String notification_msg="User has been Registered Successfully";
                                                    NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext())
                                                            .setSmallIcon(R.drawable.ic_not_message)
                                                            .setContentTitle("User Registration")
                                                            .setContentText(notification_msg)
                                                            .setAutoCancel(true)
                                                            .setPriority(NotificationCompat.PRIORITY_HIGH);
                                                    NotificationManager notificationManager=(NotificationManager)getSystemService(
                                                            Context.NOTIFICATION_SERVICE
                                                    );
                                                    Intent intent2 = new Intent(getApplicationContext(), adminpanel.class);
                                                    PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent2
                                                            ,PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);

                                                    NotificationChannel channel = new NotificationChannel(
                                                            "1",
                                                            "Kat",
                                                            NotificationManager.IMPORTANCE_HIGH
                                                    );
                                                    if (notificationManager != null) {
                                                        notificationManager.createNotificationChannel(channel);
                                                    }
                                                    builder.setChannelId("1");
                                                    notificationManager.notify(0,builder.build());
                                                    Toast.makeText(Signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(Signup.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Failed To Upload Picture", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed To Upload Picture", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Select an Image",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            uri=data.getData();
            image.setImageURI(uri);
            //transferring_image_data(uri);
            }}}
//sign up