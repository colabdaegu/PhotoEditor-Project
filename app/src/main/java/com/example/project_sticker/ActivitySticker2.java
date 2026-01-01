package com.example.project_sticker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActivitySticker2 extends AppCompatActivity {

    // 코드 작성 - 김현수
    ImageView imageView2;
    ImageView imageViewSticker2;
    String imgName2 = "oszz.png";    // 이미지 이름
    String imgStickerName2 = "stt.png";    // 스티커 이미지 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sticker_layout2);

        imageView2 = findViewById(R.id.imageView2);
        imageViewSticker2 = findViewById(R.id.imageViewSticker2);

        // 메인 이미지 로드
        try {
            String imgpath2 = getCacheDir() + "/" + imgName2;   // 내부 저장소에 저장되어 있는 이미지 경로
            Bitmap bm2 = BitmapFactory.decodeFile(imgpath2);
            imageView2.setImageBitmap(bm2);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
            //Toast.makeText(getApplicationContext(), "파일 로드 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
        }

        // 스티커 이미지 로드
        try {
            String imgpathS2 = getCacheDir() + "/" + imgStickerName2;   // 내부 저장소에 저장되어 있는 이미지 경로
            Bitmap bmS = BitmapFactory.decodeFile(imgpathS2);
            imageViewSticker2.setImageBitmap(bmS);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
            //Toast.makeText(getApplicationContext(), "스티커 로드 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "스티커 로드 실패", Toast.LENGTH_SHORT).show();
        }
        int[] location2 = new int[2];
        imageViewSticker2.getLocationOnScreen(location2);



        // Intent로부터 스티커 위치와 투명도 가져오기
        Intent intent = getIntent();
        float stickerX = intent.getFloatExtra("stickerX", 0);
        float stickerY = intent.getFloatExtra("stickerY", 0);
        float stickerAlpha = intent.getFloatExtra("stickerAlpha", 1.0f);

        // imageViewSticker2에 위치와 투명도 적용
        imageViewSticker2.setX(-stickerX);
        imageViewSticker2.setY(-stickerY);
        imageViewSticker2.setAlpha(stickerAlpha);


    }


    public void mOnCaptureClick(View v){
        View rootView = getWindow().getDecorView().getRootView();

        File screenShot = ScreenShot(rootView);
        if(screenShot!=null){
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));

            Intent intentRe2 = new Intent(ActivitySticker2.this,
                    MainActivity.class);
            startActivity(intentRe2);
        }
    }

    public File ScreenShot(View view){
        view.setDrawingCacheEnabled(true);

        Bitmap screenBitmap = view.getDrawingCache();

        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", filename);
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        Toast.makeText(getApplicationContext(), "이미지 저장에 성공하였습니다.", Toast.LENGTH_SHORT).show();
        view.setDrawingCacheEnabled(false);
        return file;

    }
}

// 코드 작성 - 김현수