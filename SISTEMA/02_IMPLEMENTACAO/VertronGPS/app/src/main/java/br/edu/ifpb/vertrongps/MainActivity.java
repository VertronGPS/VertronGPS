package br.edu.ifpb.vertrongps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String mensagem;
    private String numeroTelefone;
    private Button botaoEnviar;
    private static final int REQUEST_SEND_SMS = 255;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mensagem = "TESTE VERTRON GPS";
        this.numeroTelefone = "02187991678707";
        this.botaoEnviar = (Button) findViewById(R.id.botaoEnviar);
        this.botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlarSMS(v);
            }
        });

    }

    // SOBRESCREVENDO MÉTODO QUE É CHAMADO SEMPRE QUE UMA REQUISIÇÃO É SOLICITADA
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SEND_SMS) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enviarSMS();
            }

        }

        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void controlarSMS(View v) {

        try {

            // VERIFICANDO SE É PRECISO PERDIR A PERMISSÃO PARA ENVIAR SMS
            // OBS.: A PARTIR DO MARSHMALLOW É PRECISO
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                enviarSMS();
            } else {

                // CASO NÃO HAJA PERMISSÃO PARA ENVIAR SMS, UMA BREVE EXPLICAÇÃO SERÁ EXIBIDA
                if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                    Toast.makeText(this, "É necessária permissão para enviar SMS.", Toast.LENGTH_LONG).show();
                }

                // SOLICITANTO PERMISSÃO PARA ENVIAR SMS
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);

            }

        } catch (Exception e) {

            Toast.makeText(this, "Falha ao enviar mensagem SMS.", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

    }

    public void enviarSMS() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmar");
        alert.setMessage("Tem certeza que deseja enviar? Taxas de operadoras poderão ser cobradas.");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // CÓDIGO QUE REALMENTE ENVIA O SMS
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(numeroTelefone, null, mensagem, null, null);
                Toast.makeText(getApplicationContext(), "Enviando mensagem.", Toast.LENGTH_LONG).show();

                dialog.dismiss();

            }
        });

        alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        alert.show();


    }

}