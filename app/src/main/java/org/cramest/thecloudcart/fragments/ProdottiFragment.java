package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.adapter.ProdottoAdapter;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.dialogs.ProdottoDialog;

import java.util.ArrayList;

public class ProdottiFragment extends Fragment implements ProdottoDialog.OnProdottoDialogInteractionListener, Dati.ProdottiCompratiListener, Dati.ProdottiInListaEliminatiListener {
    private static final String ARG_PARAM = "listID";

    private int listID;
    private Lista curList;
    private ArrayList<ProdottoInLista> prodottiInLista;
    private ProdottoAdapter prodottoAdapter;
    private OnProdottiFragmentInteractionListener mListener;

    public ProdottiFragment() {
    }

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

    public void aggiornaLista() {
        prodottoAdapter.notifyDataSetChanged();
    }

    private void main(){
        prodottiInLista = Dati.getProdottoInListaByListID(listID);
        setListAdapter();
        Button buttonAggiungi = (Button)getView().findViewById(R.id.btn_aggiungi_prodotto);
        buttonAggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAggiungiProdotto(listID);
            }
        });
    }

    private void setListAdapter(){
        ListView lv = (ListView)getView().findViewById(R.id.listProdotti);
        prodottoAdapter = new ProdottoAdapter(getActivity(), R.layout.adapter_prodotto, prodottiInLista);
        lv.setAdapter(prodottoAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProdottoInLista curProdotto = prodottiInLista.get(position);
                ProdottoDialog prodottoDialog = new ProdottoDialog();
                prodottoDialog.showDialog(getActivity(), ProdottiFragment.this, curProdotto);
                //Alla fine notifichiamo MainActivity
                onProdottoClicked(curProdotto);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prodotti, container, false);
        ((TextView) v.findViewById(R.id.text_view_header_name)).setText(curList.getNome());
        return v;
    }

    public void onProdottoClicked(ProdottoInLista prodottoInLista) {
        if (mListener != null) {
            mListener.OnProdottoClicked(prodottoInLista);
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
                    + " must implement OnProdottiFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnProdottiFragmentInteractionListener) {
            mListener = (OnProdottiFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnProdottiFragmentInteractionListener");
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
    public void OnCompraProdotto(ProdottoInLista prodotto) {
        Dati.compraProdotto(prodotto, this);
        mListener.OnCompraProdotto(prodotto);
    }

    @Override
    public void OnEliminaProdotto(ProdottoInLista prodotto) {
        Dati.rimuoviProdottoInLista(prodotto, this);
        mListener.OnEliminaProdotto(prodotto);
    }

    @Override
    public void OnProdottoComprato(ProdottoInLista prodottoInLista) {
        aggiornaLista();
        mListener.OnProdottoComprato(prodottoInLista);
    }

    @Override
    public void OnProdottoNonComprato(ProdottoInLista prodottoInLista, String errore) {
        mListener.OnProdottoNonComprato(prodottoInLista, errore);
    }

    @Override
    public void OnProdottoInListaEliminato(ProdottoInLista prodottoInLista) {
        mListener.OnProdottoInListaEliminato(prodottoInLista);
    }

    @Override
    public void OnProdottoInListaNonEliminato(ProdottoInLista prodottoInLista, String errore) {
        mListener.OnProdottoInListaNonEliminato(prodottoInLista, errore);
    }

    public interface OnProdottiFragmentInteractionListener {
        void OnProdottoClicked(ProdottoInLista prodottoInLista);
        void OnAggiungiProdotto(int listID);

        void OnCompraProdotto(ProdottoInLista prodottoInLista);

        void OnEliminaProdotto(ProdottoInLista prodottoInLista);

        void OnProdottoComprato(ProdottoInLista prodottoInLista);

        void OnProdottoNonComprato(ProdottoInLista prodottoInLista, String errore);

        void OnProdottoInListaEliminato(ProdottoInLista prodottoInLista);

        void OnProdottoInListaNonEliminato(ProdottoInLista prodottoInLista, String errore);
    }
}
