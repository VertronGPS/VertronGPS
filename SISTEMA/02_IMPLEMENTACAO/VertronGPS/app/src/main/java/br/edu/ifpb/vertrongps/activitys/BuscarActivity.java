package br.edu.ifpb.vertrongps.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.edu.ifpb.vertrongps.R;

/**
 * Created by vitor on 22/09/2017.
 */

public class BuscarActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private Button botaoVizualisar;
    private EditText latitude;
    private EditText longitude;
    private LatLng localizacao;
    private MarkerOptions ponto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        this.botaoVizualisar = (Button) findViewById(R.id.botaoVizualisar);
        this.botaoVizualisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Se houver algum ponto já marcado ele ira limpar o mapa e marcar o novo ponto
                try{
                    if(ponto == null){
                        vizualisarMap();
                    }
                    else{
                        mMap.clear();
                        vizualisarMap();
                    }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Coordenadas Inválidas.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean setLocalizacao() {

        this.latitude = (EditText) findViewById(R.id.txLatitude);
        this.longitude = (EditText) findViewById(R.id.txLongitude);

        if (!latitude.getText().toString().equals(null) && !longitude.getText().toString().equals(null)) {
            double lat = Double.parseDouble(latitude.getText().toString());
            double lon = Double.parseDouble(longitude.getText().toString());
            this.localizacao = new LatLng(lat, lon);
            return true;
        } else{
            Toast.makeText(this, "Preencha as coordenadas corretamente!", Toast.LENGTH_LONG);
            return false;
        }
    }

    public void vizualisarMap(){
        setLocalizacao();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(true);

        ponto = new MarkerOptions().position(localizacao).title("Vertron");
        mMap.addMarker(ponto);
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(localizacao));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(localizacao, 8));

    }
}
