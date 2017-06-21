package com.example.hyejin.imageprocessing;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ImageActivity extends AppCompatActivity implements View.OnTouchListener {

    ImageView imageView;
    ImageView detectEdgesImageView;

    int pxl;  //

    //Bitmap bitmap;
    //String path;

    static {
        if (OpenCVLoader.initDebug()) {
            Log.d("TAG", "OpenCV initialize success");
        } else {
            Log.d("TAG", "OpenCV initialize failed");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = (ImageView)findViewById(R.id.image_view);
        detectEdgesImageView = (ImageView)findViewById(R.id.detect_edges_image_view);


        //path = Environment.getExternalStorageDirectory().getAbsolutePath();
        //bitmap = BitmapFactory.decodeFile(path+"/rabbit.png");

        try {
            detectEdges(BitmapHelper.readBitmapFromPath(this, getUriFromPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        detectEdgesImageView.setOnTouchListener(this);
    }

    private void detectEdges(Bitmap bitmap) {
        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);

        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, 80, 100);

        // Don't do that at home or work it's for visualization purpose.
        BitmapHelper.showBitmap(this, bitmap, imageView);
        Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, resultBitmap);
        BitmapHelper.showBitmap(this, resultBitmap, detectEdgesImageView);
    }

    public Uri getUriFromPath(){

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ant.png";
        //String path = "/storage/emulated/0/rabbit.png";

        Uri fileUri = Uri.parse(path);
        String filePath = fileUri.getPath();
        Cursor c = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data ='"+filePath+"'",null, null);

        c.moveToNext();
        int id = c.getInt(c.getColumnIndex("_id"));
        Uri uri_static = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri_static;
    }










    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        final int action = ev.getAction();
        int id = v.getId();

        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();

        switch (action) {

            case MotionEvent.ACTION_DOWN : // 화면에 손으로 터치해서 대고 있을 때

                return true;

            case MotionEvent.ACTION_MOVE :
                ImageView img = (ImageView) findViewById (R.id.detect_edges_image_view);
                img.setDrawingCacheEnabled(true);
                Bitmap imgbmp = Bitmap.createBitmap(img.getDrawingCache());
                img.setDrawingCacheEnabled(false);

                pxl = imgbmp.getPixel(evX, evY); // 맨 위에 int pxl; 선언 있음

                Log.d("pxl", " " + pxl);

                if(pxl != -16777216) {
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(500);
                }

                //Toast.makeText(ImageActivity.this, "pxl: " + pxl, Toast.LENGTH_SHORT).show();

                int redComponent = Color.red(pxl);
                int greenComponent = Color.green(pxl);
                int blueComponent = Color.blue(pxl);

                return true;

            case MotionEvent.ACTION_UP : // 화면에서 손을 떼었을 때
                // ...
                return false;
        }


        return true;
    }

}
