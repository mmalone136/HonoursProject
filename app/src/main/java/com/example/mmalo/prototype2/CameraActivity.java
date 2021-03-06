package com.example.mmalo.prototype2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;

import android.os.Build;
import android.os.Bundle;

import android.util.Size;
import android.util.SparseIntArray;
import android.view.OrientationEventListener;
import android.view.Surface;

import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mmalo.prototype2.Controllers.CameraPreview;
import java.io.ByteArrayOutputStream;

import java.sql.Timestamp;

import java.util.Date;

import static com.example.mmalo.prototype2.Controllers.CameraController.getCameraInstance;


/**
 * Created by mmalo on 27/10/2016.
 *
 * Camera functionality adapted from tutorial:
 * https://developer.android.com/guide/topics/media/camera.html
 *
 */
public class CameraActivity extends Activity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private boolean cancelFlag;
    /**
     * The Bitmap.
     */
    Bitmap bitmap;
    /**
     * The The time.
     */
    Timestamp theTime;
    /**
     * The Data to pass.
     */
    byte[] dataToPass;
    /**
     * The Rotation mat.
     */
    Matrix rotationMat;
    /**
     * The Api level.
     */
    int apiLevel;
    /**
     * The Preview.
     */
    FrameLayout preview;
    /**
     * The Im text view.
     */
    TextureView imTextView;
    /**
     * The M orientation listener.
     */
    OrientationEventListener mOrientationListener;
    /**
     * The Capture button.
     */
    ImageButton captureButton;
    /**
     * The Turn.
     */
    ImageView turn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiLevel = Build.VERSION.SDK_INT;
        cancelFlag = false;

        setContentView(R.layout.camera_preview);
        lowerAPI();

        rotationMat = new Matrix();
        rotationMat.postRotate(90);


        turn = (ImageView) findViewById(R.id.turnImage);
        mOrientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int orientation) {

                if (!cancelFlag) {
                    if (orientation < 200 || orientation > 320) {
                        turn.setVisibility(View.VISIBLE);
                        turn.bringToFront();
                        captureButton.setEnabled(false);

                    } else {
                        turn.setVisibility(View.INVISIBLE);
                        captureButton.setEnabled(true);

                    }
                }
            }
        };

        if (mOrientationListener.canDetectOrientation()) {
            mOrientationListener.enable();
        } else {
            mOrientationListener.disable();
        }





    }


    /**
     * Lower api.
     */
    public void lowerAPI() {
        // Create an instance of Camera
        captureButton = (ImageButton) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           takePicClick(v);
                                       }
                                   }
        );

        mCamera = getCameraInstance();

        if (mCamera == null) {
            releaseCamera();
            Toast t = Toast.makeText(this, "UNABLE to get camera instance", Toast.LENGTH_LONG);
            t.show();
            recreate();
        } else {
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            preview = (FrameLayout) findViewById(R.id.camera_preview);
            mCamera.setDisplayOrientation(90);
            preview.addView(mPreview);
        }
    }

    /**
     * Take pic click.
     *
     * @param v the v
     */
    public void takePicClick(View v) {
        mCamera.takePicture(null, null, mPicture);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            afterTaken(data);
        }
    };


    /**
     * After taken.
     *
     * @param photoData the photo data
     */
    public void afterTaken(byte[] photoData) {
        try {
            bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotationMat, true);

            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            theTime = ts;

            if (preview != null) {
                preview.removeView(mPreview);
            } else {
                imTextView = (TextureView) findViewById(R.id.imageTextView);
                imTextView.setVisibility(View.INVISIBLE);
                //imTextView.clear
            }

            ImageView iv = (ImageView) findViewById(R.id.frame_taken);
            iv.setImageBitmap(bitmap);
            iv.setVisibility(View.VISIBLE);

            cancelFlag = !cancelFlag;


            RelativeLayout seven = (RelativeLayout) findViewById(R.id.RelLay1);
            LinearLayout options = (LinearLayout) seven.findViewById(R.id.LinLay2);
            options.setVisibility(View.VISIBLE);
            LinearLayout toHide = (LinearLayout) seven.findViewById(R.id.LinLay1);
            toHide.setVisibility(View.INVISIBLE);


            dataToPass = photoData;
        }catch(Exception e){
            e.printStackTrace();
            Toast t = Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG);
            t.show();
            recreate();
        }
    }

    /**
     * Continue form.
     *
     * @param v the v
     */
    public void continueForm(View v) {
        //Pass data to next activity then release camera then load activity
        try {
        PTakenActivity.photoData = dataToPass;

        PTakenActivity.timetaken = theTime;
        PTakenActivity.filename = theTime.toString();

        releaseCamera();
        bitmap.recycle();
        dataToPass = null;
        System.gc();

        Intent i = new Intent(this, PTakenActivity.class);
        this.startActivity(i);

        }catch(Exception e){
            e.printStackTrace();
            Toast t = Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG);
            t.show();
            recreate();
        }
    }


    @Override
    public void onBackPressed() {

        releaseCamera();
        super.onBackPressed();
    }

    /**
     * Skip pic take.
     *
     * @param v the v
     */
    public void skipPicTake(View v) {
        releaseCamera();
        Intent i = new Intent(this, PTakenActivity.class);
        this.startActivity(i);

    }

    /**
     * Cancel pic.
     *
     * @param v the v
     */
    public void cancelPic(View v) {

        if (!cancelFlag) {
            //In preview Mode, cancel releases camera and returns to main options
            releaseCamera();
            Intent i = new Intent(this, OptionsActivity.class);
            this.startActivity(i);
        } else {
            //Pic Taken, cancel resets camera to take another photo
            cancelFlag = !cancelFlag;

            //this could be more efficient
            releaseCamera();
            recreate();
        }
    }


    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }


}
