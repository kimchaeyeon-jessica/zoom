package com.zoomw.zoom.features.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class CameraStreamView extends TextureView implements CameraStreamCallback {
    private BitmapFactory.Options bitmapOption;

    public CameraStreamView(Context context) {
        super(context);
        this.bitmapOption = new BitmapFactory.Options(); //이미지 처리할 때 부가적인 옵션줌
    }

    @Override
    public void drawStream(byte[] buffer, Camera.Size size, boolean isFront) { //byte[]에 들어온 내용을 화면에 뿌릴 때 사용할 수 있는 형태로 바꾸어 주어야함
        Bitmap image = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, this.bitmapOption); //decodeByteArray로 byte[]에 있는 것을 이용해서 비트맵을 만들어줌
        if (image == null) {
            return;
        }
        Bitmap drawableImage = image.copy(Bitmap.Config.ARGB_8888, true); //이미지를 이러한 방식으로 카피해달란 이야기 //변환 가능한 형태로 바꿔달란 이야기

        int width = (int)(size.width * 1.5);
        int height = (int)(size.height * 1.5);

        Matrix matrix = new Matrix(); //모양 변형
        if (isFront){
            matrix.setScale(-1,1);
            matrix.postRotate(-270);
        }else{
            matrix.postRotate(90); //이미지 90도로 틀기
        }

        Bitmap rotatedImage = Bitmap.createBitmap(drawableImage, 0, 0, drawableImage.getWidth(), drawableImage.getHeight(), matrix, false);
        Bitmap scaledImage = Bitmap.createScaledBitmap(rotatedImage, width, height, false);
        this.setLayoutParams(new LinearLayout.LayoutParams(width, height, Gravity.LEFT));


        Canvas canvas = this.lockCanvas();
        if (canvas != null) {
            canvas.drawBitmap(scaledImage, 0, 0, null); //화면에 그려 달라는 뜻 //뽀샤시해지는 기능 넣고싶으면 여기에 넣어야함
            this.unlockCanvasAndPost(canvas);
        }
    }
}
