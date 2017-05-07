package org.cramest.thecloudcart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.adapter.ProdottoAdapter;
import org.cramest.thecloudcart.classi.Prodotto;
import org.cramest.thecloudcart.classi.ProdottoInLista;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProdottiFragment.OnProdottiFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProdottiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProdottiFragment extends Fragment{

    private static final String ARG_PARAM = "listID";

    private int listID;
    private Lista curList;
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
     * @return A new instance of fragment ProdottiFragment.
     */
    public static ProdottiFragment newInstance(int listID) {
        ProdottiFragment fragment = new ProdottiFragment();
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

    private void main(){
        prodottiInLista = Dati.getProdottoInListaByListID(listID);
        prodottiInLista.add(new ProdottoInLista(0,new Prodotto("Aggiungi"),0,""));
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
                    //TODO : Se e' un prodotto normale apriremo una finestra con i dettagli del prodotto, senza una nuova activity
                    onProdottoClicked();
                }else if(curProdotto.getQuantita() == 0){
                    //TODO : Se dobbiamo invece aggiungere un prodotto creeremo una nuova activity alla quale passiamo l'id della lista e sulla
                    //Nuova activity chiederemo i dettagli del prodotto da aggiungere
                    onAggiungiProdotto(curList.getID());
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

    public void onProdottoClicked() {
        if (mListener != null) {
            mListener.OnProdottoClicked();
        }
    }

    public void onAggiungiProdotto(int listID) {
        if (mListener != null) {
            mListener.OnAggiungiProdotto(listID);
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

    public interface OnProdottiFragmentInteractionListener {
        void OnProdottoClicked();
        void OnAggiungiProdotto(int listID);
    }
}
