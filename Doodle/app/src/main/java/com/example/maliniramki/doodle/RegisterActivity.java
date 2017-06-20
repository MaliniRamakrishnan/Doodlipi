package com.example.maliniramki.doodle;

import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import java.io.ByteArrayOutputStream;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.content.ContentValues;
import android.database.sqlite.SQLiteException;

public class RegisterActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    EditText mail,pwd,un;
    String m,p,u;
    Button reg,clr;
    byte[] image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db=openOrCreateDatabase("doodlipi4",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS doodlers(" +
                "username VARCHAR(20) PRIMARY KEY," +
                "mailID VARCHAR(20) NOT NULL," +
                "password VARCHAR(10) NOT NULL," +
                "profilePic BLOB" +
                ");");
        clr=(Button)findViewById(R.id.button5);
        clr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mail.setText("");
                pwd.setText("");
                un.setText("");
            }
        });
        reg=(Button)findViewById(R.id.button5);
        reg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mail=(EditText)findViewById(R.id.editText3);
                pwd=(EditText)findViewById(R.id.editText4);
                un=(EditText)findViewById(R.id.editText5);
                m= mail.getText().toString();
                p= pwd.getText().toString();
                u= un.getText().toString();
                try { addEntry(); }
                catch(Exception e) { e.printStackTrace(); }
                Toast.makeText(getApplicationContext(),"Congratulations!\n" +
                        "Now enjoy distracting yourself from what's REALLY important.",Toast.LENGTH_LONG).show();
                Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
            });
        }
    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imgView);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                getBytes(BitmapFactory.decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG)
                    .show();
        }

    }
    public void addEntry() throws SQLiteException{
        ContentValues cv = new  ContentValues();
        cv.put("username",u);
        cv.put("mailID",m);
        cv.put("password",p);
        cv.put("profilePic",image);
        try{ db.insert("doodlers", null, cv ); }
        catch(Exception e) { Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show(); }
    }
    public void getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(bitmap.compress(CompressFormat.PNG, 100, stream))
        {
            image=stream.toByteArray();
            if(image.length==0) { Toast.makeText(this,"Null image",Toast.LENGTH_SHORT).show(); }
            else { Toast.makeText(this, "" + image.length,Toast.LENGTH_SHORT).show(); }
        }
        else
        {
            Toast.makeText(this,"Compression error",Toast.LENGTH_SHORT).show();
        }
    }
}
