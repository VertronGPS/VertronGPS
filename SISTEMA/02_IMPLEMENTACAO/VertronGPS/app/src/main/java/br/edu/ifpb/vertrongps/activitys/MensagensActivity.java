package br.edu.ifpb.vertrongps.activitys;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import br.edu.ifpb.vertrongps.entities.Mensagem;
import br.edu.ifpb.vertrongps.adapters.MensagensAdapter;
import br.edu.ifpb.vertrongps.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by emerson on 16/08/17.
 */

public class MensagensActivity extends AppCompatActivity {

    private ListView listView;
    private MensagensAdapter adapter;
    private ArrayList<Mensagem> listaMensagens;
    private BroadcastReceiver broadcastReceiver;

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

        return listaMensagens;
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

    private String todoListToString(ArrayList<Mensagem> lista) {
        StringBuilder builder = new StringBuilder();
        for (Mensagem mensagem : lista) {
            builder.append("Remetente: " + mensagem.getRemetente() + "\n");
            builder.append("Mensagem: " + mensagem.getTexto() + "\n");
            builder.append("Data: " + mensagem.getData() + "\n");
            builder.append("\n");
        }
        return builder.toString();
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
        tvMsg.setText(mensagem.getTexto());
        dialog.show();
    }

}