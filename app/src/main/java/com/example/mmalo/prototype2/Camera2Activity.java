package com.example.mmalo.prototype2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mmalo.prototype2.Controllers.CameraPreview;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.mmalo.prototype2.Controllers.CameraController.getCameraInstance;

/**
 * Created by mmalo on 27/02/2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Camera2Activity extends AppCompatActivity {

    //http://coderzpassion.com/android-working-camera2-api/
    private boolean cancelFlag;
    private Size jpegSizes[];
    private Size prevSize;

    Bitmap bitmap;
    Timestamp theTime;
    String filename;
    byte[] dataToPass;
    Matrix rotationMat;
    FrameLayout preview;
    protected CameraDevice camDevice;
    TextureView imTextView;
    private CaptureRequest.Builder prevBuild;
    private CameraCaptureSession prevSesh;
    CameraManager manager;
    Button cancelButton;

    private static final SparseIntArray ORIENT = new SparseIntArray();

    static {
        ORIENT.append(Surface.ROTATION_0, 90);
        ORIENT.append(Surface.ROTATION_90, 0);
        ORIENT.append(Surface.ROTATION_180, 270);
        ORIENT.append(Surface.ROTATION_270, 180);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera2_preview);
        cancelFlag = false;
        higherAPI();
    }


    public void higherAPI() {
        Toast t = Toast.makeText(this, "HIGHER API", Toast.LENGTH_LONG);
        //t.show();
        jpegSizes = null;
        Button capture = (Button) findViewById(R.id.button_capture);
        imTextView = (TextureView) findViewById(R.id.imageTextView);
        imTextView.setSurfaceTextureListener(surfaceTextureListener);
        rotationMat = new Matrix();
        rotationMat.postRotate(90);

        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics chars = manager.getCameraCharacteristics(camDevice.getId());
            if (chars != null) {
                jpegSizes = chars.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        capture.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (camDevice == null) {
                                               Toast t = Toast.makeText(getApplicationContext(), "camDevice is NULL", Toast.LENGTH_LONG);
                                               t.show();
                                               return;
                                           } else {
                                               takePictureHigher();
                                           }
                                       }
                                   }


        );
    }

    @TargetApi(21)
    public void takePictureHigher() {
        cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setEnabled(false);
        try {
            int width = 640, height = 480;
            if (jpegSizes != null && jpegSizes.length > 0) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader imRead = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);

            List<Surface> outputs = new ArrayList<Surface>(2);
            outputs.add(imRead.getSurface());
            outputs.add(new Surface(imTextView.getSurfaceTexture()));

            final CaptureRequest.Builder captBuild = camDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

            captBuild.addTarget(imRead.getSurface());
            captBuild.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();

            captBuild.set(CaptureRequest.JPEG_ORIENTATION, ORIENT.get(rotation));

            ImageReader.OnImageAvailableListener imageAvailableListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try{
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        dataToPass = bytes;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                afterTaken(dataToPass);
                            }
                        });
                        cancelButton.setEnabled(true);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    } finally {
                        if (image != null)
                            image.close();
                    }
                }
            };


            HandlerThread handlerThread = new HandlerThread("takepicture");
            handlerThread.start();
            final Handler handler = new Handler(handlerThread.getLooper());
            imRead.setOnImageAvailableListener(imageAvailableListener, handler);

            final CameraCaptureSession.CaptureCallback previewSSession = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                }

                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    startCamera();
                }
            };
            camDevice.createCaptureSession(outputs, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captBuild.build(), previewSSession, handler);
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @TargetApi(21)
    public void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String camID = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(camID);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            prevSize = map.getOutputSizes(SurfaceTexture.class)[0];

            manager.openCamera(camID, stateCallback, null);

        } catch (Exception e) {
        }
    }


    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            camDevice = camera;
            startCamera();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        if (camDevice != null) {
            camDevice.close();
        }
    }

    void startCamera() {
        if (camDevice == null || !imTextView.isAvailable() || prevSize == null) {
            return;
        }
        SurfaceTexture texture = imTextView.getSurfaceTexture();
        if (texture == null) {
            return;
        }
        texture.setDefaultBufferSize(prevSize.getWidth(), prevSize.getHeight());
        Surface surface = new Surface(texture);
        try {
            prevBuild = camDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (Exception e) {
        }
        prevBuild.addTarget(surface);
        try {
            camDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    prevSesh = session;
                    getChangedPreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, null);
        } catch (Exception e) {
        }
    }

    void getChangedPreview() {
        if (camDevice == null) {
            return;
        }
        prevBuild.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread thread = new HandlerThread("changed Preview");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        try {
            prevSesh.setRepeatingRequest(prevBuild.build(), null, handler);
        } catch (Exception e) {
        }
    }


    public void afterTaken(byte[] photoData) {
        try {
            Bitmap bmp = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), rotationMat, true);
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            theTime = ts;

            imTextView = (TextureView) findViewById(R.id.imageTextView);
            imTextView.setVisibility(View.INVISIBLE);
            //imTextView.clear

            ImageView iv = (ImageView) findViewById(R.id.frame_taken);
            iv.setImageBitmap(bitmap);
            iv.setVisibility(View.VISIBLE);

            cancelFlag = !cancelFlag;

            Button confirm = (Button) findViewById(R.id.button_cont);
            confirm.setVisibility(View.VISIBLE);

            Button capture = (Button) findViewById(R.id.button_capture);
            capture.setVisibility(View.INVISIBLE);

            dataToPass = photoData;
        } catch (Exception e) {
            e.printStackTrace();
            Toast t = Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG);
            t.show();
            recreate();
        }
    }

    public void continueForm(View v) {
        //Pass data to next activity then release camera then load activity
        //saveImageToFile(dataToPass);
        //readImageFromFile(filename);
        try {
            PTakenActivity.photoData = dataToPass;

            PTakenActivity.timetaken = theTime;
            PTakenActivity.filename = theTime.toString();

            releaseCamera();
            System.gc();

            Intent i = new Intent(this, PTakenActivity.class);
            this.startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
            Toast t = Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG);
            t.show();
            recreate();
        }
    }


    @Override
    public void onBackPressed() {

        releaseCamera();
        super.onBackPressed();
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


            //this could be more efficient
            releaseCamera();
            recreate();
        }
    }


    private void releaseCamera() {
        if (camDevice != null) {
            camDevice.close();
            camDevice = null;
        }
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        if (dataToPass != null) {
            dataToPass = null;
        }
    }


}

