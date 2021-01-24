package com.zoomw.zoom.features.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

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

    //카메라 쓸 수 있나?? 카메라 연결 되어 있나??
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
                cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE); //사진 찍을 때 자동으로 포커스 맞춰줌-이게 없으면 처음 켰을 때 카메라 화면만 그대로 떠있음 계속
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
            //카메라 선택하는 로직
            currentCamera = (currentCamera + 1) % maxCamera; //프로그램 종료되지 않고 계속 카메라 바꾸며 진행됨
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

    private Camera.PictureCallback getTakePictureCallback() {
        return new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                camera.startPreview();
                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null) {
                    Log.e("CameraManager", "파일 생성 실패");
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.e("CameraManager", "파일 찾기 실패:" + e.getMessage());
                } catch (IOException e) {
                    Log.e("CameraManager", "파일 접근 실패:" + e.getMessage());
                } catch (Exception e) {
                    Log.e("CameraManager", "파일 저장 실패:" + e.getMessage());
                }
            }
        };
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), //사진을 저장할 수 있는 폴더
                "ZoomPictures" //하위폴더
        ); //사진을 저장할 곳

        if (!mediaStorageDir.exists()) { //존재하지 않으면
            if (!mediaStorageDir.mkdirs()) { // mkdirs: 경로에 있는 모든 폴더 생성
                Log.e("CameraManager", "파일 디렉토리 생성 실패"); //생성이 안되면 false
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("YYYYMMDDHHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg"); //
        } else {
            return null;
        }

        return mediaFile;
    }

    public void takeAndSaveImage(Camera camera) {
        camera.takePicture(null, null, getTakePictureCallback());
    }

    public boolean isFrontCamera(){
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(currentCamera, cameraInfo);
        return cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT; //카메라가 정면카메라인지 확인
    } //카메라 정보를 가져오는 것
}