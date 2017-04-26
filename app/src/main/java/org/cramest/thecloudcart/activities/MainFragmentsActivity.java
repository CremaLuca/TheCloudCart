package org.cramest.thecloudcart.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.fragments.ListsFragment;
import org.cramest.thecloudcart.fragments.LoadingFragment;
import org.cramest.thecloudcart.fragments.ProdottiFragment;

public class MainFragmentsActivity extends FragmentActivity implements ListsFragment.OnListFragmentInteractionListener,ProdottiFragment.OnProdottiFragmentInteractionListener,Dati.OnDatiLoadedListener{

    private String username;
    private String userID;

    private ListsFragment listFragment;
    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_fragments);
        //recuperiamo nome utente e password dall'intent
        username = getIntent().getExtras().getString("username");
        userID = getIntent().getExtras().getString("userID");
        InizializzaApplicazione();
        mostraFragmentLoading();
    }

    private void mostraFragmentLoading(){
        System.out.println("Mostro loading fragment");
        loadingFragment = LoadingFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, loadingFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void mostraFragmentListe(){
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            if(listFragment == null) {
                System.out.println("Genero il fragment della lista");
                //Questo costruttore statico permette di passare i parametri direttamente
                listFragment = ListsFragment.newInstance(userID);
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, listFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void mostraFragmentProdotti(int listID,String listName){
        System.out.println("Genero il fragment dei prodotti");
        ProdottiFragment prodottiFragment = ProdottiFragment.newInstance(listID, listName);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back

        transaction.replace(R.id.fragment_container, prodottiFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void OnListFragmentInteractionListener(int listID/*,TODO : String listName*/) {
        //Questa funzione viene chiamata dal fragment della lista quando viene cliccato qualcosa
        mostraFragmentProdotti(listID,"Mario");
    }

    @Override
    public void OnProdottiFragmentInteraction() {
        //Questa funzione viene chiamata invece dal fragment dei prodotti quando uno della lista viene cliccato
    }

    private void InizializzaApplicazione(){
        System.out.println("ListsFragment - Recupero le categorie e i prodotti");
        Dati dati = new Dati(this,userID);
    }

    @Override
    public void OnDatiLoaded() {
        //Quando la prima volta vengono caricati i dati viene mostrata questa finestra
        mostraFragmentListe();
    }
}
