package org.cramest.thecloudcart.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.*;
import org.cramest.thecloudcart.dialogs.*;
import org.cramest.thecloudcart.fragments.*;
import org.cramest.utils.DataSaver;

public class MainActivity extends AppCompatActivity
implements NavigationDrawerFragment.NavigationDrawerCallbacks,ListsFragment.OnListFragmentInteractionListener,
        ProdottiFragment.OnProdottiFragmentInteractionListener,Dati.OnDatiListener, AggiungiListaFragment.OnAggiungiListaListener,
        AggiungiProdottoFragment.OnAggiungiProdottiListener,CreaProdottoFragment.OnCreaProdottiListener,
        ProdottoDialog.OnProdottoDialogInteractionListener, ListaDialog.OnListaDialogInteractionListener {

    private String username;
    private String userID;

    private ListsFragment listFragment;

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
        mostraFragmentSenzaBackStack(LoadingFragment.newInstance());
    }

    private void mostraFragmentConBackStack(Fragment fragment){
        if (findViewById(R.id.fragment_container) != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction().addToBackStack(null);
            transaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right,R.animator.slide_in_right,R.animator.slide_out_left);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }else{
            System.out.println("Manca il fragment container");
        }
    }

    private void mostraFragmentSenzaBackStack(Fragment fragment){
        if (findViewById(R.id.fragment_container) != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right,R.animator.slide_in_right,R.animator.slide_out_left);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }else{
            System.out.println("Manca il fragment container");
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        System.out.println("MainActibity - Chiamato drawerselected - "+position);
        switch (position) {
            case 0:
                //Le mie liste
                mostraFragmentSenzaBackStack(ListsFragment.newInstance(userID));
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
        mostraFragmentConBackStack(ProdottiFragment.newInstance(listID));
    }

    @Override
    public void OnAggiungiLista() {
        //Viene chiamata quando si preme il pulsante "Aggiungi lista"
        System.out.println("MainActivity - Premuto il bottone 'aggiungi lista'");
        mostraFragmentSenzaBackStack(AggiungiListaFragment.newInstance(userID));
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
        mostraFragmentSenzaBackStack(ListsFragment.newInstance(userID));
    }

    @Override
    public void OnProdottoInListaEliminato(int listID) {
        //Quando un prodotto qualsiasi viene eliminato (non comprato) apparirà qua
        System.out.println("MainActivity - Eliminato prodotto in lista("+listID+")");
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentConBackStack(ProdottiFragment.newInstance(listID));
    }

    @Override
    public void OnProdottoComprato(int listID) {
        //Quando un prodotto viene comprato e torna la risposta positiva dalla pagina web
        System.out.println("MainActivity - Prodotto comprato");
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentConBackStack(ProdottiFragment.newInstance(listID));
    }

    @Override
    public void OnListaEliminata() {
        //Quando torna la risposta dal server che la lista è stata eliminata
        System.out.println("MainActivity - Lista eliminata con successo");
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentSenzaBackStack(ListsFragment.newInstance(userID));
    }

    @Override
    public void OnAggiungiProdotto(int listID) {
        //Questa funzione viene chiamata quando viene cliccato il pulsante "aggiungi prodotto";
        System.out.println("MainActivity - Premuto bottone 'aggiungi prodotto'");
        mostraFragmentConBackStack(AggiungiProdottoFragment.newInstance(userID,listID));
    }

    @Override
    public void OnProdottoAggiunto(ProdottoInLista prodotto) {
        //Nel caso venga confemata l'aggiunta di un nuovo prodotto ad una lista
        System.out.println("MainActivity - Prodotto in lista aggiunto alla lista id:"+prodotto.getIdLista());
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentConBackStack(ProdottiFragment.newInstance(prodotto.getIdLista()));
    }

    @Override
    public void OnDevoCreareNuovoProdotto(int listID) {
        //Quando durante la creazione di un prodotto viene premuto il tasto "crea prodotto" questa funzione viene chiamata
        System.out.println("MainActivity - Premuto bottone 'crea prodotto'");
        mostraFragmentSenzaBackStack(CreaProdottoFragment.newInstance(userID,listID));
    }

    @Override
    public void OnProdottoCreato(int listID, int prodottoID) {
        //Un prodotto viene creato dal nulla e si torna alla pagina aggiungi prodotto con il prodotto appena creato
        System.out.println("MainActivity - Nuovo prodotto creato");
        mostraFragmentSenzaBackStack(AggiungiProdottoFragment.newInstance(userID,listID,prodottoID));
    }

    @Override
    public void OnProdottoNonCreato(int listID) {
        System.out.println("MainActivity - Prodotto non creato");
        //TODO : Gestiore il caso un prodotto non venga creato
    }

    @Override
    public void OnCompraProdotto(ProdottoInLista prodotto) {
        //Se nel dialog il prodotto viene comprato
        System.out.println("MainActivity - Premuto il bottone 'compra prodotto'");
        LoadingOverlayHandler.mostraLoading(this);
        Dati.instance.compraProdotto(userID,prodotto);
    }

    @Override
    public void OnEliminaProdotto(ProdottoInLista prodotto) {
        //Se nel dialog si vuole eliminare il prodotto in lista
        System.out.println("MainActivity - Prodotto in lista da eliminare");
        LoadingOverlayHandler.mostraLoading(this);
        Dati.instance.rimuoviProdottoInLista(prodotto);
    }

    @Override
    public void onListaAggiunta(Lista lista) {
        //Nel caso venga confermata la creazione di una nuova lista
        System.out.println("MainActivity - Lista creata con successo");
        Dati.aggiungiLista(lista);
        mostraFragmentSenzaBackStack(ListsFragment.newInstance(userID));
    }

    @Override
    public void onListaNonAggiunta() {
        //Nel caso non venga aggiunta l'unica soluzione è deprimersi
        System.out.println("MainActivity - Lista non creata");
        //TODO : Gestire il caso della lista non aggiunta
    }


    @Override
    public void OnCondividiLista(Lista lista) {
        //TODO : Mostra fragment o dialog condividi lista
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
