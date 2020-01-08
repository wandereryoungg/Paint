package com.young.paint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private Paint paint = new Paint();
    private Path path = new Path();
    private SurfaceHolder holder;
    private Bitmap bitmap = Bitmap.createBitmap(400,800, Bitmap.Config.ARGB_8888);
    private Canvas canvas;
    private Canvas canvasTemp;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.DKGRAY);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(13);
        setOnTouchListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmap);
        draw();

    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            if(holder!=null){
                canvasTemp.drawColor(Color.WHITE);
                canvasTemp.drawPath(path,paint);
                canvas.drawBitmap(bitmap,0,0,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(holder !=null){
                holder.unlockCanvasAndPost(canvas);
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(),event.getY());
                draw();
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                path.lineTo(event.getX(),event.getY());
                draw();
                break;

        }
        return true;
    }

    private String getTime(){
        return new SimpleDateFormat("HHmmssSSS").format(new Date(System.currentTimeMillis()));
    }

    public void clearCanvas(){
        path.reset();
        draw();
    }

    public void saveCanvas(View view){
        final FileOutputStream fos;
        String fileName = getTime();
        String filePath = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+fileName+".JPEG";
        try {
            fos = new FileOutputStream(new File(filePath));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                }
            }).start();

            MediaScannerConnection.scanFile(getContext(),new String[]{filePath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("young","图片保存成功");
                }
            });
            YoungToast.showTextToast(getContext(),"图片保存成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
