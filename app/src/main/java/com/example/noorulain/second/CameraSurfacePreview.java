package com.example.noorulain.second;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Noorulain on 09-01-2016.
 */
public class CameraSurfacePreview extends SurfaceView implements SurfaceHolder.Callback {



    private SurfaceHolder mholder;
    private Camera mcamera;
    Context mcontext;
    public Activity mactivity;


    public CameraSurfacePreview(Context context,Camera camera) {
        super(context);

        mcamera=camera;
        mcontext=context;

        mholder=getHolder();
        mholder.addCallback(this);

        mholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {

            mcamera.setPreviewDisplay(holder);
            mcamera.startPreview();

        }
        catch(IOException e){


            Log.d("TAG", "Error setting camera preview: " + e.getMessage());

        }

    }




    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {



        // stopprev();

        startprev();

/*
            // set preview size and make any resize, rotate or
            // reformatting changes here
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE )
        {
            stopprev();
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                mcamera.setDisplayOrientation(90);

            } else {

                mcamera.setDisplayOrientation(180);

            }
            startprev();

        }
        else
        {
            stopprev();
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
                mcamera.setDisplayOrientation(0);

            } else {

                mcamera.setDisplayOrientation(90);

            }
            startprev();
        }

*/
        //setCameraDisplayOrientation(mactivity,0,mcamera);

        // start preview with new settings

        //startprev();


    }

    private void startprev()
    {
        try {
            mcamera.setPreviewDisplay(mholder);
            //  mcamera.setPreviewCallback(mcontext);
            mcamera.startPreview();

        } catch (Exception e){
            Log.d( "FOR YOU!!!","Error starting camera preview: " + e.getMessage());

        }

    }

    private void stopprev()
    {
        if (mholder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        try {
            mcamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


   /* public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
*/



}
