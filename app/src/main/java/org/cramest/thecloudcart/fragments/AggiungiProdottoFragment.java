package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.Prodotto;
import org.cramest.thecloudcart.classi.ProdottoInLista;

import java.util.ArrayList;


public class AggiungiProdottoFragment extends Fragment implements Dati.OnProdottiAggiungiListener {

    private static final String ARG_PARAM = "listID";

    private int listID;
    private Lista curList;

    private OnAggiungiProdottiListener mListener;

    private ArrayList<Prodotto> curProdottiByCategoria;
    private Prodotto curProdottoSel = null;

    public AggiungiProdottoFragment() {

    }

    public static AggiungiProdottoFragment newInstance(int listID) {
        AggiungiProdottoFragment fragment = new AggiungiProdottoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, listID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listID = getArguments().getInt(ARG_PARAM);
            curList = Dati.getListaByID(listID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aggiungi_prodotto_lista, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        TextView titolo =(TextView)getActivity().findViewById(R.id.title_aggiungi_prodotto);
        titolo.setText(getString(R.string.Add_to_list) + ":" + curList.getNome());
        setCategorieSpinner();
        updatePodottoSpinner(0);

        Button aggiungi = (Button)getActivity().findViewById(R.id.aggiungi_prodotto_button);
        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aggiungiProdottoInLista(curProdottoSel);
            }
        });

        Button crea = (Button)getActivity().findViewById(R.id.button_crea_prodotto);
        crea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnDevoCreareNuovoProdotto(listID);
            }
        });
    }

    private void aggiungiProdottoInLista(Prodotto prodotto){
        if(prodotto != null) {
            String descrizione = ((EditText) getActivity().findViewById(R.id.descrizione)).getText().toString();
            int quantita = 1;
            try {
                quantita = Integer.parseInt(((EditText) getActivity().findViewById(R.id.quantita)).getText().toString());
            } catch (Exception e) {
                System.out.println("AggiungiProdottoFragment - Manca la quantità, non importa");
            }
            ProdottoInLista tmpProdottoInLista = new ProdottoInLista(listID, prodotto, quantita, descrizione);

            mListener.OnDevoAggiungereProdotto(tmpProdottoInLista);
            Dati.aggiungiProdottoALista(tmpProdottoInLista, this);

        }else{
            System.out.println("AggiungiProdottoFragment - Nessun prodotto selezionato");
            Toast.makeText(getActivity(), "Nessun prodotto selezionato", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCategorieSpinner(){

        Spinner spinner = (Spinner)getActivity().findViewById(R.id.categoria_spinner);
        //Recuperiamo i nomi delle categorie
        ArrayList<String> stringCategorie = Dati.getCategorieAsString();
        //Questo sposta di uno la posizione delle categorie
        stringCategorie.add(0, getString(R.string.Recommended));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, stringCategorie);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        //Le categorie sono in ordine di ID quindi l'ID coincide con la posizione+1 all'interno dello spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //La categoria sarà posizione+1 ma -1 perchè la 0 è consigliati
                updatePodottoSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void updatePodottoSpinner(int idCategoria){
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.prodotto_spinner);
        //Recuperiamo i nomi delle categorie
        curProdottiByCategoria = Dati.getProdottiByCategoria(idCategoria, listID);
        ArrayList<String> prodotti = Dati.prodottiToString(curProdottiByCategoria);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, prodotti);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                curProdottoSel = curProdottiByCategoria.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAggiungiProdottiListener) {
            mListener = (OnAggiungiProdottiListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAggiungiProdottiListener");
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if (activity instanceof OnAggiungiProdottiListener) {
            mListener = (OnAggiungiProdottiListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnAggiungiProdottiListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void OnProdottoAggiunto(ProdottoInLista prodottoInLista) {
        mListener.OnProdottoAggiunto(prodottoInLista);
    }

    @Override
    public void OnProdottoNonAggiunto(ProdottoInLista prodottoInLista, String errore) {
        mListener.OnProdottoNonAggiunto(prodottoInLista, errore);
    }

    public interface OnAggiungiProdottiListener {
        void OnDevoAggiungereProdotto(ProdottoInLista prodottoInLista);
        void OnProdottoAggiunto(ProdottoInLista prodottoInLista);

        void OnProdottoNonAggiunto(ProdottoInLista prodottoInLista, String errore);
        void OnDevoCreareNuovoProdotto(int listID);
    }
}
