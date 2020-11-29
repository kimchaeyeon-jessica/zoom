package com.zoomw.zoom.features.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera; //카메라 (화면 관리 위해)
    private SurfaceHolder holder; //화면에 출력하고 있는 것과 연결시켜줌

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;

        this.holder = getHolder();
        this.holder.addCallback(this);
        this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override //함수 추가
    //화면이 만들어졌을 때
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            this.camera.setPreviewDisplay(holder); //미리보기를 쟤한테 관리하게 할거야
            this.camera.setDisplayOrientation((getDegree()));
            this.camera.startPreview(); //미리보기를 실행시켜줘 -미리보기를 만들어주는 부분
        } catch (IOException e) {
            Log.d("CameraPreview", "미리보기 생성 실패: " + e.getMessage());
        }
    }

    @Override //현재 화면에 출력하고 있는게 있어 없어?
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (this.holder.getSurface() == null) {
            return;
        }
        try {
            this.camera.stopPreview(); //화면을 없앴다가
        } catch (Exception e) {

        }

        try {
            this.camera.setPreviewDisplay(this.holder);
            this.camera.setDisplayOrientation(getDegree()); //보여줄 각도를 정함
            this.camera.startPreview(); //다시 새로 그려줌
        } catch (Exception e) {
            Log.d("CameraPreview", "미리보기 생성 실패: " + e.getMessage());
        }
    }

    //화면을 움직이면서 방향이 바뀌면 카메라 각도 변환주는 함수
    private int getDegree() {
        Activity currentActivity = (Activity) (this.getContext()); //화면 관리하는 애 데려옴 (화면 어떻게 돼있냐고 물어봄!)
        int rotation = currentActivity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_90: //90도로 꺾여있을 때
                return 0;
            case Surface.ROTATION_180: //180도로 꺾여있을 때
                return 270;
            case Surface.ROTATION_270: //270도로 꺾여있을 때
                return 180;
            default:
                return 90;
        }
    }

    @Override //화면 없어졌을 때 처리
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //기존에 있던 미리보기를 새로운 미리 보기로 바꾸는 코드
    public void changeCamera(Camera newCamera) {
        try {
            this.camera.stopPreview();
            this.camera.release();
            this.camera = null;
        } catch (Exception e) {

        }
        try {
            newCamera.setPreviewDisplay(this.holder);
            newCamera.setDisplayOrientation(getDegree());
            newCamera.startPreview();
            this.camera = newCamera;
        } catch (Exception e) {
            Log.d("CameraPreview", "미리보기 변경 실패: " + e.getMessage());
        }
    }
}
