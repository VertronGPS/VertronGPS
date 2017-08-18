package br.edu.ifpb.vertrongps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.edu.ifpb.vertrongps.entities.Mensagem;
import br.edu.ifpb.vertrongps.R;

import java.util.ArrayList;

/**
 * Created by Emerson on 16/08/2017.
 */
public class MensagensAdapter extends BaseAdapter {

    private ArrayList<Mensagem> lista;
    private LayoutInflater layoutInflater;
    public MensagensAdapter(Context context, ArrayList<Mensagem> lista){
        this.lista = lista;
        layoutInflater = layoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Mensagem mensagem = lista.get(position);

        view = layoutInflater.inflate(R.layout.mensagens_adapter,null);

        TextView tvNumero = (TextView) view.findViewById(R.id.tvNumero);

        tvNumero.setText(mensagem.getDestinatario());

        return view;
    }
}