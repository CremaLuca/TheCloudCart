package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.adapter.ListaAdapter;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;

import java.util.ArrayList;

/**
 * Created by User on 11/06/2017.
 */

public class ListeMieFragment extends Fragment {

    private static final String ARG_PARAM = "userID";

    private String userID;

    private ArrayList<Lista> listeMie;

    private ListaAdapter listAdapter;

    private OnListeMieFragmentInteractionListener mListener;

    public ListeMieFragment() {
        // Required empty public constructor
    }

    public static ListeMieFragment newInstance(String userID) {
        ListeMieFragment fragment = new ListeMieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("ListeMieFragment - OnCreate");
        //Lo chiamo qui così viene chiamato una volta sola teoricamente
        if (getArguments() != null) {
            System.out.println("ListeMieFragment - OnCreate con argomenti");
            userID = getArguments().getString(ARG_PARAM);
            listeMie = new ArrayList<Lista>(Dati.getListeMie());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liste_mie, container, false);
        if (listeMie != null) {
            setAdapter((ListView) view.findViewById(R.id.listViewMie), listeMie);
            view.findViewById(R.id.button_crea_lista).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAggiungiLista();
                }
            });
        } else {
            System.out.println("ListeMieFragment - listeMie e' null");
        }
        return view;
    }

    public void onAggiungiLista() {
        if (mListener != null) {
            mListener.OnAggiungiLista();
        }
    }

    public void onListaLongClicked(Lista lista) {
        if (mListener != null) {
            mListener.OnListaLongClicked(lista);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        System.out.println("ListeMieFragment - Chiamato onAttach");
        super.onAttach(activity);
        if (activity instanceof OnListeMieFragmentInteractionListener) {
            System.out.println("ListeMieFragment - impostato mListener");
            mListener = (OnListeMieFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListeMieFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListeMieFragmentInteractionListener) {
            mListener = (OnListeMieFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListeMieFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListeMieFragmentInteractionListener {
        void OnListaClicked(int listID);

        void OnAggiungiLista();

        void OnListaLongClicked(Lista lista);
    }

    public void aggiornaLista() {
        listAdapter.notifyDataSetChanged();
    }

    private void setAdapter(final ListView lv, final ArrayList<Lista> liste) {
        listAdapter = new ListaAdapter(getActivity(), R.layout.adapter_lista_prodotti, liste);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("ListFragment - Lista cliccata");
                int idLista = liste.get(position).getID();
                if (idLista > 0) {
                    mListener.OnListaClicked(idLista);
                } else {
                    Toast.makeText(getActivity(), "Errore, lista con id errato : " + idLista, Toast.LENGTH_SHORT).show();
                }
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("ListFragment - Lista cliccata a lungo");
                onListaLongClicked(liste.get(position));
                return true;
            }
        });

    }
}
