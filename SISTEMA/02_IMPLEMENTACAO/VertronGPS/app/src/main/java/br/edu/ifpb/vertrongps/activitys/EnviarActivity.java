package br.edu.ifpb.vertrongps.activitys;

/**
 * Created by emerson on 16/08/17.
 */

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.edu.ifpb.vertrongps.R;

public class EnviarActivity extends AppCompatActivity {
    private EditText mensagem;
    private EditText numeroTelefone;
    private String array_spinner[];
    private Button botaoEnviar;
    private Spinner spinner;
    private String msg;
    private static final int REQUEST_SEND_SMS = 255;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);

        array_spinner = new String[4];
        array_spinner[0] = "Selecione";
        array_spinner[1] = "Ping";
        array_spinner[2] = "GetLocation";
        array_spinner[3] = "Personalizada";
        spinner = (Spinner) findViewById(R.id.spinnerOpcoes);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener escolha = new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String item = spinner.getSelectedItem().toString();
                if (item.equals("Ping") || item.equals("GetLocation") || item.equals("Selecione")){
                    mensagem.setEnabled(false);
                }
                else{
                    mensagem.setEnabled(true);
                }
            }

            public void onNothingSelected(AdapterView<?> parent){

            }
        };

        spinner.setOnItemSelectedListener(escolha);

        mensagem = (EditText) findViewById(R.id.editTextMensagem);
        numeroTelefone = (EditText) findViewById(R.id.editTextContato);
        botaoEnviar = (Button) findViewById(R.id.buttonEnviar);
        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlarSMS(v);
            }
        });

    }

    // SOBRESCREVENDO MÉTODO QUE É CHAMADO SEMPRE QUE UMA REQUISIÇÃO DE PERMISSÃO É SOLICITADA
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

        if(!spinner.getSelectedItem().toString().equals("Selecione")){

            if(spinner.getSelectedItem().toString().equals("Ping")){
                msg = "ping";
            }

            else{
                if(spinner.getSelectedItem().toString().equals("GetLocation")){
                    msg = "getlocation";
                }

                else{
                    msg = mensagem.getText().toString();
                }
            }

            if(!numeroTelefone.getText().toString().isEmpty()){


                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Confirmar");
                alert.setMessage("Tem certeza que deseja enviar? Taxas de operadoras poderão ser cobradas.");
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            // CÓDIGO QUE REALMENTE ENVIA O SMS
                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage(numeroTelefone.getText().toString(), null, msg, null, null);
                            mensagem.setText("");
                            numeroTelefone.setText("");
                            spinner.setSelection(0);
                            Toast.makeText(getApplicationContext(), "Enviando mensagem.", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Falha ao enviar mensagem SMS.", Toast.LENGTH_LONG).show();
                        }

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

            else{
                Toast.makeText(getApplicationContext(), "Informe um número de contato!", Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(getApplicationContext(), "Escolha um método de envio!", Toast.LENGTH_LONG).show();
        }

    }
}