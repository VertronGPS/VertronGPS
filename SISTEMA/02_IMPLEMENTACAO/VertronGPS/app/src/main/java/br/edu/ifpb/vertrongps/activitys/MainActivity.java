package br.edu.ifpb.vertrongps.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.edu.ifpb.vertrongps.R;

/**
 * Created by andre on 01/05/17.
 */

public class MainActivity extends AppCompatActivity {

    private Button botaoEnviar;
    private Button botaoVisualizar;
    private Button botaoBuscar;

    private static final int REQUEST_SEND_SMS = 255;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controlarSMS();

        botaoBuscar = (Button) findViewById(R.id.botaoBuscar);
        botaoEnviar = (Button) findViewById(R.id.botaoEnviar);
        botaoVisualizar = (Button) findViewById(R.id.botaoVisualizar);

        this.botaoBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BuscarActivity.class));
            }
        });

        this.botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EnviarActivity.class));
            }
        });

        this.botaoVisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MensagensActivity.class));
            }
        });

    }

    // SOBRESCREVENDO MÉTODO QUE É CHAMADO SEMPRE QUE UMA REQUISIÇÃO DE PERMISSÃO É SOLICITADA
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SEND_SMS) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }

        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void controlarSMS() {

        try {

            // VERIFICANDO SE É PRECISO PERDIR A PERMISSÃO PARA ENVIAR E VISUALIZAR SMS
            // OBS.: A PARTIR DO MARSHMALLOW É PRECISO
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {

            } else {

                // CASO NÃO HAJA PERMISSÃO PARA ENVIAR E VISUALIZAR SMS, UMA BREVE EXPLICAÇÃO SERÁ EXIBIDA
                if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                    Toast.makeText(this, "É necessária permissão para enviar e vizualizar SMS.", Toast.LENGTH_LONG).show();
                }

                // SOLICITANTO PERMISSÃO PARA ENVIAR E VISUALIZAR SMS
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);

            }

        } catch (Exception e) {

            Toast.makeText(this, "Falha ao permitir envio e visualização de SMS.", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

    }

}