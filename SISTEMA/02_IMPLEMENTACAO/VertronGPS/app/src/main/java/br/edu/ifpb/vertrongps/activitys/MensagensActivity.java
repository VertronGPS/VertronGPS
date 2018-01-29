package br.edu.ifpb.vertrongps.activitys;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.edu.ifpb.vertrongps.R;
import br.edu.ifpb.vertrongps.adapters.MensagensAdapter;
import br.edu.ifpb.vertrongps.entities.Mensagem;

/**
 * Created by emerson on 16/08/17.
 */

public class MensagensActivity extends AppCompatActivity {

    private ListView listView;
    private MensagensAdapter adapter;
    private ArrayList<Mensagem> listaMensagens;
    private BroadcastReceiver broadcastReceiver;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens);

        listView = (ListView) findViewById(R.id.listView);
        atualizaView();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                atualizaView();
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mensagem mensagem = listaMensagens.get(position);
                showDialog(mensagem);
            }
        });

    }

    public void atualizaView() {
        if (fetchInbox() != null) {
            adapter = new MensagensAdapter(this, fetchInbox());
            listView.setAdapter(adapter);
        }
    }

    public ArrayList<Mensagem> fetchInbox() {
        listaMensagens = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor.moveToFirst()) {
            Mensagem msg = new Mensagem(cursor.getString(cursor.getColumnIndexOrThrow("body")), cursor.getString(cursor.getColumnIndexOrThrow("address")), DateFormat.format("dd/MM/yyyy HH:mm", retornaData(cursor.getString(cursor.getColumnIndexOrThrow("date")))).toString());
            listaMensagens.add(msg);
        }

        while (cursor.moveToNext()) {
            Mensagem mensagem = new Mensagem(cursor.getString(cursor.getColumnIndexOrThrow("body")), cursor.getString(cursor.getColumnIndexOrThrow("address")), DateFormat.format("dd/MM/yyyy HH:mm", retornaData(cursor.getString(cursor.getColumnIndexOrThrow("date")))).toString());
            listaMensagens.add(mensagem);
        }

        ArrayList<Mensagem> msgsFiltradas = new ArrayList<Mensagem>();

        for (Mensagem msg : listaMensagens){
            if (msg.getTexto().contains("vertrongps")){
                msgsFiltradas.add(msg);
            }
        }

        return msgsFiltradas;
    }

    public Date retornaData(String data) {
        String date = data;
        Long timestamp = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Date finaldate = calendar.getTime();

        return finaldate;
    }

    @Override
    protected void onResume() {
        registerReceiver(broadcastReceiver, new IntentFilter("atualizar"));
        atualizaView();
        super.onResume();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private void showDialog(Mensagem mensagem) {
        Dialog dialog = new Dialog(this);
        dialog.setTitle("Informação");
        dialog.setContentView(R.layout.dialog_layout);
        TextView tvDest = (TextView) dialog.findViewById(R.id.tvDest);
        tvDest.setText(mensagem.getRemetente());
        TextView tvData = (TextView) dialog.findViewById(R.id.tvData);
        tvData.setText(mensagem.getData());
        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button botaoAbrirLink = (Button) dialog.findViewById(R.id.botaoAbrirLink);
        if (!mensagem.getTexto().contains("gpsfail") && mensagem.getTexto().contains("getlocation")){
            this.url = "";
            String[] texto = mensagem.getTexto().split(":");
            this.url = "https://maps.google.com/maps/?&z=10&q="+texto[2]+","+texto[3];

            botaoAbrirLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(browserIntent);
                }
            });
        }

        else {
            botaoAbrirLink.setVisibility(View.INVISIBLE);
        }
        tvMsg.setText(mensagem.getTexto());

        dialog.show();
    }

}