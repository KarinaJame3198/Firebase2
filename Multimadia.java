package com.example.laujame.firebase2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Multimadia extends AppCompatActivity {

    private ImageView botonCamara;
    private ImageView botonMusica;
    private ImageView botonVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimadia);

        botonCamara = findViewById(R.id.imageCamara);
        botonMusica = findViewById(R.id.imageMusic);
        botonVideo  = findViewById(R.id.imagVideo);

        botonMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMusica();
            }
        });

        botonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirVideo();
            }
        });


        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TomarFoto();

            }
        });
    }



    public void abrirMusica(){

        Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        startActivityForResult(intent, 3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                //Aqui guardo mi imagen
                //El 1 es el requestCode que manda es startActivityForResult

                //Variable que almacena la respuesta de la toma
                //de la foto en un mapa de bits
                Date date = new Date();
                Bitmap picture = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                picture.compress(Bitmap.CompressFormat.PNG, 0,arrayOutputStream);
                File file = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "nombre"+
                                date.getTime()+
                                date.getHours()+
                                date.getMinutes()+
                                date.getSeconds() +
                                ".png");

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(arrayOutputStream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    public void  abrirVideo(){

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, 2);
    }

    public void TomarFoto(){
        //Permisos en tiempo de ejecucion

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);


    }



}
