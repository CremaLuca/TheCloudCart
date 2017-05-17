package org.cramest.thecloudcart.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.ProdottoInLista;

public class ProdottoAdapter extends ArrayAdapter<ProdottoInLista> {

    private ArrayList<ProdottoInLista> prodotti;

    public ProdottoAdapter(Context context, int textViewResourceId, ArrayList<ProdottoInLista> objects) {
        super(context, textViewResourceId, objects);
        this.prodotti = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_prodotto, null);
        }

        ProdottoInLista i = prodotti.get(position);
        if (i != null) {
            TextView nomeProdotto = (TextView) v.findViewById(R.id.textViewNomeProdotto);
            TextView quantita = (TextView) v.findViewById(R.id.textViewQta);
            TextView descrizione = (TextView) v.findViewById(R.id.textViewDescrizioneProdotto);
            nomeProdotto.setText(i.getProdotto().getNome());
            if(i.getQuantita() > 0) {
                quantita.setText(i.getQuantita() + "");
            }else{
                ((TextView) v.findViewById(R.id.textViewNumProdotto)).setText("");
                quantita.setText("");
            }
            if(i.getDescrizione() != null || !i.getDescrizione().equals("")){
                descrizione.setText(i.getDescrizione());
            }else{
                //descrizione.setVisibility(View.GONE);
                descrizione.setHeight(0);
            }
        }

        return v;

    }

}
