package org.cramest.thecloudcart.classi;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cramest.thecloudcart.R;

public class ProdottoAdapter extends ArrayAdapter<ProdottoInLista> {

    private ArrayList<ProdottoInLista> objects;

    public ProdottoAdapter(Context context, int textViewResourceId, ArrayList<ProdottoInLista> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_prodotto, null);
        }

        ProdottoInLista i = objects.get(position);
        if (i != null) {
            TextView nomeProdotto = (TextView) v.findViewById(R.id.textViewNomeProdotto);
            TextView quantita = (TextView) v.findViewById(R.id.textViewQta);
            nomeProdotto.setText(i.getProdotto().getNome());
            quantita.setText(i.getQuantita() + "");
        }

        return v;

    }

}
