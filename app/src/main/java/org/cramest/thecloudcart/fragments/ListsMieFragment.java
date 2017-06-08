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
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.adapter.ListaAdapter;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;

import java.util.ArrayList;

/**
 * Created by cremaluca on 08/06/2017.
 */

public class ListsMieFragment extends Fragment {

    private static final String ARG_PARAM = "userID";

    private String userID;

    private ArrayList<Lista> listeMie;

    private ListaAdapter listAdapter;

    private OnListMieFragmentInteractionListener mListener;

    public ListsMieFragment() {
        // Required empty public constructor
    }

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
            listeMie = new ArrayList<Lista>(Dati.getListeMie());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //Setup delle listview
        setAdapter((ListView)getView().findViewById(R.id.listViewMie),listeMie);
        aggiungiBottoneCreaLista((ListView)getView().findViewById(R.id.listViewMie));
    }

    private void aggiungiBottoneCreaLista(ListView lv){
        //Aggiungiamo alla fine della lista il bottone
        final Button btnCrea = new Button(getActivity());
        btnCrea.setText("Crea nuova lista");
        btnCrea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAggiungiLista();
            }
        });
        //Alla fine della lista aggiungiamo questo
        lv.addFooterView(btnCrea);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liste_mie, container, false);
        return view;
    }

    public void onAggiungiLista() {
        if (mListener != null) {
            mListener.OnAggiungiLista();
        }
    }

    public void onListaLongClicked(Lista lista){
        if (mListener != null) {
            mListener.OnListaLongClicked(lista);
        }
    }

    @Override
    public void onAttach(Activity activity){
        System.out.println("ListMieFragment - Chiamato onAttach");
        super.onAttach(activity);
        if (activity instanceof OnListMieFragmentInteractionListener) {
            System.out.println("ListMieFragment - impostato mListener");
            mListener = (OnListMieFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListMieFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListMieFragmentInteractionListener) {
            mListener = (OnListMieFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListMieFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListMieFragmentInteractionListener {
        void OnListaClicked(int listID);
        void OnAggiungiLista();
        void OnListaLongClicked(Lista lista);
    }

    public void aggiornaLista(){
        listAdapter.notifyDataSetChanged();
    }

    private void setAdapter(final ListView lv, final ArrayList<Lista> liste){
        listAdapter = new ListaAdapter(getActivity(), R.layout.adapter_lista_prodotti, liste);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("ListMieFragment - Lista cliccata");
                int idLista = liste.get(position).getID();
                if(idLista > 0) {
                    mListener.OnListaClicked(idLista);
                }else{
                    Toast.makeText(getActivity(), "Errore, lista con id errato : "+idLista, Toast.LENGTH_SHORT).show();
                }
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("ListMieFragment - Lista cliccata a lungo");
                onListaLongClicked(liste.get(position));
                return true;
            }
        });

    }
}
