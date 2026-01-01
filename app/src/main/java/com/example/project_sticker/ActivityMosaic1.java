package com.example.project_sticker;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ActivityMosaic1 extends AppCompatActivity {

    ImageView imageView;
    String imgName = "osz3.png";    // 이미지 이름
    Bitmap originalBitmap;         // Original bitmap to apply mosaic effect

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mosaic_layout1);
        imageView = findViewById(R.id.imageView);
        try {
            String imgpath = getCacheDir() + "/" + imgName;   // 내부 저장소에 저장되어 있는 이미지 경로
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            originalBitmap = bm;   // Store the original bitmap
            imageView.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
            Toast.makeText(getApplicationContext(), "파일 로드 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
        }

        Button b = (Button) findViewById(R.id.backButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMosaic1.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void bt1(View view) {    // 이미지 선택 누르면 실행됨 이미지 고를 갤러리 오픈
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            ContentResolver resolver = getContentResolver();
            try {
                InputStream instream = resolver.openInputStream(fileUri);
                Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                instream.close();   // 스트림 닫아주기
                if (requestCode == 101) {
                    originalBitmap = imgBitmap; // Store the original bitmap
                    imageView.setImageBitmap(imgBitmap);    // 선택한 이미지 이미지뷰에 셋
                    saveBitmapToJpeg(imgBitmap);    // 내부 저장소에 저장
                }
                Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        File tempFile = new File(getCacheDir(), imgName);    // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
            //Toast.makeText(getApplicationContext(), "파일 저장 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void btSave(View view) {
        Intent intent22 = new Intent(ActivityMosaic1.this, ActivityMosaic2.class);

        startActivity(intent22);
    }

    public Bitmap applyMosaicEffect(Bitmap src, int level) {
        if (level == 0) {
            return src; // Return the original bitmap if level is 0
        }

        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        for (int y = 0; y < src.getHeight(); y += level) {
            for (int x = 0; x < src.getWidth(); x += level) {
                int pixel = src.getPixel(x, y);
                for (int dy = 0; dy < level && y + dy < src.getHeight(); dy++) {
                    for (int dx = 0; dx < level && x + dx < src.getWidth(); dx++) {
                        result.setPixel(x + dx, y + dy, pixel);
                    }
                }
            }
        }

        return result;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked and apply the mosaic effect accordingly
        if (checked) {
            int mosaicLevel = 0; // Default mosaic level
            if (view.getId() == R.id.alphaHigh) {
                mosaicLevel = 50;
            }else if (view.getId() == R.id.alphaDeep) {
                mosaicLevel = 30;
            } else if (view.getId() == R.id.alphaShallow) {
                mosaicLevel = 10;
            } else if (view.getId() == R.id.alphaBasic) {
                mosaicLevel = 0;
            }

            Bitmap mosaicBitmap = applyMosaicEffect(originalBitmap, mosaicLevel);
            imageView.setImageBitmap(mosaicBitmap);
            saveBitmapToJpeg(mosaicBitmap);    // Save the new mosaic image

        }
    }
}
