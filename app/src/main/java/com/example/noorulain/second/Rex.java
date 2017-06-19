package com.example.noorulain.second;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

/**
 * Created by Noorulain on 09-01-2016.
 */
public class Rex extends Activity implements Camera.PreviewCallback {


    Camera c;
    FrameLayout preview;

    private CameraSurfacePreview mpreview;
    private int FRONT_CAMERA_INDEX = 1;
    private int BACK_CAMERA_INDEX = 0;

    public static boolean switchCamera = false;

    //faceproc

    private boolean _qcSDKEnabled = true;
    FacialProcessing faceproc;

    private int numfaces;
    FaceData[] facearray = null;

    //drawview
    DrawView drawview;

    //rotation of preview
    Display display;
    private int dAngle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);


        //rotation android:layout_alignTop="@+id/switchCamera"


        preview = (FrameLayout) findViewById(R.id.camera_preview);
        try {
            startCamera();

        } catch (Exception e) {
            Log.d("idhar DEKHO :D", "Error : " + e.getMessage());
        }

        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();


        final Button switchCam = (Button) findViewById(R.id.switchCamera);
        switchCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!switchCamera) {
                    stopCamera();
                    switchCamera = true;
                    startCamera();
                } else {
                    stopCamera();
                    switchCamera = false;
                    startCamera();
                }


            }
        });

        final Button play = (Button) findViewById(R.id.PlayNow);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // sendMessage();
               // finish();
                Intent intent = new Intent();
                intent.putExtra("SmileDetected", true);
                intent.putExtra("Playlist","HAPPY");
                setResult(RESULT_OK,intent);
                finish();

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_fat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startCamera() {


        _qcSDKEnabled = FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING);

        boolean w = FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_RECOGNITION);
        Log.d("idhar DEKHO..:D", "QCKENABLED:  " + faceproc + "  qck  " + _qcSDKEnabled + "  " + w);

        if (_qcSDKEnabled && faceproc == null) {


            Log.e("IDHAR DEKHO :D", "FEATURE IS SUPPORTED");
            faceproc = FacialProcessing.getInstance();

        } else if (!_qcSDKEnabled) {
            Toast toast = Toast.makeText(getApplicationContext(), "Facial Processing not supported",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 120);
            toast.show();

        }


        // c=Camera.open();

        if (!switchCamera) {
            c = Camera.open();
        } else {
            c = Camera.open(FRONT_CAMERA_INDEX);
        }

        mpreview = new CameraSurfacePreview(Rex.this, c);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mpreview);
        c.setPreviewCallback(this);


    }


    public void onPreviewFrame(byte[] data, Camera camera) {

        int drotation = display.getRotation();
        FacialProcessing.PREVIEW_ROTATION_ANGLE anglleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_0;

        Log.e("idhar dekho :D", "ROTATION :" + drotation);

        switch (drotation) {

            case 0:
                dAngle = 90;
                anglleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_90;
                break;

            case 1:
                dAngle = 0;
                anglleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_0;
                break;

            case 2:
                dAngle = 270;
                anglleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_270;
                break;

            case 3:
                dAngle = 180;
                anglleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_180;
                break;
        }

        c.setDisplayOrientation(dAngle);


        if (_qcSDKEnabled) {
            if (faceproc == null) {
                faceproc = FacialProcessing.getInstance();
            }
            Camera.Parameters params = c.getParameters();
            Camera.Size previewsize = params.getPreviewSize();

            if (this.getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE && !switchCamera) {
                faceproc.setFrame(data, previewsize.width, previewsize.height, true, anglleEnum);
            } else if (this.getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE && switchCamera) {
                faceproc.setFrame(data, previewsize.width, previewsize.height, false, anglleEnum);
            } else if (this.getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT && !switchCamera) {
                faceproc.setFrame(data, previewsize.width, previewsize.height, true, anglleEnum);
            } else {
                faceproc.setFrame(data, previewsize.width, previewsize.height, false, anglleEnum);
            }
        }


        try {
            numfaces = faceproc.getNumFaces();

            int surfacewidth = mpreview.getWidth();
            int surfaceheight = mpreview.getHeight();
            faceproc.normalizeCoordinates(surfacewidth, surfaceheight);
        } catch (Exception e) {
            Log.d("idhar DEKHO :D", "Error now: " + e.getMessage() + " faces  " + numfaces);
        }

        if (numfaces > 0) {
            Log.d("idhar dekho :D", "FACE DETECTED");
            facearray = faceproc.getFaceData();
            preview.removeView(drawview);
            drawview = new DrawView(this, facearray, true);
            preview.addView(drawview);

            for(int i =0;i<facearray.length;i++)
            {
                if(facearray[i].getSmileValue()==85)
                {
                    Intent intent = new Intent();
                    intent.putExtra("SmileDetected", true);
                    intent.putExtra("Playlist","HAPPY");
                    setResult(RESULT_OK, intent);
                    finish();

                }

            }



        } else {
            preview.removeView(drawview);
            drawview = new DrawView(this, null, false);
            preview.addView(drawview);

        }

    }

    private void stopCamera() {

        if (c != null) {
            c.stopPreview();
            c.setPreviewCallback(null);
            preview.removeView(mpreview);
            c.release();

            if (_qcSDKEnabled) {

                faceproc.release();
                faceproc = null;
            }
        }

        c = null;


    }


    protected void onPause() {

        super.onPause();
        stopCamera();
    }

    protected void onDestroy() {

        super.onDestroy();
    }

    protected void onResume() {

        super.onResume();
        if (c != null) {

            stopCamera();
        }

        startCamera();

    }





}
