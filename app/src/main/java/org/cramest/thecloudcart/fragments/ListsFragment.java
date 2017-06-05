package org.cramest.thecloudcart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
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
            listeMie = new ArrayList<Lista>(Dati.getListeMie());
            listeCondivise = Dati.getListeCondivise();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //Setup delle listview
        setAdapter((ListView)getView().findViewById(R.id.listViewMie),listeMie);
        aggiungiBottoneCreaLista((ListView)getView().findViewById(R.id.listViewMie));
        setAdapter((ListView)getView().findViewById(R.id.listViewCondivise),listeCondivise);
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
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        return view;
    }

    public void apriLista(int listID) {
        if (mListener != null) {
            mListener.OnListaClicked(listID);
        }
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

        void OnListaLongClicked(Lista lista);
    }

    private void setAdapter(final ListView lv, final ArrayList<Lista> liste){
        ListaAdapter listViewadapter = new ListaAdapter(getActivity(), R.layout.adapter_lista_prodotti, liste);
        lv.setAdapter(listViewadapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idLista = liste.get(position).getID();
                if(idLista > 0) {
                    apriLista(idLista);
                }else{
                    Toast.makeText(getActivity(), "Errore, lista con id errato : "+idLista, Toast.LENGTH_SHORT).show();
                }
                lv.setItemChecked(-1,true);
                lv.setSelection(-1);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(mListener != null) {
                    onListaLongClicked(liste.get(position));
                }
                lv.setItemChecked(-1,true);
                lv.setSelection(-1);
                return true;
            }
        });

    }
}
