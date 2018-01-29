package br.edu.ifpb.vertrongps.activitys;

/**
 * Created by emerson on 16/08/17.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.edu.ifpb.vertrongps.R;
import br.edu.ifpb.vertrongps.entities.Contato;

public class EnviarActivity extends AppCompatActivity {
    private AutoCompleteTextView acObjText;
    private EditText edtPassword;
    private String array_spinner[];
    private Button botaoEnviar;
    private Button botaoConfirmar;
    private Button botaoAddContato;
    private Spinner spinner;
    private String msg;
    private Dialog dialog;
    private ArrayList<Contato> contatos = new ArrayList<Contato>();
    private ArrayList<String> nomes = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);

        array_spinner = new String[] {"Selecione", "ping", "getlocation"};
        spinner = (Spinner) findViewById(R.id.spinnerOpcoes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array_spinner);
        spinner.setAdapter(adapter);

        showContacts();

        acObjText = (AutoCompleteTextView) findViewById(R.id.autoCompleteContatos);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);
        acObjText.setAdapter(arrayAdapter);

        botaoEnviar = (Button) findViewById(R.id.buttonEnviar);

        botaoAddContato = (Button) findViewById(R.id.botaoAddContato);

        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlarSMS(v);
            }
        });

        botaoAddContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContato(v);
            }
        });

    }

    public void addContato(View view) {
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        if (!acObjText.getText().toString().trim().equals("")) {
            if (buscaNumero(acObjText.getText().toString()) == null) {
                intent.putExtra(ContactsContract.Intents.Insert.NAME, acObjText.getText().toString());
                finish();
                startActivity(intent);
            }
            Toast.makeText(this, "Já existe um contato com o mesmo nome", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, "Informe um nome para o contato", Toast.LENGTH_LONG).show();
    }

    private void showContacts() {

        Cursor cursor_contatos = null;
        ContentResolver contentResolver = getContentResolver();

        try {
            cursor_contatos = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Erro em contatos", ex.getMessage());
        }

        if (cursor_contatos.getCount() > 0) {

            while (cursor_contatos.moveToNext()) {
                Contato contato = new Contato();
                String contato_id = cursor_contatos.getString(cursor_contatos.getColumnIndex(ContactsContract.Contacts._ID));
                String contato_nome = cursor_contatos.getString(cursor_contatos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contato.setNome(contato_nome);

                int hasPhoneNumber = Integer.parseInt(cursor_contatos.getString(cursor_contatos.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                            , new String[]{contato_id}
                            , null);

                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contato.setNumero(phoneNumber);
                    }
                    phoneCursor.close();
                }
                contatos.add(contato);
                nomes.add(contato.getNome());
            }
        }
    }

    public void controlarSMS(View v) {

        try {

            enviarSMS();

        } catch (Exception e) {

            Toast.makeText(this, "Falha ao enviar mensagem SMS.", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

    }

    public String buscaNumero(String nome){
        String numero = "";
        for (Contato contato : contatos){
            if (contato.getNome().equals(nome)){
                numero = contato.getNumero();
            }
        }
        if (!numero.equals("")){
            return numero;
        }
        else {
            return null;
        }
    }

    public void enviarSMS() {

        if(spinner.getSelectedItemPosition() != 0){

            if(spinner.getSelectedItemPosition() == 1){
                msg = "ping";
            }

            else{
                if(spinner.getSelectedItemPosition() == 2){
                    msg = "getlocation";
                }
            }

            if(buscaNumero(acObjText.getText().toString()) != null){


                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Confirmar");
                alert.setMessage("Tem certeza que deseja enviar? Taxas de operadoras poderão ser cobradas.");
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            // CÓDIGO QUE REALMENTE ENVIA O SMS
                            showDialog();

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
                Toast.makeText(getApplicationContext(), "Contato não encontrado!", Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(getApplicationContext(), "Escolha um método de envio!", Toast.LENGTH_LONG).show();
        }

    }

    private void showDialog() {
        dialog = new Dialog(this);
        dialog.setTitle("Informação");
        dialog.setContentView(R.layout.dialog_password);
        edtPassword = (EditText) dialog.findViewById(R.id.editTextPassword);
        botaoConfirmar = (Button) dialog.findViewById(R.id.botaoConfirmar);

        botaoConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getText().toString().equals("3636")){
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(buscaNumero(acObjText.getText().toString()), null, msg.concat(":").concat(edtPassword.getText().toString()), null, null);
                    startActivity(new Intent(EnviarActivity.this, MainActivity.class));
                    Toast.makeText(getApplicationContext(), "Mensagem enviada, aguarde o retorno da requisição.", Toast.LENGTH_LONG).show();
                    dialog.hide();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Senha errada!", Toast.LENGTH_LONG).show();
                    edtPassword.setText("");
                }
            }
        });

        dialog.show();
    }
}