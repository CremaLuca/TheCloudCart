package org.cramest.thecloudcart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.LoadingOverlayHandler;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.dialogs.ListaDialog;
import org.cramest.thecloudcart.dialogs.ProdottoDialog;
import org.cramest.thecloudcart.fragments.AggiungiListaFragment;
import org.cramest.thecloudcart.fragments.AggiungiProdottoFragment;
import org.cramest.thecloudcart.fragments.CreaProdottoFragment;
import org.cramest.thecloudcart.fragments.ListsFragment;
import org.cramest.thecloudcart.fragments.LoadingFragment;
import org.cramest.thecloudcart.fragments.NavigationDrawerFragment;
import org.cramest.thecloudcart.fragments.ProdottiFragment;
import org.cramest.utils.DataSaver;

public class MainActivity extends AppCompatActivity
implements NavigationDrawerFragment.NavigationDrawerCallbacks,ListsFragment.OnListFragmentInteractionListener,
        ProdottiFragment.OnProdottiFragmentInteractionListener,Dati.OnDatiListener, AggiungiListaFragment.OnAggiungiListaListener,
        AggiungiProdottoFragment.OnAggiungiProdottiListener,CreaProdottoFragment.OnCreaProdottiListener,
        ProdottoDialog.OnProdottoDialogInteractionListener, ListaDialog.OnListaDialogInteractionListener {

    private String username;
    private String userID;

    private ListsFragment listFragment;
    private LoadingFragment loadingFragment;

    private LoadingOverlayHandler loadingOverlayHandler;

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
        ((TextView)mNavigationDrawerFragment.getActivity().findViewById(R.id.text_view_quantita)).setText(username);
        restoreActionBar();
        InizializzaApplicazione();
        mostraFragmentLoading();
    }

    private void mostraFragmentLoading(){
        System.out.println("MainActivity - Mostro loading fragment");
        loadingFragment = LoadingFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loadingFragment);

        transaction.commit();
    }

    private void mostraFragmentListe(){
        if (findViewById(R.id.fragment_container) != null) {
            listFragment = ListsFragment.newInstance(userID);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, listFragment);

            transaction.commit();
        }
    }

    private void mostraFragmentProdotti(int listID){
        System.out.println("Genero il fragment dei prodotti");
        ProdottiFragment prodottiFragment = ProdottiFragment.newInstance(listID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, prodottiFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void mostraFragmentAggiungiLista(){
        AggiungiListaFragment aggiungiListaFragment = AggiungiListaFragment.newInstance(userID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, aggiungiListaFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void mostraFragmentAggiungiProdotto(int listID){
        AggiungiProdottoFragment aggiungiProdottoFragment = AggiungiProdottoFragment.newInstance(userID,listID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, aggiungiProdottoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void mostraFragmentAggiungiProdotto(int listID,int prodottoID){
        AggiungiProdottoFragment aggiungiProdottoFragment = AggiungiProdottoFragment.newInstance(userID,listID,prodottoID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, aggiungiProdottoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void mostraFragmentCreaProdotto(int listID){
        CreaProdottoFragment creaProdottoFragment = CreaProdottoFragment.newInstance(userID,listID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, creaProdottoFragment);
        transaction.addToBackStack(null);
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
                //I miei prodotti
                break;
            case 2:
                //Impostazioni
                break;
            case 3:
                //Disconnetti
                DataSaver.getInstance().clearData(this);
                Intent i = new Intent(getApplicationContext(), LandActivity.class);
                startActivity(i);
                finish();
                break;
            case 4:
                //Informazioni
                break;
        }
    }

    @Override
    public void onNavigationHeaderClicked() {
        //Apriremo la schermata delle impostazioni credo
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
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
    public void OnListaLongClicked(Lista lista) {
        //Quando teniamo premuto su una lista
        ListaDialog listaDialog = new ListaDialog();
        listaDialog.showDialog(this, this, lista,userID);
    }

    @Override
    public void OnProdottoClicked(ProdottoInLista prodottoInLista) {
        //Questa funzione viene chiamata invece dal fragment dei prodotti quando uno della lista viene cliccato
        ProdottoDialog prodottoDialog = new ProdottoDialog();
        prodottoDialog.showDialog(this,this,prodottoInLista);
    }

    private void InizializzaApplicazione(){
        //Con una nuova istanza di dati li scarichiamo tutti per essere accessibili via static
        System.out.println("MainActivity - Recupero tutti i dati dell'utente");
        new Dati(this, userID);
    }

    @Override
    public void OnDatiLoaded() {
        //Quando la prima volta vengono caricati i dati viene mostrata questa finestra
        System.out.println("MainActivity - Dati caricati, mostro il fragment liste");
        mostraFragmentListe();
    }

    @Override
    public void OnProdottoInListaEliminato(int listID) {
        //Quando un prodotto qualsiasi viene eliminato (non comprato) apparirà qua
        System.out.println("MainActivity - Eliminato prodotto in lista("+listID+")");
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentProdotti(listID);
    }

    @Override
    public void OnProdottoComprato(int listID) {
        //Quando un prodotto viene comprato e torna la risposta positiva dalla pagina web
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentProdotti(listID);
    }

    @Override
    public void OnListaEliminata() {
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentListe();
    }

    @Override
    public void OnAggiungiProdotto(int listID) {
        //Questa funzione viene chiamata quando viene cliccato il pulsante "aggiungi prodotto";
        mostraFragmentAggiungiProdotto(listID);
    }

    @Override
    public void OnProdottoAggiunto(ProdottoInLista prodotto) {
        //Nel caso venga confemata l'aggiunta di un nuovo prodotto ad una lista
        LoadingOverlayHandler.nascondiLoading(this);
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
        LoadingOverlayHandler.mostraLoading(this);
        Dati.instance.compraProdotto(userID,prodotto);
    }

    @Override
    public void OnEliminaProdotto(ProdottoInLista prodotto) {
        //Se nel dialog si vuole eliminare il prodotto in lista
        LoadingOverlayHandler.mostraLoading(this);
        Dati.instance.rimuoviProdottoInLista(prodotto);
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


    @Override
    public void OnCondividiLista(Lista lista) {
        //TODO : Mostra fragment o dialog condividi lista
    }
}
