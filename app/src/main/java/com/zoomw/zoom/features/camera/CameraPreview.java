package com.zoomw.zoom.features.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceHolder holder;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;

        this.holder = getHolder();
        this.holder.addCallback(this);
        this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override //함수 추가
   public void surfaceCreated(SurfaceHolder holder) {
        try{
            this.camera.setPreviewDisplay(holder);
            this.camera.startPreview();
        } catch(IOException e){
            Log.d("CameraPreview", "미리보기 생성 실패: "+e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        if (this.holder.getSurface() == null){
            return;
        }
        try{
            this.camera.stopPreview();
        }catch (Exception e){

        }

        try{
            this.camera.setPreviewDisplay(this.holder);
            this.camera.startPreview();
        } catch (Exception e){
            Log.d("CameraPreview", "미리보기 생성 실패: "+e.getMessage());
        }
    }
}
