package org.cramest.thecloudcart.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.activities.AggiungiListaActivity;
import org.cramest.thecloudcart.activities.ListsActivity;
import org.cramest.thecloudcart.activities.ProdottiActivity;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.ListaAdapter;
import org.cramest.thecloudcart.classi.ListaCategorie;
import org.cramest.thecloudcart.classi.ListaProdotti;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.WebsiteDataManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListsFragment extends Fragment implements DataHandler{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "username";
    private static final String ARG_PARAM2 = "userID";

    private String username;
    private String userID;

    private OnFragmentInteractionListener mListener;

    public ListsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListsFragment newInstance(String param1, String param2) {
        ListsFragment fragment = new ListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void main(){
        //recuperiamo nome utente e password dai parametri
        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM1);
            userID = getArguments().getString(ARG_PARAM2);
        }
        //Inizializziamo categorie e prodotti dell'utente
        InizializzaApplicazione();
        //Carichiamo la lista
        CaricaListaMie();
        setAdapter(R.id.listViewMie,new ArrayList<Lista>(Arrays.asList(new Lista(0,"Caricamento...",-1))));
        CaricaListaCondivise();
        setAdapter(R.id.listViewCondivise,new ArrayList<Lista>(Arrays.asList(new Lista(0,"Caricamento...",-1))));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lists, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void InizializzaApplicazione(){
        System.out.println("ListsActivity - Recupero le categorie e i prodotti");
        //recuperiamo le categorie
        new ListaCategorie().recuperaCategorie(getActivity());
        //recuperiamo tutti i prodotti
        new ListaProdotti().recuperaProdotti(getActivity());
    }

    /** La richiesta delle liste della spesa dell'utente
     */
    private void CaricaListaMie(){
        System.out.println("ListsActivity - Carico le liste dell'utente " + username);
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
        System.out.println("ListsActivity - Carico le liste dell'utente " + username);
        //richiesta = "userlist" & user = username
        String[] parametri = {"req","userID"};
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
                int idProdotto = lista.get(position).getID();
                if(idProdotto != -1) {
                    //Nuovo intent per aprire la activity per visualizzare i prodotti in questa lista
                    Intent i = new Intent(getActivity(), ProdottiActivity.class);
                    i.putExtra("IDLista", idProdotto);
                    i.putExtra("username", username);
                    i.putExtra("userID", userID);
                    startActivity(i);
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
                //Aggiungamo il bottone crea nuova lista
                liste.add(new Lista(-1,"Crea nuova lista",-1));
                //Inseriamo nel ListView le liste
                setAdapter(R.id.listViewMie,liste);
            }
            if(nome.equals("listeSpesaCondivise")){
                //Inseriamo nel ListView le liste
                setAdapter(R.id.listViewCondivise,liste);
            }
        }else{
            Toast.makeText(getActivity(), "Errore : " + data, Toast.LENGTH_SHORT).show();
        }
    }
}
