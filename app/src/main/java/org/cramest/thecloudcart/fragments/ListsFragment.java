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
import android.widget.TextView;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.activities.AggiungiListaActivity;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.adapter.ListaAdapter;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.WebsiteDataManager;

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
public class ListsFragment extends Fragment implements DataHandler {

    private static final String ARG_PARAM = "userID";

    private String username;
    private String userID;

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

    public void mainBeforeView(){
        //recuperiamo nome utente e password dai parametri
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM);
        }
        //Carichiamo la lista
        //CaricaListaMie();
        //CaricaListaCondivise();
    }

    public void mainAfterView(){
        if(Dati.getListeMie() == null) {
            setAdapter(R.id.listViewMie, new ArrayList<Lista>(Arrays.asList(new Lista(0, "Caricamento...", -1))));
        }else{
            setAdapter(R.id.listViewMie,Dati.getListeMie());
        }
        if(Dati.getListeCondivise() == null) {
            setAdapter(R.id.listViewCondivise, new ArrayList<Lista>(Arrays.asList(new Lista(0, "Caricamento...", -1))));
        }else{
            setAdapter(R.id.listViewCondivise,Dati.getListeCondivise());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Lo chiamo qui così viene chiamato una volta sola teoricamente
        mainBeforeView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mainAfterView();
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
            mListener.OnListFragmentInteractionListener(listID);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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

    public interface OnListFragmentInteractionListener {
        void OnListFragmentInteractionListener(int listID);
    }


    /** La richiesta delle liste della spesa dell'utente
     */
    private void CaricaListaMie(){
        System.out.println("ListsFragment - Carico le liste dell'utente " + username);
        //richiesta = "userlist" & user = username
        String[] parametri = {"req","userID"};
        String[] valori = {"getUserList",userID};
        if(Connettore.getInstance(getActivity()).isNetworkAvailable()) {
            //Chiediamo al sito le liste
            Connettore.getInstance(getActivity()).GetDataFromWebsite(this, "listeSpesaMie", parametri, valori);
        }else{
            //TODO : Carichiamo le liste dal locale
        }
    }

    private void CaricaListaCondivise(){
        System.out.println("ListsFragment - Carico le liste dell'utente " + username);
        //richiesta = "userlist" & user = username
        String[] parametri = {"req","userID"};
        System.out.println("Ho ricavato l'userID " + userID);
        String[] valori = {"getCondivisioniUtente",userID};
        if(Connettore.getInstance(getActivity()).isNetworkAvailable()) {
            //Chiediamo al sito le liste
            Connettore.getInstance(getActivity()).GetDataFromWebsite(this, "listeSpesaCondivise", parametri, valori);
        }else{
            //non carichiamo niente perchè tanto gli altri non possono visualizzare le modifiche, eliminiamo la scritta liste condivise
            ((TextView)getView().findViewById(R.id.textAltreListe)).setVisibility(View.GONE);
        }
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
                    Intent i = new Intent(getActivity(), AggiungiListaActivity.class);
                    i.putExtra("username", username);
                    i.putExtra("userID", userID);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void HandleData(String nome, boolean successo,String data){
        if(successo) {
            //Dato che facciamo solo due richieste ed entrambe tornano un arraylist di liste facciamo il recupero di entrambe fuori
            //Convertiamo i dati in liste
            ArrayList<Lista> liste = new ArrayList<>(Arrays.asList(WebsiteDataManager.getListeUtente(data)));
            if (nome.equals("listeSpesaMie")) {
                //listeMie = liste;
                //Aggiungamo il bottone crea nuova lista
                liste.add(new Lista(-1,"Crea nuova lista",-1));
                //Inseriamo nel ListView le liste
                //setAdapter(R.id.listViewMie,listeMie);
            }
            if(nome.equals("listeSpesaCondivise")){
                //listeCondivise = liste;
                //Inseriamo nel ListView le liste
                //setAdapter(R.id.listViewCondivise,listeCondivise);
            }
        }else{
            Toast.makeText(getActivity(), "Errore : " + data, Toast.LENGTH_SHORT).show();
        }
    }
}
