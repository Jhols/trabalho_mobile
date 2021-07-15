package com.example.atividade_mobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class PermissaoCamera extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissao_camera);

        Button btnCamera = findViewById(R.id.btn_abrir_camera);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requererPermissao()) {
                    abrirCamera();
                }
            }
        });

    }

    private boolean requererPermissao() {

        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        List<String> listaPermissoes = new ArrayList<>();

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listaPermissoes.add(Manifest.permission.CAMERA);
        }

        if (!listaPermissoes.isEmpty()) {
            ActivityCompat.requestPermissions(this, listaPermissoes.toArray(new String[listaPermissoes.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }

        return true;
    }

    private void abrirCamera() {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), REQUEST_ID_MULTIPLE_PERMISSIONS);
    }
}