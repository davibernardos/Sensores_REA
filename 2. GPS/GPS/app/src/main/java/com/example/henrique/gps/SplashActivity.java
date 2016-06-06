package com.example.henrique.gps;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import java.security.Permission;
import java.security.Permissions;

public class SplashActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);
        // Permições necessárias
        String permissions[] = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
        };
        // Validação das permissões
        boolean ok = PermissionsUtils.validate(this, 0, permissions);
        if (ok) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
