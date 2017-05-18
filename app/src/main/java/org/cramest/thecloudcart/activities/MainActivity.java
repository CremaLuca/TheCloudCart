package org.cramest.thecloudcart.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.Prodotto;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.dialogs.ProdottoDialog;
import org.cramest.thecloudcart.fragments.AggiungiListaFragment;
import org.cramest.thecloudcart.fragments.AggiungiProdottoFragment;
import org.cramest.thecloudcart.fragments.CreaProdottoFragment;
import org.cramest.thecloudcart.fragments.ListsFragment;
import org.cramest.thecloudcart.fragments.LoadingFragment;
import org.cramest.thecloudcart.fragments.NavigationDrawerFragment;
import org.cramest.thecloudcart.fragments.ProdottiFragment;
import org.cramest.utils.DataSaver;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,ListsFragment.OnListFragmentInteractionListener,
        ProdottiFragment.OnProdottiFragmentInteractionListener,Dati.OnDatiListener, AggiungiListaFragment.OnAggiungiListaListener,
        AggiungiProdottoFragment.OnAggiungiProdottiListener,CreaProdottoFragment.OnCreaProdottiListener,
        ProdottoDialog.OnProdottoDialogInteractionListener{

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
    private CharSequence mTitle = "TheCloudCart";

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
        ((TextView)mNavigationDrawerFragment.getActivity().findViewById(R.id.text_view_username)).setText(username);
        restoreActionBar();
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
    private void mostraFragmentAggiungiProdotto(int listID,int prodottoID){
        AggiungiProdottoFragment aggiungiProdottoFragment = AggiungiProdottoFragment.newInstance(userID,listID,prodottoID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, aggiungiProdottoFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void mostraFragmentCreaProdotto(int listID){
        CreaProdottoFragment creaProdottoFragment = CreaProdottoFragment.newInstance(userID,listID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, creaProdottoFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        System.out.println("Chiamato drawerselected ---- "+position);
        switch (position) {
            case 0:
                //Le mie liste
                mostraFragmentListe();
                break;
            case 1:
                //Impostazioni
                break;
            case 2:
                //Disconnetti
                DataSaver.getInstance().clearData(this);
                Intent i = new Intent(getApplicationContext(), LandActivity.class);
                startActivity(i);
                finish();
                break;
            case 3:
                //Informazioni
                break;
        }
    }

    @Override
    public void onNavigationHeaderClicked() {
        //Apriremo la schermata delle impostazioni credo
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("TheCloudCart");
    }

    @Override
    public void OnListaClicked(int listID) {
        System.out.println("Premuta la lista con id: " + listID);
        //Questa funzione viene chiamata dal fragment della lista quando viene cliccato qualcosa
        mostraFragmentProdotti(listID);
    }

    @Override
    public void OnAggiungiLista() {
        //Viene chiamata quando si preme il pulsante "Aggiungi lista"
        mostraFragmentAggiungiLista();
    }

    @Override
    public void OnProdottoClicked(ProdottoInLista prodottoInLista) {
        //Questa funzione viene chiamata invece dal fragment dei prodotti quando uno della lista viene cliccato
        ProdottoDialog prodottoDialog = new ProdottoDialog();
        prodottoDialog.showDialog(this,this,prodottoInLista);
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
    public void OnProdottoInListaEliminato(int listID) {
        //Quando un prodotto qualsiasi viene eliminato (non comprato) apparirà qua
        mostraFragmentProdotti(listID);
    }

    @Override
    public void OnProdottoComprato(int listID) {
        //Quando un prodotto viene comprato e torna la risposta positiva dalla pagina web
        mostraFragmentProdotti(listID);
    }

    @Override
    public void OnAggiungiProdotto(int listID) {
        //Questa funzione viene chiamata quando viene cliccato il pulsante "aggiungi prodotto";
        mostraFragmentAggiungiProdotto(listID);
    }

    @Override
    public void OnProdottoAggiunto(ProdottoInLista prodotto) {
        //Nel caso venga confemata l'aggiunta di un nuovo prodotto ad una lista
        mostraFragmentProdotti(prodotto.getIdLista());
    }

    @Override
    public void OnDevoCreareNuovoProdotto(int listID) {
        //Quando durante la creazione di un prodotto viene premuto il tasto "crea prodotto" questa funzione viene chiamata
        mostraFragmentCreaProdotto(listID);
    }

    @Override
    public void OnProdottoCreato(int listID, int prodottoID) {
        //Un prodotto viene creato dal nulla e si torna alla pagina aggiungi prodotto con il prodotto appena creato
        mostraFragmentAggiungiProdotto(listID,prodottoID);
    }

    @Override
    public void OnProdottoNonCreato(int listID) {
        //TODO : Gestiore il caso un prodotto non venga creato
    }

    @Override
    public void OnCompraProdotto(ProdottoInLista prodotto) {
        //Se nel dialog il prodotto viene comprato
        Dati.instance.compraProdotto(userID,prodotto);
    }

    @Override
    public void OnEliminaProdotto(ProdottoInLista prodotto) {
        //Se nel dialog si vuole eliminare il prodotto
        Dati.instance.rimuoviProdottoInLista(prodotto);
        mostraFragmentProdotti(prodotto.getIdLista());
    }

    @Override
    public void onListaAggiunta(Lista lista) {
        //Nel caso venga confermata la creazione di una nuova lista
        Dati.aggiungiLista(lista);
        mostraFragmentListe();
    }

    @Override
    public void onListaNonAggiunta() {
        //Nel caso non venga aggiunta l'unica soluzione è deprimersi
        //TODO : Gestire il caso della lista non aggiunta
    }
}
