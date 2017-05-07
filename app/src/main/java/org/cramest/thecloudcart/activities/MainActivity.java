package org.cramest.thecloudcart.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.fragments.AggiungiListaFragment;
import org.cramest.thecloudcart.fragments.AggiungiProdottoFragment;
import org.cramest.thecloudcart.fragments.ListsFragment;
import org.cramest.thecloudcart.fragments.LoadingFragment;
import org.cramest.thecloudcart.fragments.NavigationDrawerFragment;
import org.cramest.thecloudcart.fragments.ProdottiFragment;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,ListsFragment.OnListFragmentInteractionListener,ProdottiFragment.OnProdottiFragmentInteractionListener,Dati.OnDatiLoadedListener, AggiungiListaFragment.OnAggiungiListaListener,AggiungiProdottoFragment.OnProdottoAggiuntoListener{

    private String username;
    private String userID;

    private ListsFragment listFragment;
    private LoadingFragment loadingFragment;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

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

    private void mostraFragmentProdotti(int listID){
        System.out.println("Genero il fragment dei prodotti");
        ProdottiFragment prodottiFragment = ProdottiFragment.newInstance(listID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, prodottiFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void mostraFragmentAggiungiLista(){
        AggiungiListaFragment aggiungiListaFragment = AggiungiListaFragment.newInstance(userID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, aggiungiListaFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void mostraFragmentAggiungiProdotto(int listID){
        AggiungiProdottoFragment aggiungiProdottoFragment = AggiungiProdottoFragment.newInstance(userID,listID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, aggiungiProdottoFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        /*FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();*/
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void OnListaClicked(int listID) {
        //Questa funzione viene chiamata dal fragment della lista quando viene cliccato qualcosa
        mostraFragmentProdotti(listID);
    }

    @Override
    public void OnAggiungiLista() {
        //Viene chiamata quando si preme il pulsante "Aggiungi lista"
        mostraFragmentAggiungiLista();
    }

    @Override
    public void OnProdottoClicked() {
        //Questa funzione viene chiamata invece dal fragment dei prodotti quando uno della lista viene cliccato
    }

    private void InizializzaApplicazione(){
        System.out.println("ListsFragment - Recupero le categorie e i prodotti");
        //Con una nuova istanza di dati li scarichiamo tutti per essere accessibili via static
        Dati dati = new Dati(this,userID);
    }

    @Override
    public void OnDatiLoaded() {
        //Quando la prima volta vengono caricati i dati viene mostrata questa finestra
        mostraFragmentListe();
    }

    @Override
    public void onAggiungiLista() {
        //Nel caso venga confermata l'aggiunta della lista
    }

    @Override
    public void OnAggiungiProdotto(int listID) {
        //Questa funzione viene chiamata quando viene cliccato il pulsante "aggiungi prodotto";
        mostraFragmentAggiungiProdotto(listID);
    }

    @Override
    public void OnProdottoAggiunto(ProdottoInLista prodotto) {
        //Nel caso venga confemata l'aggiunta di un nuovo podotto ad una lista
        finish();
    }
}
