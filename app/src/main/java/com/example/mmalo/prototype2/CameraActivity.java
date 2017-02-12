package com.example.mmalo.prototype2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mmalo.prototype2.Controllers.CameraPreview;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;

import static android.R.attr.id;
import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.example.mmalo.prototype2.Controllers.CameraController.getCameraInstance;
import static com.example.mmalo.prototype2.Controllers.CameraController.getOutputMediaFile;

/**
 * Created by mmalo on 27/10/2016.
 */
public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private boolean cancelFlag;
    Bitmap bitmap;
    Timestamp theTime;
    String filename;
    byte[] dataToPass;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);

        cancelFlag = false;
        // Create an instance of Camera
        mCamera = getCameraInstance();

        if (mCamera == null) {
            releaseCamera();
            Toast t = Toast.makeText(this, "UNABLE to get camera instance", Toast.LENGTH_LONG);
            t.show();
            //Intent i = new Intent(this, MainActivity.class);
            //startActivity(i);

        } else

        {
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            mCamera.setDisplayOrientation(90);
            preview.addView(mPreview);

        }

    }

    public void takePicClick(View v) {
        // get an image from the camera
        //mCamera.setDisplayOrientation(270);

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        theTime = ts;


        mCamera.takePicture(null, null, mPicture);

    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {


            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            //afterTaken(bitmap,data);
            //dataToPass= data;

            ByteArrayOutputStream str = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, str);
            dataToPass = str.toByteArray();


            Matrix rotationMat = new Matrix();
            //rotationMat.postRotate(90);
            rotationMat.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotationMat, true);
            afterTaken(bitmap, dataToPass);


        }
    };





    public void afterTaken(Bitmap bmp, byte[] photoData) {
        ImageView iv = (ImageView) findViewById(R.id.frame_taken);
        iv.setImageBitmap(bmp);
        iv.setVisibility(View.VISIBLE);

        FrameLayout fl = (FrameLayout) findViewById(R.id.camera_preview);
        fl.setVisibility(View.INVISIBLE);

        cancelFlag = !cancelFlag;

        Button confirm = (Button) findViewById(R.id.button_cont);
        confirm.setVisibility(View.VISIBLE);

        Button capture = (Button) findViewById(R.id.button_capture);
        capture.setVisibility(View.INVISIBLE);
    }

    public void continueForm(View v) {
        //Pass data to next activity then release camera then load activity
        //saveImageToFile(dataToPass);
        //readImageFromFile(filename);

        PTakenActivity.photoData = dataToPass;

        //commented here
        //Matrix rotationMat = new Matrix();
        //rotationMat.postRotate(90);
        //rotationMat.postRotate(270);
        //bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotationMat, true);

        //PTakenActivity.thePic = bitmap;
        PTakenActivity.timetaken = theTime;
        PTakenActivity.filename = theTime.toString();
        //saveImageToFile(dataToPass);

        releaseCamera();
        bitmap.recycle();
        dataToPass = null;
        System.gc();


        Intent i = new Intent(this, PTakenActivity.class);
        this.startActivity(i);
    }


    @Override
    public void onBackPressed() {

        releaseCamera();
        super.onBackPressed();
    }

    public void skipPicTake(View v) {
        releaseCamera();
        Intent i = new Intent(this, PTakenActivity.class);
        this.startActivity(i);

    }

    public void cancelPic(View v) {

        if (!cancelFlag) {
            //In preview Mode, cancel releases camera and returns to main options
            releaseCamera();
            Intent i = new Intent(this, OptionsActivity.class);
            this.startActivity(i);
        } else {
            //Pic Taken, cancel resets camera to take another photo
            cancelFlag = !cancelFlag;
            Button confirm = (Button) findViewById(R.id.button_cont);
            confirm.setVisibility(View.INVISIBLE);

            Button capture = (Button) findViewById(R.id.button_capture);
            capture.setVisibility(View.VISIBLE);
        }
    }


    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }



}
