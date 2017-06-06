package com.example.hyejin.imageprocessing;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static com.example.hyejin.imageprocessing.BaseActivity.KEY_BITMAP;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ImageView detectEdgesImageView;



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
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.image_view);
        detectEdgesImageView = (ImageView)findViewById(R.id.detect_edges_image_view);

        try {
            detectEdges(BitmapHelper.readBitmapFromPath(this, getUriFromPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/rabbit.png";

        Uri fileUri = Uri.parse(path);
        String filePath = fileUri.getPath();
        Cursor c = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data ='"+filePath+"'",null, null);

        c.moveToNext();
        int id = c.getInt(c.getColumnIndex("_id"));
        Uri uri_static = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri_static;
    }

}
