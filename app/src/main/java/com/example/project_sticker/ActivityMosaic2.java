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

public class ActivityMosaic2 extends AppCompatActivity {
    ImageView imageView2;
    String imgName22 = "osz3.png";    // 이미지 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mosaic_layout2);

        imageView2 = findViewById(R.id.imageView2);
        // 메인 이미지 로드
        try {
            String imgpath22 = getCacheDir() + "/" + imgName22;   // 내부 저장소에 저장되어 있는 이미지 경로
            Bitmap bm22 = BitmapFactory.decodeFile(imgpath22);
            imageView2.setImageBitmap(bm22);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
        }

    }

    public void mOnCaptureClick(View v){
        View rootView = getWindow().getDecorView().getRootView();

        File screenShot = ScreenShot(rootView);
        if(screenShot!=null){
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));

            Intent intentRe3 = new Intent(ActivityMosaic2.this,
                    MainActivity.class);
            startActivity(intentRe3);
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
