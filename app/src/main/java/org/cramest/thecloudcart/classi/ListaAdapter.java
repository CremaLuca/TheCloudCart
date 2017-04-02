package org.cramest.thecloudcart.classi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cramest.thecloudcart.R;

import java.util.ArrayList;

public class ListaAdapter extends ArrayAdapter<Lista> {

    private ArrayList<Lista> objects;

    public ListaAdapter(Context context, int textViewResourceId, ArrayList<Lista> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_lista_prodotti, null);
        }

        Lista i = objects.get(position);
        if (i != null) {
            TextView nomeProdotto = (TextView) v.findViewById(R.id.textViewNomeLista);
            TextView quantita = (TextView) v.findViewById(R.id.textViewQtaLista);
            TextView numProdotti = (TextView) v.findViewById(R.id.textViewNumProdotto);
            nomeProdotto.setText(i.getNome());
            if(i.getQuantita() >= 0) {
                quantita.setVisibility(View.VISIBLE);
                numProdotti.setVisibility(View.VISIBLE);
                quantita.setText(i.getQuantita() + "");
            }else{
                quantita.setVisibility(View.GONE);
                numProdotti.setVisibility(View.GONE);
            }
        }

        return v;

    }

}
