package org.cramest.thecloudcart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.ProdottoInLista;

import java.util.ArrayList;

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
            v = inflater.inflate(R.layout.adapter_prodotto, null);
        }

        ProdottoInLista prodottoInLista = prodotti.get(position);
        if (prodottoInLista != null) {
            TextView nomeProdotto = (TextView) v.findViewById(R.id.textViewNomeProdotto);
            TextView quantita = (TextView) v.findViewById(R.id.text_view_quantita);
            TextView descrizione = (TextView) v.findViewById(R.id.textViewDescrizioneProdotto);

            nomeProdotto.setText(prodottoInLista.getProdotto().getNome());
            quantita.setText(prodottoInLista.getQuantita() + "");

            if(prodottoInLista.getDescrizione() != null && !prodottoInLista.getDescrizione().equals("")){
                System.out.println("ProdottoAdapter("+prodottoInLista.getProdotto().getNome()+") - Imposto la descrizione '"+prodottoInLista.getDescrizione()+"' ");
                descrizione.setText(prodottoInLista.getDescrizione());
            }else{
                System.out.println("ProdottoAdapter("+prodottoInLista.getProdotto().getNome()+") - Imposto descrizione nulla");
                descrizione.setText("Nessuna descrizione");
                descrizione.setVisibility(View.GONE);
                descrizione.setHeight(0);
            }
        }

        return v;

    }

}
