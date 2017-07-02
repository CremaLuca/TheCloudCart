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
import org.cramest.thecloudcart.classi.Utente;
import org.cramest.thecloudcart.dialogs.CondividiDialog;
import org.cramest.thecloudcart.dialogs.ListaDialog;

import java.util.ArrayList;

/**
 * Created by User on 11/06/2017.
 */

public class ListeMieFragment extends Fragment implements ListaDialog.OnListaDialogInteractionListener, CondividiDialog.OnCondividiDialogInteractionListener, Dati.OnListeEliminaListener, Dati.OnListeCondividiListener {

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
            if (Dati.getListeMie() != null) {
                listeMie = new ArrayList<>(Dati.getListeMie());
            } else {
                listeMie = new ArrayList<>();
            }
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

        ListaDialog listaDialog = new ListaDialog();
        listaDialog.showDialog(getActivity(), this, lista, userID);

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

    @Override
    public void OnEliminaLista(Lista lista) {
        Dati.eliminaLista(lista, this);
        mListener.OnEliminaLista(lista);
    }

    @Override
    public void OnCondividiLista(Lista lista) {
        CondividiDialog condividiDialog = new CondividiDialog();
        condividiDialog.showDialog(getActivity(), this, lista);
        mListener.OnCondividiLista(lista);
    }

    @Override
    public void OnRequestCondividiLista(Lista lista, Utente user) {
        Dati.condividiLista(lista, user, this);
        mListener.OnRequestCondividiLista(lista, user);
    }

    @Override
    public void OnListaEliminata(Lista lista) {
        aggiornaLista();
        mListener.OnListaEliminata(lista);
    }

    @Override
    public void OnListaNonEliminata(Lista lista, String errore) {
        mListener.OnListaNonEliminata(lista, errore);
    }

    public void aggiornaLista() {
        listeMie = Dati.getListeMie();
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

    @Override
    public void OnListaCondivisa(Lista lista, Utente utente) {
        mListener.OnListaCondivisa(lista, utente);
    }

    @Override
    public void OnListaNonCondivisa(Lista lista, Utente utente, String errore) {
        mListener.OnListaNonCondivisa(lista, utente, errore);
    }

    public interface OnListeMieFragmentInteractionListener {
        void OnEliminaLista(Lista lista);

        void OnCondividiLista(Lista lista);

        void OnListaClicked(int listID);

        void OnAggiungiLista();

        void OnRequestCondividiLista(Lista lista, Utente utente);

        void OnListaLongClicked(Lista lista);

        void OnListaCondivisa(Lista lista, Utente utente);

        void OnListaNonCondivisa(Lista lista, Utente utente, String errore);

        void OnListaEliminata(Lista lista);

        void OnListaNonEliminata(Lista lista, String errore);
    }
}
