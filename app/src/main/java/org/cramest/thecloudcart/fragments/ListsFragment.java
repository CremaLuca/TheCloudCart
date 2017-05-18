package org.cramest.thecloudcart.fragments;

import android.content.Context;
import android.content.Intent;
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
import org.cramest.thecloudcart.adapter.ListaAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListsFragment.OnListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListsFragment extends Fragment{

    private static final String ARG_PARAM = "userID";

    private String userID;

    private ArrayList<Lista> listeMie;
    private ArrayList<Lista> listeCondivise;

    private OnListFragmentInteractionListener mListener;

    public ListsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userID L'ID dell'utente.
     * @return A new instance of fragment ListsFragment.
     */

    public static ListsFragment newInstance(String userID) {
        ListsFragment fragment = new ListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Lo chiamo qui così viene chiamato una volta sola teoricamente
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM);
            listeMie = Dati.getListeMie();
            listeMie.add(new Lista(-1,"Aggiungi nuova lista",-1));
            listeCondivise = Dati.getListeCondivise();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setAdapter(R.id.listViewMie,listeMie);
        setAdapter(R.id.listViewCondivise,listeCondivise);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        return view;
    }

    public void apriLista(int listID) {
        if (mListener != null) {
            mListener.OnListaClicked(listID);
        }
    }

    public void aggiungiLista() {
        if (mListener != null) {
            mListener.OnAggiungiLista();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void OnListaClicked(int listID);
        void OnAggiungiLista();
    }

    private void setAdapter(int viewID, final ArrayList<Lista> lista){
        ListaAdapter listViewadapter = new ListaAdapter(getActivity(), R.layout.list_lista_prodotti, lista);
        ListView lv = (ListView)getView().findViewById(viewID);
        lv.setAdapter(listViewadapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idLista = lista.get(position).getID();
                if(idLista != -1) {
                    apriLista(idLista);
                }else{
                    //Nuovo intent per aggiungere una nuova lista
                    aggiungiLista();
                }
            }
        });
        int totalHeight = listViewadapter.getView(0,null,lv).getMeasuredHeight() * lista.size();
        ViewGroup.LayoutParams params = lv.getLayoutParams();
        params.height = totalHeight + (lv.getDividerHeight() * (lv.getCount() - 1));
        lv.setLayoutParams(params);
        lv.requestLayout();
    }
}
