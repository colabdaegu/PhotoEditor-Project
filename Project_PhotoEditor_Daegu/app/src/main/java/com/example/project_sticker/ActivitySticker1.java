package com.example.project_sticker;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class ActivitySticker1 extends AppCompatActivity {

    // 코드 작성 - 김현수
    ImageView imageView;
    ImageView imageViewSticker;

    String imgName = "oszz.png";    // 이미지 이름
    String imgStickerName = "stt.png";    // 이미지 이름

    private int prevX;
    private int prevY;


    int locationSum = 0;
    float x, y;

    int bugFix = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sticker_layout1);

        Button b = (Button) findViewById(R.id.backButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySticker1.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.imageView);
        imageViewSticker = findViewById(R.id.imageViewSticker);


        imageViewSticker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 터치 이벤트의 종류를 가져옴
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 이미지뷰를 터치한 순간의 좌표를 가져옴
                        prevX = (int) event.getX();
                        prevY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 이미지뷰를 드래그하는 동안의 좌표를 가져옴
                        int newX = (int) event.getX();
                        int newY = (int) event.getY();
                        // 드래그한 거리만큼 이미지뷰를 이동시킴
                        imageViewSticker.setX(imageViewSticker.getX() + newX - prevX);
                        imageViewSticker.setY(imageViewSticker.getY() + newY - prevY);
                        // 이동한 후의 좌표를 저장하여 다음 이벤트에서 사용
                        prevX = newX;
                        prevY = newY;


                        break;
                }
                // 이벤트가 처리되었음을 반환
                return true;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            imageViewSticker.setX(210);
            imageViewSticker.setY(1495);

            if (bugFix == 1){
                // 이미지뷰의 위치를 가져와서 x, y 값 설정
                int[] location = new int[2];
                imageViewSticker.getLocationOnScreen(location);
                x = location[0];
                y = location[1];

                // 가져온 위치 값을 출력하여 확인
                Toast.makeText(getApplicationContext(), "Initial X: " + x + ", Initial Y: " + y, Toast.LENGTH_SHORT).show();
                bugFix = 0;
            }
        }
    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // 투명도 값을 상수로 설정

        // 체크된 라디오 버튼에 따라 투명도 값을 설정
        if (checked) {
            if (view.getId() == R.id.alphaDeep) {
                imageViewSticker.setAlpha(0.1f);
            } else if (view.getId() == R.id.alphaShallow) {
                imageViewSticker.setAlpha(0.6f);
            } else if (view.getId() == R.id.alphaBasic) {
                imageViewSticker.setAlpha(0.99f);
            }
        }
    }

    public void bt1(View view) {    // 이미지 선택 누르면 실행됨 이미지 고를 갤러리 오픈
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);
    }

    public void btC(View view) {    // 이미지 선택 누르면 실행됨 스티커 고를 갤러리 오픈
        Intent intentC = new Intent();
        intentC.setType("image/*");
        intentC.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentC, 102);

        imageViewSticker.setX(210);
        imageViewSticker.setY(1495);
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
                    imageView.setImageBitmap(imgBitmap);    // 선택한 이미지 이미지뷰에 셋
                } else if (requestCode == 102) {
                    imageViewSticker.setImageBitmap(imgBitmap);    // 선택한 스티커 이미지 이미지뷰에 셋
                }
                //Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveBitmapToPng(Bitmap bitmap, String filename) {   // 선택한 이미지 내부 저장소에 저장
        File tempFile = new File(getCacheDir(), filename);    // 파일 경로와 이름 넣기
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

    public void btSave(View view) {    // 이미지 저장
        if (imageView.getDrawable() != null) {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            saveBitmapToPng(bitmap, imgName);
            //saveBitmapToPng(bitmap);
        }
        if (imageViewSticker.getDrawable() != null) {
            BitmapDrawable drawable = (BitmapDrawable) imageViewSticker.getDrawable();
            Bitmap bitmapS = drawable.getBitmap();
            saveBitmapToPng(bitmapS, imgStickerName);
            //saveBitmapToPng(bitmapS);
        }

        // imageViewSticker의 최종 위치와 투명도 가져오기
        int[] locationAfter = new int[2];
        imageViewSticker.getLocationOnScreen(locationAfter);
        float stickerAfterX = locationAfter[0];
        float stickerAfterY = locationAfter[1];
        float stickerAlpha = imageViewSticker.getAlpha();

        // ActivitySticker2로 전달
        Intent intent2 = new Intent(ActivitySticker1.this, ActivitySticker2.class);
        intent2.putExtra("stickerX", x-stickerAfterX);
        intent2.putExtra("stickerY", y-stickerAfterY);
        intent2.putExtra("stickerAlpha", stickerAlpha);

        bugFix = 1;

        startActivity(intent2);
    }
}

// 코드 작성 - 김현수