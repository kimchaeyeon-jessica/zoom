package com.zoomw.zoom.features.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

public class CameraManager {
    private static CameraManager cameraManager; //디자인 패턴-싱글톤 (single ton)패턴 :전체 중에서 반드시 하나만 유지할 수 있도록 하는 것
    private static int maxCamera;
    private static int currentCamera = 0;

    private CameraManager() {
    }

    public static CameraManager getCameraManager() {
        if (cameraManager == null) { //없었을 땐 null이었을 것이고
            cameraManager = new CameraManager(); //그 다음엔 카메라 매니저를 이용할 것
        }
        maxCamera = Camera.getNumberOfCameras();  //내 핸드폰에 카메라가 몇개 있는지 반환
        return cameraManager; //리턴해줌
    }

    public boolean checkCameraUsable(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    } //카메라가 들어있으면 true 안들어 있으면 false. 카메라가 있는지 검사하기 위한 코드


    //카메라 요청하는 코드
    public Camera getCamera() {
        Camera camera = null;

        //try 는 이 안에서 소스코드 실행하다가 될지 안될지 모르지만 일단 시도해보겠다
        //catch는 try에서 실패하면 받아서 처리
        try {
            camera = Camera.open(); //카메라를 안드로이드한테 요청-카메라 쓸 수 있게 허락 해줘 안돼? 안되면 catch로 떨어짐
            Camera.Parameters cameraParameters = camera.getParameters(); //설정들 -현재 내가 불러온 카메라 속성들이 전부 들어있음
            if (cameraParameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE); //사진 찍을 때 알아서 포커스 맞춰줌-이게 없으면 처음 켰을 때 카메라 화면만 그대로 떠있음 계속
                camera.setParameters(cameraParameters);
            }//내가 카메라를 제어할 때 포커스 조절을 할 수 있나? 확인하기 위해 가져온 코드
        } catch (Exception ex) {
            Log.e("CameraManager", ex.toString());
            System.exit(1); //종료 시키기
        }
        return camera;
    }

    public Camera getNextCamera() {
        Camera camera = null;
        try {
            //TODO: 카메라 선택하는 로직
            camera = Camera.open(currentCamera);
            Camera.Parameters cameraParameters = camera.getParameters();
            if (cameraParameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                camera.setParameters(cameraParameters);
            }
        } catch (Exception ex) {
            Log.e("CameraManager", ex.toString());
            System.exit(1);
        }
        return camera;
    }
}