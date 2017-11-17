package com.aim.surfaceview_demo;

import android.hardware.Camera;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {


    @SuppressWarnings("deprication")
    Camera camera;
    SurfaceView camView;
    SurfaceHolder surfaceHolder;
    boolean camCondition = false;
    Button cap;


    @SuppressWarnings("deprication")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setFormat(PixelFormat.UNKNOWN);
        camView = (SurfaceView)findViewById(R.id.camerapreview);
        surfaceHolder = camView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

        cap = (Button)findViewById(R.id.takepicture);
        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null,null,null,mPictureCallback);
            }
        });

    }

    @SuppressWarnings("deprecation")
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback(){


        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {

            FileOutputStream outputStream = null;
            try{
                outputStream = new FileOutputStream("/sdcard/AndroidCodec_"+ System.currentTimeMillis()+ ".jpg");
                outputStream.write(bytes);
                outputStream.close();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } finally {

            }
        }
    };








    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        camera.setDisplayOrientation(90);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        if (camCondition){
            camera.stopPreview();
            camCondition = false;}
        if (camera != null){
            try{
                Camera.Parameters parameters = camera.getParameters();
                parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
                camera.setParameters(parameters);
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();

                camCondition = true;
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        camera.stopPreview();
        camera.release();
        camera = null;
        camCondition = false;

    }
}
