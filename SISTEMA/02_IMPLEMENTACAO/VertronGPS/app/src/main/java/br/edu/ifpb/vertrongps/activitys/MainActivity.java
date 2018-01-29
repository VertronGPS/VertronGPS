package br.edu.ifpb.vertrongps.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.vertrongps.R;

/**
 * Created by andre on 01/05/17.
 */

public class MainActivity extends AppCompatActivity {

    private Button botaoEnviar;
    private Button botaoVisualizar;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botaoEnviar = (Button) findViewById(R.id.botaoEnviar);
        botaoVisualizar = (Button) findViewById(R.id.botaoVisualizar);

        checkAndRequestPermissions();

        this.botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()){
                    startActivity(new Intent(MainActivity.this, EnviarActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "É necessário ter permissões de acesso a SMS e Contatos!", Toast.LENGTH_LONG).show();
                    checkAndRequestPermissions();
                }
            }
        });

        this.botaoVisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()){
                    startActivity(new Intent(MainActivity.this, MensagensActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "É necessário ter permissões de acesso a SMS e Contatos!", Toast.LENGTH_LONG).show();
                    checkAndRequestPermissions();
                }
            }
        });
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);

        int readContacts = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        int writeContacts = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (readContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (writeContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}