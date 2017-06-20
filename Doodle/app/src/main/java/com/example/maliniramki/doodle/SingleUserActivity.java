package com.example.maliniramki.doodle;

import android.widget.ImageView;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.graphics.drawable.BitmapDrawable;
import android.widget.LinearLayout;
import java.io.File;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.util.Random;

public class SingleUserActivity extends AppCompatActivity {

    String imgDecodableString;
    int RESULT_LOAD_IMG=1;
    DrawingView mDrawingView;
    //ImageView imageView;
    LinearLayout mDrawingPad;
    Button save,test,loadFromGallery;
    EditText saveAsName;
    String saveAs,imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);
        mDrawingView = new DrawingView(this);
        mDrawingView = new DrawingView(this);
        mDrawingPad = (LinearLayout) findViewById(R.id.view_drawing_pad);
        mDrawingPad.addView(mDrawingView);
        test = (Button) findViewById(R.id.button14);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SingleUserActivity.this,testimage.class);
                startActivity(i);
            }});
        save = (Button) findViewById(R.id.button12);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAsName = (EditText) findViewById(R.id.editText6);
                saveAs = "myDoodle"+saveAsName.getText().toString();
                mDrawingPad.buildDrawingCache();
                Bitmap bmap = mDrawingPad.getDrawingCache();
                ImageView iv=(ImageView)findViewById(R.id.imageView4);
                iv.setImageBitmap(bmap);
                saveImage(bmap);
                Intent i=new Intent(SingleUserActivity.this,ShareDoodle.class);
                i.putExtra("imagePath",imagePath);
                startActivity(i);
            }
        });
        loadFromGallery = (Button) findViewById(R.id.button10);
        loadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImagefromGallery(v);
            }
        });
    }
    public void saveImage(Bitmap img){
        String RootDir = Environment.getExternalStorageDirectory()
                + File.separator + "doodlipi";
        File myDir=new File(RootDir);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "image-" + saveAs + n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), file.getAbsolutePath(),Toast.LENGTH_LONG).show();
    }
    public void setImagefromGallery(View view) {
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
                Bitmap bmImg = BitmapFactory.decodeFile(imgDecodableString);
                BitmapDrawable background = new BitmapDrawable(bmImg);
                mDrawingPad.setBackgroundDrawable(background);

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

}

class DrawingView extends View {
    Paint   mPaint;
    Bitmap  mBitmap;
    Canvas  mCanvas;
    Path    mPath;
    Paint   mBitmapPaint;


    public DrawingView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
        mPath = new Path();
        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(Color.BLUE);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
}