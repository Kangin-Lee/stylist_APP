package com.example.stylist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stylist.ml.Colors;
import android.graphics.Color;

import org.tensorflow.lite.DataType;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


public class ColorMatch extends AppCompatActivity {

    TextView result, resultText, resultText1,textView,warningMessage;
    TextView color1, color2,color3, color4,color5;
    ImageView imageView;
    Button picture;
    int imageSize = 224;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_recommend);
        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        Button galleryButton = findViewById(R.id.button2);
        resultText = findViewById(R.id.resultText);
        resultText1 = findViewById(R.id.resultText1);
        textView = findViewById(R.id.textView);
        warningMessage = findViewById(R.id.warningMessage);

        //  갤러리 사진 가져오기
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 갤러리에서 사진을 선택하기 위한 인텐트 생성
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2); // 2는 requestCode로 사용할 값입니다.
            }
        });


        picture.setOnClickListener(view -> {
            // Launch camera if we have permission
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            } else {
                //Request camera permission if we don't have it.
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });
    }
    public void classifyImage(Bitmap image){
        try {
            Colors color = Colors.newInstance(getApplicationContext());

            // 이미지를 원하는 크기로 조정
            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, imageSize, imageSize, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of pixels in the image
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, imageSize, 0, 0, imageSize, imageSize);

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            for (int pixelValue : intValues) {
                byteBuffer.putFloat(((pixelValue >> 16) & 0xFF) * (1.f / 255.f));
                byteBuffer.putFloat(((pixelValue >> 8) & 0xFF) * (1.f / 255.f));
                byteBuffer.putFloat((pixelValue & 0xFF) * (1.f / 255.f));
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Colors.Outputs outputs = color.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"GREEN","NAVY","RED","BURGUNDY","BEIGE","BROWN","BLACK","BLUE","SKYBLUE","YELLOW","ORANGE","KAHKI","CREAM","PURPLE","PINK","WHITE","GRAY"};
            String[] green={"green1","green2","green3","green4","green5"};
            String[] navy={"navy1","navy2","navy3","navy4","navy5"};
            String[] gray={"gray1","gray2","gray3","gray4","gray5"};
            String[] red={"red1","red2","red3","red4","red5"};
            String[] burgundy={"burgundy1","burgundy2","burgundy3","burgundy4","burgundy5"};
            String[] begie={"begie1","begie2","begie3","begie4","begie5"};
            String[] brown={"brown1","brown2","brown3","brown4","brown5"};
            String[] blue={"blue1","blue2","blue3","blue4","blue5"};
            String[] skyblue={"skyblue1","skyblue2","skyblue3","skyblue4","skyblue5"};
            String[] yellow={"yellow1","yellow2","yellow3","yellow4","yellow5"};
            String[] orange={"orange1","orange2","orange3","orange4","orange5"};
            String[] kahki={"kahki1","kahki2","kahki3","kahki4","kahki5"};
            String[] cream={"cream1","cream2","cream3","cream4","cream5"};
            String[] purple={"purple1","purple2","purple3","purple4","purple5"};
            String[] black={"black1","black2","black3","black4","black5"};
            String[] pink={"pink1","pink2","pink3","pink4","pink5"};
            String[] white={"white1","white2","white3","white4","white5"};



            String[] greenColor = {"#00FF00", "#008000", "#7FFF00", "#008080", "#00FA9A"};

            int[] imageViewIds = {R.id.color1, R.id.color2, R.id.color3, R.id.color4, R.id.color5 };

            int[] imageViewIds2 = { R.id.outfit11, R.id.outfit12,R.id.outfit13, R.id.outfit14,R.id.outfit15};


            //인식결과와 퍼센트 출력
            result.setText(classes[maxPos]+" ("+Math.round(maxConfidence*100)+"%)");
            textView.setText("");
            warningMessage.setText("※ 인식이 잘못되었을 경우, 재 인식을 해주세요.");
            resultText.setText(classes[maxPos]+"와(과) 어울리는 색상 5가지입니다.");
            resultText1.setText(classes[maxPos]+"와(과) 어울리는 색상 코디 5가지입니다.");


            //인식한 결과가 반팔일 경우
            if(classes[maxPos].equals("GREEN")){
                for (int i = 0; i < 5; i++) {
                    TextView textView1 = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shorts[i];
                    String imageName2 = green[i];
                    // 색상 코드를 정수로 변환
                    int colors = Color.parseColor(greenColor[i]);

                    // TextView의 배경색 설정
//                    textView1.setBackgroundColor(colors);
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            } else if (classes[maxPos].equals("NAVY")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = navy[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("GRAY")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = gray[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("RED")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = red[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("BURGUNDY")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = burgundy[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            } else if (classes[maxPos].equals("BEIGE")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = begie[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            } else if (classes[maxPos].equals("BROWN")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = brown[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("BLACK")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = black[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("BLUE")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = blue[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("SKYBLUE")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = skyblue[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("YELLOW")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = yellow[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("ORANGE")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = orange[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("KAHKI")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = kahki[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("CREAM")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = cream[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("PURPLE")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = purple[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("PINK")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = pink[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if (classes[maxPos].equals("WHITE")) {
                for (int i = 0; i < 5; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

//                    String imageName = shirt[i];
                    String imageName2 = white[i];
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }

            // Releases model resources if no longer used.
            color.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    private MappedByteBuffer loadModelFile(String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 사진 찍기 결과
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        } else  if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            // 갤러리에서 사진을 선택한 경우

            // 선택한 이미지의 URI를 가져옵니다.
            Uri selectedImageUri = data.getData();

            try {
                // URI에서 Bitmap 이미지를 가져옵니다.
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                int dimension = Math.min(selectedImage.getWidth(), selectedImage.getHeight());
                selectedImage = ThumbnailUtils.extractThumbnail(selectedImage, dimension, dimension);

                // ImageView에 이미지 설정
                imageView.setImageBitmap(selectedImage);

                // 이미지를 모델에 전달하여 분류를 수행하는 함수 호출
                classifyImage(selectedImage);
            } catch (IOException e) {
                // 예외 처리
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}

