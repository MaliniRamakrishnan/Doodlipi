package com.example.maliniramki.doodle;

import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor c;
    Button login;
    EditText un,pswd;
    String u,p,query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=openOrCreateDatabase("doodlipi4",MODE_PRIVATE,null);
        setContentView(R.layout.activity_login);
        login=(Button)findViewById(R.id.button4);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                un=(EditText)findViewById(R.id.editText); u=un.getText().toString();
                pswd=(EditText)findViewById(R.id.editText2); p=pswd.getText().toString();
                query="SELECT * FROM doodlers WHERE username='" + u + "' AND password ='" + p + "';";
                c=db.rawQuery(query,null);
                if(c.getCount()>0)
                    {
                        c.moveToFirst();
                        Intent i=new Intent(LoginActivity.this,ConnectWithUserActivity.class);
                        i.putExtra("uname",u);
                        startActivity(i);
                    }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid credentials.", Toast.LENGTH_SHORT).show();
                    un.setText("");
                    pswd.setText("");
                }
            }
        });
    }
}
