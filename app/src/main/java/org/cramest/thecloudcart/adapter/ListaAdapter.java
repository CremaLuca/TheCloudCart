package org.cramest.thecloudcart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Lista;

import java.util.ArrayList;

public class ListaAdapter extends ArrayAdapter<Lista> {

    private ArrayList<Lista> objects;

    public ListaAdapter(Context context, int textViewResourceId, ArrayList<Lista> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.adapter_lista_prodotti, null);
        }

        Lista lista = objects.get(position);
        if (lista != null) {
            TextView nomeProdotto = (TextView) v.findViewById(R.id.textViewNomeLista);
            TextView quantita = (TextView) v.findViewById(R.id.textViewQtaLista);
            TextView numProdotti = (TextView) v.findViewById(R.id.text_view_numero_prodotti);

            nomeProdotto.setText(lista.getNome());
            if(lista.getQuantita() >= 0) {
                quantita.setVisibility(View.VISIBLE);
                numProdotti.setVisibility(View.VISIBLE);
                quantita.setText(lista.getQuantita() + "");
            }else{
                quantita.setVisibility(View.GONE);
                numProdotti.setVisibility(View.GONE);
            }
        }

        return v;

    }

}
