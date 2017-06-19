package com.example.noorulain.second;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.SurfaceView;

import com.qualcomm.snapdragon.sdk.face.FaceData;

/**
 * Created by Noorulain on 09-01-2016.
 */
public class DrawView extends SurfaceView {



    public FaceData[]mfacearray;
    boolean inframe;

    //canvas

    private Paint leftEyeBrush =new Paint();
    private Paint rightEyeBrush =new Paint();
    private Paint mouthBrush =new Paint();
    private Paint rectBrush =new Paint();
    public Point leftEye,rightEye,mouth;
    Rect mfacerect;


    public DrawView(Context context, FaceData[]facearray, boolean inFrame)
    {
        super(context);
        setWillNotDraw(false);

        mfacearray=facearray;
        inframe=inFrame;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (inframe)
        {
            for(int i=0;i<mfacearray.length;i++)
            {
                leftEyeBrush.setColor(Color.RED);
                canvas.drawCircle(mfacearray[i].leftEye.x, mfacearray[i].leftEye.y,
                        5f, rightEyeBrush);

                rightEyeBrush.setColor(Color.GREEN);
                canvas.drawCircle(mfacearray[i].rightEye.x, mfacearray[i].rightEye.y,
                        5f, rightEyeBrush);

                mouthBrush.setColor(Color.WHITE);
                canvas.drawCircle(mfacearray[i].mouth.x, mfacearray[i].mouth.y,
                        5f, mouthBrush);

                setRectColor(mfacearray[i], rectBrush);

                rectBrush.setStrokeWidth(2);
                rectBrush.setStyle(Paint.Style.STROKE);
                canvas.drawRect(mfacearray[i].rect.left,mfacearray[i].rect.top,
                        mfacearray[i].rect.right,mfacearray[i].rect.bottom,rectBrush);


            }
        }
        else
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);

    }


    private void setRectColor(FaceData facedata,Paint rectBrush)
    {
        if(facedata.getSmileValue()<40)
        {
            rectBrush.setColor(Color.RED);
        }
        else if(facedata.getSmileValue()<55)
        {
            rectBrush.setColor(Color.parseColor("#FE642E"));
        }
        else if(facedata.getSmileValue()<70)
        {
            rectBrush.setColor(Color.parseColor("#D7DF01"));
        }
        else if(facedata.getSmileValue()<85)
        {
            rectBrush.setColor(Color.parseColor("#86B404"));





        }
        else
        {
            rectBrush.setColor(Color.parseColor("#5FB404"));
        }
    }




}
