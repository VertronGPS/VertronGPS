package br.edu.ifpb.vertrongps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String mensagem;
    String numeroTelefone;
    Button botaoEnviar;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mensagem = "TESTE VERTRON GPS";
        this.numeroTelefone = "02187991678707";
        this.botaoEnviar = (Button) findViewById(R.id.botaoEnviar);
        this.botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSMS(v);
            }
        });

    }

    public void enviarSMS(View v) {

        try{

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

            if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Não há permissão para enviar SMS.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 255);
            }

            else {
                Log.i("Mensagem", "Já há permissão para envio de SMS.");
            }

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(this.numeroTelefone, null, this.mensagem, null, null);
            Toast.makeText(getApplicationContext(), "Mensagem enviada.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Falha ao enviar mensagem SMS.", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

    }
}