package com.example.stylist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stylist.ml.Model;

import org.tensorflow.lite.DataType;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;



public class OutfitRecommend extends AppCompatActivity {

    TextView result, resultText, resultText1;
    ImageView imageView, outfit1;
    Button picture;
    int imageSize = 224;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_recommend);
        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
//        outfit1 = findViewById(R.id.outfit1);
        Button galleryButton = findViewById(R.id.button2);
        resultText = findViewById(R.id.resultText);
        resultText1 = findViewById(R.id.resultText1);






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
            Model model = Model.newInstance(getApplicationContext());

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
            Model.Outputs outputs = model.process(inputFeature0);
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
            String[] classes = {"반팔","셔츠","맨투맨 or 롱슬리브","니트","후드티","카디건","코트","패딩"};
            String[] shorts={"short1","short2","short3","short4","short5","short6","short7","short8","short9","short10"};
            String[] shorts2={"short11","short12","short13","short14","short15","short16","short17","short18","short19","short20"};
            String[] shirt={"shirt1","shirt2","shirt3","shirt4","shirt5","shirt6","shirt7","shirt8","shirt9","shirt10"};
            String[] shirt2={"shirt11","shirt12","shirt13","shirt14","shirt15","shirt16","shirt17","shirt18","shirt19","shirt20"};
            String[] man={"man1","man2","man3","man4","man5","man6","man7","man8","man9","man10"};
            String[] man2={"man11","man12","man13","man14","man15","man16","man17","man18","man19","man20"};
            String[] knit={"knit1","knit2","knit3","knit4","knit5","knit6","knit7","knit8","knit9","knit10"};
            String[] knit2={"knit11","knit12","knit13","knit14","knit15","knit16","knit17","knit18","knit19","knit20"};
            String[] hoodie={"hoodie1","hoodie2","hoodie3","hoodie4","hoodie5","hoodie6","hoodie7","hoodie8","hoodie9","hoodie10"};
            String[] hoodie2={"hoodie11","hoodie12","hoodie13","hoodie14","hoodie15","hoodie16","hoodie17","hoodie18","hoodie19","hoodie20"};
            String[] cardigan={"cardigan1","cardigan2","cardigan3","cardigan4","cardigan5","cardigan6","cardigan7","cardigan8","cardigan9","cardigan10"};
            String[] cardigan2={"cardigan11","cardigan12","cardigan13","cardigan14","cardigan15","cardigan16","cardigan17","cardigan18","cardigan19","cardigan20"};
            String[] coat={"coat1","coat2","coat3","coat4","coat5","coat6","coat7","coat8","coat9","coat10"};
            String[] coat2={"coat11","coat12","coat13","coat14","coat15","coat16","coat17","coat18","coat19","coat20"};
            String[] padding={"padding1","padding2","padding3","padding4","padding5","padding6","padding7","padding8","padding9","padding10"};
            String[] padding2={"padding1","padding12","padding13","padding14","padding15","padding16","padding17","padding18","padding19","padding20"};

            int[] imageViewIds = {R.id.outfit1, R.id.outfit2, R.id.outfit3, R.id.outfit4, R.id.outfit5,
                    R.id.outfit6, R.id.outfit7, R.id.outfit8, R.id.outfit9, R.id.outfit10 };

            int[] imageViewIds2 = { R.id.outfit11, R.id.outfit12,R.id.outfit13, R.id.outfit14,R.id.outfit15,
                    R.id.outfit16,R.id.outfit17,R.id.outfit18, R.id.outfit19, R.id.outfit20
            };


            //인식결과와 퍼센트 출력
            result.setText(classes[maxPos]+" ("+Math.round(maxConfidence*100)+"%)");
            resultText.setText(classes[maxPos]+"와(과) 어울리는 아이템 10가지입니다.");
            resultText1.setText(classes[maxPos]+"와(과) 어울리는 코디 10가지입니다.");

            //인식한 결과가 반팔일 경우
            if(classes[maxPos].equals("반팔")){
                for (int i = 0; i < 10; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

                    String imageName = shorts[i];
                    String imageName2 = shorts2[i];
                    int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    imageView.setImageResource(resID);
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            } else if (classes[maxPos].equals("셔츠")) {
                for (int i = 0; i < 10; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

                    String imageName = shirt[i];
                    String imageName2 = shirt2[i];
                    int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    imageView.setImageResource(resID);
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if(classes[maxPos].equals("맨투맨 or 롱슬리브")){
                for (int i = 0; i < 10; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

                    String imageName = man[i];
                    String imageName2 = man2[i];
                    int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    imageView.setImageResource(resID);
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if(classes[maxPos].equals("니트")){
                for (int i = 0; i < 10; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

                    String imageName = knit[i];
                    String imageName2 = knit2[i];
                    int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    imageView.setImageResource(resID);
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if(classes[maxPos].equals("후드티")){
                for (int i = 0; i < 10; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

                    String imageName = hoodie[i];
                    String imageName2 = hoodie2[i];
                    int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    imageView.setImageResource(resID);
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if(classes[maxPos].equals("카디건")){
                for (int i = 0; i < 10; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

                    String imageName = cardigan[i];
                    String imageName2 = cardigan2[i];
                    int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    imageView.setImageResource(resID);
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if(classes[maxPos].equals("코트")){
                for (int i = 0; i < 10; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

                    String imageName = coat[i];
                    String imageName2 = coat2[i];
                    int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    imageView.setImageResource(resID);
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }else if(classes[maxPos].equals("패딩")){
                for (int i = 0; i < 10; i++) {
                    ImageView imageView = findViewById(imageViewIds[i]);
                    ImageView imageView1 = findViewById(imageViewIds2[i]);

                    String imageName = padding[i];
                    String imageName2 = padding2[i];
                    int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    imageView.setImageResource(resID);
                    int resID2 = getResources().getIdentifier(imageName2, "drawable",getPackageName());
                    imageView1.setImageResource(resID2);
                }
            }

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
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

