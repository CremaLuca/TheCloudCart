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
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.Prodotto;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AggiungiProdottoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AggiungiProdottoFragment extends Fragment implements DataHandler{

    private static final String ARG_PARAM1 = "userID";
    private static final String ARG_PARAM2 = "listID";
    private static final String ARG_PARAM3 = "productID";

    private int listID;
    private Lista curList;
    private String userID;

    private OnAggiungiProdottiListener mListener;

    private ArrayList<Prodotto> curProdottiByCategoria;
    private Prodotto curProdottoSel = null;
    private Prodotto initialProdotto = null;

    private ProdottoInLista tmpProdottoInLista;

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

    public static AggiungiProdottoFragment newInstance(String userID,int listID,int productID) {
        AggiungiProdottoFragment fragment = new AggiungiProdottoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userID);
        args.putInt(ARG_PARAM2, listID);
        args.putInt(ARG_PARAM3, productID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Recupero i dati");
        if (getArguments() != null) {
            System.out.println("Recuperando");
            userID = getArguments().getString(ARG_PARAM1);
            listID = getArguments().getInt(ARG_PARAM2);
            curList = Dati.getListaByID(listID);
            int productID = getArguments().getInt(ARG_PARAM3);
            if(productID != 0 && productID>0) {
                initialProdotto = Dati.getProdottoByID(getArguments().getInt(ARG_PARAM3));
            }
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
        if(initialProdotto != null){
            Spinner spinnerCategorie = (Spinner)getActivity().findViewById(R.id.categoria_spinner);
            spinnerCategorie.setSelection(initialProdotto.getCategoria().getID()-1);
        }

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
                onDevoCreareNuovoProdotto(listID);
            }
        });
    }

    private void aggiungiProdottoInLista(Prodotto prodotto){
        if(prodotto != null) {
            String descrizione = ((EditText) getActivity().findViewById(R.id.descrizione)).getText().toString();
            int quantita = Integer.parseInt(((EditText) getActivity().findViewById(R.id.quantita)).getText().toString());
            tmpProdottoInLista = new ProdottoInLista(listID, prodotto, quantita, descrizione);

            //Aggiungiamo la lista tramite l'API
            String[] parametri = {"req","userID","listID","productID","quantity","description"};
            String[] valori = {"addProduct",userID,listID+"",prodotto.getID()+"",quantita+"",descrizione};
            if(Connettore.getInstance(getActivity()).isNetworkAvailable()) {
                //Chiediamo al sito le liste
                Connettore.getInstance(getActivity()).GetDataFromWebsite(AggiungiProdottoFragment.this, "aggiungiProdotto", parametri, valori);
            }else{
                //TODO : Aggiunta prodotto in locale alla lista degli aggiornamenti
            }
            //Comunque sia alla fine dobbiamo aggiungere il prodotto alla lista
            Dati.aggiungiProdottoInLista(tmpProdottoInLista);
        }else{
            //TODO : Gestisci il caso nessun prodotto sia selezionato
        }
    }

    private void setCategorieSpinner(){

        Spinner spinner = (Spinner)getActivity().findViewById(R.id.categoria_spinner);
        //Recuperiamo i nomi delle categorie
        ArrayList<String> stringCategorie = Dati.getCategorieAsString();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stringCategorie);
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
        if(initialProdotto != null){
            for(int i=0;i<prodotti.size();i++){
                if(prodotti.get(i).equals(initialProdotto.getNome())){
                    //Impostiamo il prodotto selezionato dove è stata trovata corrispondenza
                    //TODO : Cambiare mettere per ID non per nome
                    spinner.setSelection(i);
                }
            }
            //Abbiamo già selezionato, adesso non ci serve più e lo eliminiamo
            initialProdotto = null;
        }
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
    public void onDevoCreareNuovoProdotto(int listID) {
        if (mListener != null) {
            mListener.OnDevoCreareNuovoProdotto(listID);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success) {
            Toast.makeText(getContext(),data, Toast.LENGTH_SHORT).show();
            if(tmpProdottoInLista != null) {
                onProdottoAggiunto(tmpProdottoInLista);
            }
        }else{
            Toast.makeText(getContext(),data, Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnAggiungiProdottiListener {
        void OnProdottoAggiunto(ProdottoInLista prodotto);
        void OnDevoCreareNuovoProdotto(int listID);
    }
}
