package com.example.henrique.hellocamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ImageView imgView;
    // Caminho para salvar o arquivo
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = (ImageView) findViewById(R.id.imagem);
        ImageButton b =
                (ImageButton) findViewById(R.id.btAbrirCamera);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Cria o caminho do arquivo no SD card
                file = SDCardUtils.getPrivateFile(getBaseContext(),
                        "foto.jpg",
                        Environment.DIRECTORY_PICTURES);
                // Chama a intent informando o arquivo para salvar a foto
                Intent i =
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(file));
                startActivityForResult(i, 0);
            }
        });
        if (savedInstanceState != null){
            // Caso a tela tenha girado, recupera o estado
            file =
                (File) savedInstanceState.getSerializable("file");
            showImage(file);
        }
    }

    @Override
    protected  void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Salvar o estado caso gire a tela
        outState.putSerializable("file", file);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data){
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (data != null){
            Bundle bundle = data.getExtras();
            if (bundle != null){
                Bitmap bitmap = (Bitmap) bundle.get("data");
                imgView.setImageBitmap(bitmap);
            }
        }

        if (resultCode == RESULT_OK && file != null){
            // Recebe o resultado da intent da câmera
            showImage(file);
        }
    }

    // Atualiza a imagem na tela
    private void showImage(File file){
        if (file != null && file.exists()){
            Log.d("foto", file.getAbsolutePath());
            int w = imgView.getWidth();
            int h = imgView.getHeight();
            // Redimensionar foto antes de mostrá-la no ImageView
            Bitmap bitmap =
                ImageResizeUtils.getResizedImage(Uri.fromFile(file),
                    w, h, false);
            Toast.makeText(this, "w/h: " +
                    bitmap.getWidth() +
                    "/" +
                    bitmap.getHeight(),
                    Toast.LENGTH_SHORT).show();
            imgView.setImageBitmap(bitmap);
        }
    }
}
