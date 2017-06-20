package com.example.maliniramki.doodle;

import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.content.Intent;

public class ConnectWithUserActivity extends AppCompatActivity {

    Cursor cursor;
    SQLiteDatabase db;
    String un;
    ImageView pp;
    Button b1,b2;
    Bitmap profilePic;
    byte[] profileBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_with_user);
        db=openOrCreateDatabase("doodlipi4",MODE_PRIVATE,null);
        un = getIntent().getStringExtra("uname");
        TextView nameDisplay = (TextView) findViewById(R.id.textView5);
        nameDisplay.setText(un);
        cursor=db.rawQuery("SELECT * FROM doodlers WHERE username='" + un + "';",null);
        cursor.moveToFirst();
        profileBytes=cursor.getBlob(cursor.getColumnIndex("profilePic"));
        /*profilePic=BitmapFactory.decodeByteArray(profileBytes, 0, profileBytes.length);
        pp.setImageBitmap(profilePic);*/
        b1=(Button)findViewById(R.id.button8);
        b2=(Button)findViewById(R.id.button9);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1=new Intent(ConnectWithUserActivity.this,IntrovertDoodle.class);
                startActivity(in1);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2=new Intent(ConnectWithUserActivity.this,WithFriendsActivity.class);
                startActivity(in2);
            }
        });
    }
}
