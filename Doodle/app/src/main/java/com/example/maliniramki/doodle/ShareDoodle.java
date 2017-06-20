package com.example.maliniramki.doodle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ShareDoodle extends AppCompatActivity {

    Button share;
    ImageView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_doodle);
        show=(ImageView)findViewById(R.id.imageView3);
        share=(Button)findViewById(R.id.button13);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath=getIntent().getStringExtra("filePath");
                Bitmap img= BitmapFactory.decodeFile(filePath);
                show.setImageBitmap(img);
                Intent i=new Intent(ShareDoodle.this,ShareConnectActivity.class);
                startActivity(i);
            }
        });
    }
}
