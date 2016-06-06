package com.example.henrique.gps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected GoogleMap map;
    private static final String TAG = "Exemplo GPS";
    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Fragment do mapa
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Configura o objeto GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SplashActivity sa = new SplashActivity();
        switch (item.getItemId()) {
            case R.id.action_my_location:
                String permissions[] = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,

                };

                boolean ok = PermissionsUtils.validate(this, 0, permissions);
                if (ok){
                    Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    Log.d(TAG, "lastlocation: " + l);

                    setMapLocation(l);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMapLocation(Location l) {
        if (map != null && l != null) {
            LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            map.animateCamera(update);
            Log.d(TAG, "setMapLocation: " + l);
            TextView text = (TextView) findViewById(R.id.text);
            text.setText(String.format("Lat/Lnt %f%f, provider: %s", l.getLatitude(), l.getLongitude(), l.getProvider()));
            CircleOptions circle = new CircleOptions().center(latLng);
            circle.fillColor(Color.RED);
            circle.radius(25); //metros
            map.clear();
            map.addCircle(circle);
        }
    }

    private void toast(String s) {
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(TAG, "onMapReady: " + map);
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    @Override
    public void onConnected(Bundle bundle) {
        toast("Conectado ao Google P. Services");
    }

    @Override
    public void onConnectionSuspended(int i) {
        toast("Conexao interrompida");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        toast("Erro ao conect : " + connectionResult);
    }

    // Método da Activity
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    // Método da Activity
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
}
