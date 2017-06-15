package org.cramest.thecloudcart.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Categoria;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.LoadingOverlayHandler;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.classi.Utente;
import org.cramest.thecloudcart.dialogs.CondividiDialog;
import org.cramest.thecloudcart.dialogs.ListaDialog;
import org.cramest.thecloudcart.dialogs.ProdottoDialog;
import org.cramest.thecloudcart.fragments.AggiungiListaFragment;
import org.cramest.thecloudcart.fragments.AggiungiProdottoFragment;
import org.cramest.thecloudcart.fragments.CreaProdottoFragment;
import org.cramest.thecloudcart.fragments.ImpostazioniFragment;
import org.cramest.thecloudcart.fragments.InformazioniFragment;
import org.cramest.thecloudcart.fragments.ListeCondiviseFragment;
import org.cramest.thecloudcart.fragments.ListeMieFragment;
import org.cramest.thecloudcart.fragments.ListsFragment;
import org.cramest.thecloudcart.fragments.LoadingFragment;
import org.cramest.thecloudcart.fragments.NavigationDrawerFragment;
import org.cramest.thecloudcart.fragments.ProdottiFragment;
import org.cramest.utils.DataSaver;

public class MainActivity extends AppCompatActivity
implements NavigationDrawerFragment.NavigationDrawerCallbacks,ListsFragment.OnListFragmentInteractionListener,
        ProdottiFragment.OnProdottiFragmentInteractionListener, Dati.OnDatiListener, AggiungiListaFragment.OnAggiungiListaFragmentInteractionListener,
        AggiungiProdottoFragment.OnAggiungiProdottiListener,CreaProdottoFragment.OnCreaProdottiListener,
        ProdottoDialog.OnProdottoDialogInteractionListener, ListaDialog.OnListaDialogInteractionListener, CondividiDialog.OnCondividiDialogInteractionListener, ListeMieFragment.OnListeMieFragmentInteractionListener,
        ListeCondiviseFragment.OnListeCondiviseFragmentInteractionListener, ImpostazioniFragment.OnImpostazioniFragmentListener {

    private String username;
    private String nameUser;
    private String userID;

    private ListsFragment listFragment;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle = "The Cloudcart";

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
        nameUser = getIntent().getExtras().getString("nameUser");
        ((TextView)mNavigationDrawerFragment.getActivity().findViewById(R.id.text_view_quantita)).setText(username);
        restoreActionBar();
        InizializzaApplicazione();
        mostraFragmentSenzaBackStack(LoadingFragment.newInstance(), "LoadingFragment");
    }

    private void mostraFragmentConBackStack(Fragment fragment,String fragmentName){
        View fragmentView = findViewById(R.id.fragment_container);
        if (fragmentView != null) {
            FragmentManager fm = getFragmentManager();

            //SE non sto mostrando lo stesso fragment
            if (fm.findFragmentById(fragmentView.getId()) != null) {
                if (fragment.equals(fm.findFragmentById(fragmentView.getId()).getTag())) {
                    return;
                }
            }
            FragmentTransaction transaction = fm.beginTransaction().addToBackStack(fragmentName);
            transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }else{
            System.out.println("MainActivity - Manca il fragment container");
        }
    }

    private void mostraFragmentSenzaBackStack(Fragment fragment, String fragmentName) {
        View fragmentView = findViewById(R.id.fragment_container);
        if (findViewById(R.id.fragment_container) != null) {
            FragmentManager fm = getFragmentManager();
            //SE non sto mostrando lo stesso fragment
            if (fm.findFragmentById(fragmentView.getId()) != null) {
                if (fragment.equals(fm.findFragmentById(fragmentView.getId()).getTag())) {

                    return;
                }
            }
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
            transaction.replace(R.id.fragment_container, fragment, fragmentName);
            transaction.commit();
        }else{
            System.out.println("MainActivity - Manca il fragment container");
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        System.out.println("MainActivity - Chiamato drawerselected - " + position);
        switch (position) {
            case 0:
                //Le mie liste
                mostraFragmentSenzaBackStack(ListsFragment.newInstance(userID), "ListsFragment");
                break;
            /*case 1:
                //I miei prodotti
                break;*/
            case 1:
                //Impostazioni
                mostraFragmentConBackStack(ImpostazioniFragment.newInstance(userID, nameUser), "ImpostazioniFragment");
                break;
            case 2:
                //Disconnetti
                DataSaver.getInstance().clearData(this);
                Intent i = new Intent(getApplicationContext(), LandActivity.class);
                startActivity(i);
                finish();
                break;
            case 3:
                //Info su The CloudCart
                mostraFragmentConBackStack(InformazioniFragment.newInstance(),"InformazioniFragment");
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
        actionBar.setTitle("The Cloudcart");
    }

    @Override
    public void OnListaClicked(int listID) {
        System.out.println("MainActivity - Premuta la lista con id: " + listID);
        //Questa funzione viene chiamata dal fragment della lista quando viene cliccato qualcosa
        mostraFragmentConBackStack(ProdottiFragment.newInstance(listID),"ProdottiFragment");
    }

    @Override
    public void OnAggiungiLista() {
        //Viene chiamata quando si preme il pulsante "Aggiungi lista"
        System.out.println("MainActivity - Premuto il bottone 'aggiungi lista'");
        mostraFragmentConBackStack(AggiungiListaFragment.newInstance(userID),"AggiungiListaFragment");
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
        mostraFragmentSenzaBackStack(ListsFragment.newInstance(userID), "ListsFragment");
    }

    @Override
    public void OnProdottoInListaEliminato(int listID) {
        //Quando un prodotto qualsiasi viene eliminato (non comprato) apparirà qua
        System.out.println("MainActivity - Eliminato prodotto in lista("+listID+")");
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentConBackStack(ProdottiFragment.newInstance(listID),"ProdottiFragment");
    }

    @Override
    public void OnProdottoComprato(int listID) {
        //Quando un prodotto viene comprato e torna la risposta positiva dalla pagina web
        System.out.println("MainActivity - Prodotto comprato");
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentConBackStack(ProdottiFragment.newInstance(listID),"ProdottiFragment");
    }

    @Override
    public void OnListaEliminata() {
        //Quando torna la risposta dal server che la lista è stata eliminata
        System.out.println("MainActivity - Lista eliminata con successo");
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentSenzaBackStack(ListsFragment.newInstance(userID), "ListsFragment");
    }

    @Override
    public void OnProdottoInListaCreato(ProdottoInLista prodottoInLista) {
        //Un prodotto viene aggiunto ad una lista, torniamo alla visualizzazione della lista
        System.out.println("MainActivity - Nuovo prodotto creato");
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentSenzaBackStack(ProdottiFragment.newInstance(prodottoInLista.getIdLista()), "ProdottiFragment");
    }

    @Override
    public void OnListaCondivisa(Lista lista,Utente utente) {
        System.out.println("MainActivity - Lista " + lista.getNome() + " condivisa con " + utente.getUsername());
        LoadingOverlayHandler.nascondiLoading(this);
    }

    @Override
    public void OnListaNonCondivisa() {
        //Nel caso fallisca la condivisione di una lista
        LoadingOverlayHandler.nascondiLoading(this);
    }

    @Override
    public void OnAggiungiProdotto(int listID) {
        //Questa funzione viene chiamata quando viene cliccato il pulsante "aggiungi prodotto";
        System.out.println("MainActivity - Premuto bottone 'aggiungi prodotto'");
        mostraFragmentConBackStack(AggiungiProdottoFragment.newInstance(userID,listID),"AggiungiProdottoFragment");
    }

    @Override
    public void OnProdottoAggiunto(ProdottoInLista prodotto) {
        //Nel caso venga confemata l'aggiunta di un nuovo prodotto ad una lista
        System.out.println("MainActivity - Prodotto in lista aggiunto alla lista id:"+prodotto.getIdLista());
        Dati.aggiungiProdottoInLista(prodotto);
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentConBackStack(ProdottiFragment.newInstance(prodotto.getIdLista()),"ProdottiFragment");
    }

    @Override
    public void OnProdottoNonAggiunto(ProdottoInLista prodottoInLista) {
        //Nel caso venga confermata la NON aggiunta del prodotto
        System.out.println("MainActivity - Prodotto in lista NON aggiunto alla lista id:" + prodottoInLista.getIdLista());
        LoadingOverlayHandler.nascondiLoading(this);
        mostraFragmentConBackStack(ProdottiFragment.newInstance(prodottoInLista.getIdLista()), "ProdottiFragment");
    }

    @Override
    public void OnDevoCreareNuovoProdotto(int listID) {
        //Quando durante la creazione di un prodotto viene premuto il tasto "crea prodotto" questa funzione viene chiamata
        System.out.println("MainActivity - Premuto bottone 'crea prodotto'");
        mostraFragmentConBackStack(CreaProdottoFragment.newInstance(userID,listID),"CreaProdotto");
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
        System.out.println("MainActivity - Lista "+lista.getNome()+" creata con successo");
        LoadingOverlayHandler.nascondiLoading(this);
        Dati.aggiungiLista(lista);
        mostraFragmentSenzaBackStack(ListsFragment.newInstance(userID), "ListsFragment");
    }

    @Override
    public void onListaNonAggiunta() {
        //Nel caso non venga aggiunta l'unica soluzione è deprimersi
        System.out.println("MainActivity - Lista non creata");
        LoadingOverlayHandler.nascondiLoading(this);
        //TODO : Gestire il caso della lista non aggiunta
    }


    @Override
    public void OnEliminaLista(int listID) {
        //Quando viene premuto il tasto "elimina lista" dentro il dialog
        System.out.println("MainActivity - Elimino la lista " + listID);
        Dati.eliminaLista(listID,userID);
        LoadingOverlayHandler.mostraLoading(this);
    }

    @Override
    public void OnCondividiLista(Lista lista) {
        //Quando nel dialog viene premuto "condividi lista"
        CondividiDialog condividiDialog = new CondividiDialog();
        condividiDialog.showDialog(this,this, lista);
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

    @Override
    public void OnDevoCreareProdotto(String nome, Double prezzo, String marca, String dimensione, Categoria categoria, int quantita, String descrizione,int idLista) {
        //Quando nella schermata crea prodotto viene premuto il pulsante 'Crea prodotto'
        LoadingOverlayHandler.mostraLoading(this);
        Dati.creaProdottoEAggiungiloALista(userID,nome,prezzo,marca,dimensione,categoria,quantita,descrizione,idLista);
    }

    @Override
    public void OnRequestCondividiLista(Lista lista, Utente utente) {
        //Quando viene premuto un utente nella lista utenti sulla condivisione
        System.out.println("MainActivity - Lista " + lista.getNome() + " da condividere");
        Dati.condividiLista(lista,utente);
        LoadingOverlayHandler.mostraLoading(this);
    }

    @Override
    public void OnRichiestoCambioNome(String nome) {
        LoadingOverlayHandler.mostraLoading(this);
    }

    @Override
    public void OnCambioNomeEseguito(String nome) {
        LoadingOverlayHandler.nascondiLoading(this);
        nameUser = nome;
    }
}
