package com.example.browse_image2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Button button,download;
    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);
        button=findViewById(R.id.button);
        download=findViewById(R.id.download);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,1);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bitmapDrawable=(BitmapDrawable) imageView.getDrawable();
                bitmap=bitmapDrawable.getBitmap();

                FileOutputStream fileOutputStream=null;

                File sdCard = Environment.getExternalStorageDirectory();
                File Directory=new File(sdCard.getAbsolutePath()+ "/Download");
                Directory.mkdir();

                String filename=String.format("%d.jpg",System.currentTimeMillis());
                File outfile=new File(Directory,filename);

                Toast.makeText(MainActivity.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();

                try {
                    fileOutputStream=new FileOutputStream(outfile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outfile));
                    sendBroadcast(intent);
                    
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==1 && resultCode == RESULT_OK && null !=data){
            Uri selectedImage = data.getData();
            String[] filepath={MediaStore.Images.Media.DATA};

            Cursor cursor=getContentResolver().query(selectedImage,filepath,null,null,null);
            cursor.moveToFirst();
            int columneIndex=cursor.getColumnIndex(filepath[0]);
            String picturepath =cursor.getString(columneIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturepath));
            String filename=picturepath.substring(picturepath.lastIndexOf("/")+1);
            textView.setText(filename);

        }
    }


}