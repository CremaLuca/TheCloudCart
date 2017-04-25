package org.cramest.thecloudcart.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Prodotto;
import org.cramest.thecloudcart.classi.ProdottoAdapter;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.WebsiteDataManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProdottiFragment.OnProdottiFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProdottiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProdottiFragment extends Fragment implements DataHandler{

    private static final String ARG_PARAM1 = "listID";
    private static final String ARG_PARAM2 = "listName";

    private int listID;
    private String listName;
    private ArrayList<ProdottoInLista> prodottiInLista;

    private OnProdottiFragmentInteractionListener mListener;

    public ProdottiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param listID ID della lista da cui ricavare i prodotti.
     * @param listName Nome della lista da mostrare.
     * @return A new instance of fragment ProdottiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProdottiFragment newInstance(int listID,String listName) {
        ProdottiFragment fragment = new ProdottiFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, listID);
        args.putString(ARG_PARAM2, listName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listID = getArguments().getInt(ARG_PARAM1);
            listName = getArguments().getString(ARG_PARAM2);
        }
    }

    private void main(){
        String[] pars = {"req","listID"};
        String[] vals = {"getProductList",listID+""};
        Connettore.getInstance(getActivity()).GetDataFromWebsite(this,"ProdottiLista",pars,vals);
        prodottiInLista = new ArrayList<ProdottoInLista>();
        prodottiInLista.add(new ProdottoInLista(new Prodotto("Caricamento..."),-1,""));
        setListAdapter();
    }

    private void setListAdapter(){
        ListView lv = (ListView)getView().findViewById(R.id.listProdotti);
        ProdottoAdapter listViewadapter = new ProdottoAdapter(getActivity(), R.layout.list_prodotto, prodottiInLista);
        lv.setAdapter(listViewadapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProdottoInLista curProdotto = prodottiInLista.get(position);
                if(curProdotto.getQuantita() > 0){
                    //Se e' un prodotto normale apriremo una finestra con i dettagli del prodotto, senza una nuova activity
                }else if(curProdotto.getQuantita() == 0){
                    //Se dobbiamo invece aggiungere un prodotto creeremo una nuova activity alla quale passiamo l'id della lista e sulla
                    //Nuova activity chiederemo i dettagli del prodotto da aggiungere
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prodotti, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.OnProdottiFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProdottiFragmentInteractionListener) {
            mListener = (OnProdottiFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        main();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success){
            if(nome.equals("ProdottiLista")){
                if(data != null) {
                    prodottiInLista = new ArrayList<ProdottoInLista>(Arrays.asList(WebsiteDataManager.getProdottiInLista(data)));
                    prodottiInLista.add(new ProdottoInLista(new Prodotto("Aggiungi prodotto"), 0, ""));
                    //Inseriamo nel ListView le liste
                    setListAdapter();
                }else{
                    System.out.println("Lista "+ listName + " vuota");
                    prodottiInLista.clear();
                    prodottiInLista.add(new ProdottoInLista(new Prodotto("Aggiungi prodotto"), 0, ""));
                    setListAdapter();
                }
            }
        }
    }

    public interface OnProdottiFragmentInteractionListener {
        void OnProdottiFragmentInteraction();
    }
}
