package org.cramest.thecloudcart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import org.cramest.thecloudcart.classi.Categoria;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.Prodotto;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AggiungiProdottoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AggiungiProdottoFragment extends Fragment implements DataHandler{

    private static final String ARG_PARAM1 = "userID";
    private static final String ARG_PARAM2 = "listID";
    private int listID;
    private Lista curList;
    private String userID;

    private OnProdottoAggiuntoListener mListener;

    private ArrayList<Prodotto> curProdottiByCategoria;
    private Prodotto curProdottoSel;

    public AggiungiProdottoFragment() {
        // Required empty public constructor
    }

    /** TODO : Rifare documentazione
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param listID Parameter 1.
     * @return A new instance of fragment AggiungiListaFragment.
     */
    public static AggiungiProdottoFragment newInstance(String userID,int listID) {
        AggiungiProdottoFragment fragment = new AggiungiProdottoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userID);
        args.putInt(ARG_PARAM2, listID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM1);
            listID = getArguments().getInt(ARG_PARAM2);
            curList = Dati.getListaByID(listID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aggiungi_prodotto_lista, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //TODO : Fai qui
        TextView titolo =(TextView)getActivity().findViewById(R.id.title_aggiungi_prodotto);
        titolo.setText("Aggiungi a lista : "+curList.getNome());
        setCategorieSpinner();
        updatePodottoSpinner(1);
        Button aggiungi = (Button)getActivity().findViewById(R.id.aggiungi_prodotto_button);
        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aggiungiProdottoInLista(curProdottoSel);
            }
        });
    }

    private void aggiungiProdottoInLista(Prodotto prodotto){
        if(prodotto != null) {
            String descrizione = ((EditText) getActivity().findViewById(R.id.descrizione)).getText().toString();
            int quantita = Integer.parseInt(((EditText) getActivity().findViewById(R.id.quantita)).getText().toString());
            ProdottoInLista prodottoAggiunto = new ProdottoInLista(listID, prodotto, quantita, descrizione);

            //Aggiungiamo la lista tramite l'API
            String[] parametri = {"req","userID","listID","productID","quantity","description"};
            String[] valori = {"addProduct",userID,listID+"",prodotto.getID()+"",quantita+"",descrizione};
            if(Connettore.getInstance(getActivity()).isNetworkAvailable()) {
                //Chiediamo al sito le liste
                Connettore.getInstance(getActivity()).GetDataFromWebsite(AggiungiProdottoFragment.this, "aggiungiLista", parametri, valori);
            }else{
                //TODO : Aggiunta prodotto in locale alla lista degli aggiornamenti
            }
            //Comunque sia alla fine dobbiamo aggiungere il prodotto alla lista
            Dati.aggiungiProdottoInLista(prodottoAggiunto);
        }else{
            //TODO : Gestisci il caso nessun prodotto sia selezionato
        }
    }

    private void setCategorieSpinner(){

        Spinner spinner = (Spinner)getActivity().findViewById(R.id.categoria_spinner);
        //Recuperiamo i nomi delle categorie
        ArrayList<String> categorie = Dati.getCategorieAsString();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categorie);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        //Le categorie sono in ordine di ID quindi l'ID coincide con la posizione+1 all'interno dello spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updatePodottoSpinner(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void updatePodottoSpinner(int idCategoria){
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.prodotto_spinner);
        //Recuperiamo i nomi delle categorie
        curProdottiByCategoria = Dati.getProdottiByCategoria(idCategoria);
        ArrayList<String> prodotti = Dati.prodottiToString(curProdottiByCategoria);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, prodotti);
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

    public void onProdottoAggiunto(ProdottoInLista prodotto) {
        if (mListener != null) {
            mListener.OnProdottoAggiunto(prodotto);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProdottoAggiuntoListener) {
            mListener = (OnProdottoAggiuntoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success) {
            Toast.makeText(getContext(),data, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),data, Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnProdottoAggiuntoListener {
        void OnProdottoAggiunto(ProdottoInLista prodotto);
    }
}
