package com.zoomw.zoom.features.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

public class CameraManager {
    private static CameraManager cameraManager; //디자인 패턴-싱글톤 (single ton)패턴 :전체 중에서 반드시 하나만 유지할 수 있도록 하는 것

    private CameraManager() {
    }

    public static CameraManager getCameraManager() {
        if (cameraManager == null) { //없었을 땐 null이었을 것이고
            cameraManager = new CameraManager(); //그다음엔 카메라 매니저를 이용할 것
        }
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

        try {
            camera = Camera.open(); //이게 메인! 카메라 오픈하는 부분
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