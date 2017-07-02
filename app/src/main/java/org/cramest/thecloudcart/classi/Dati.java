package org.cramest.thecloudcart.classi;

import android.content.Context;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.WebsiteDataManager;

import java.util.ArrayList;
import java.util.Arrays;


public class Dati implements DataHandler{

    private static DatiLoadedListener datiLoadedListener;

    public static Dati instance;

    private static ArrayList<Lista> listeMie;
    private static ArrayList<Lista> listeCondivise;
    private static ArrayList<Categoria> categorie;
    private static ArrayList<Prodotto> prodotti;
    private static ArrayList<ProdottoInLista> prodottiInLista;
    private static ArrayList<ProdottoConsigliato> prodottiConsigliati = new ArrayList<>();
    private static ArrayList<Utente> utenti = new ArrayList<>();

    private static String userID;
    private static Context ctx;

    private boolean chiamatoLoaded = false;

    private int listeCaricate = 0;
    private int listeDaCaricare;

    public Dati(Context context, String userID) {
        System.out.println("Dati - User id : " + userID);
        //Ci salviamo il listener se esiste
        if (context instanceof DatiLoadedListener) {
            datiLoadedListener = (DatiLoadedListener) context;
        } else {
            throw new RuntimeException(ctx.toString()
                    + " must implement OnDatiListener");
        }
        this.userID = userID;
        this.ctx = context;
        instance = this;
        //Svuotiamo le liste nel caso contengano qualcosa
        svuotaListe();
        setupCategorie();
        //Iniziamo
        richiediProdotti(context, userID);
    }

    private void setupCategorie() {
        categorie = new ArrayList<>();
        categorie.add(new Categoria(1, ctx.getString(R.string.Category_1)));
        categorie.add(new Categoria(2, ctx.getString(R.string.Category_2)));
        categorie.add(new Categoria(3, ctx.getString(R.string.Category_3)));
        categorie.add(new Categoria(4, ctx.getString(R.string.Category_4)));
        categorie.add(new Categoria(5, ctx.getString(R.string.Category_5)));
        categorie.add(new Categoria(6, ctx.getString(R.string.Category_6)));
        categorie.add(new Categoria(7, ctx.getString(R.string.Category_7)));
        categorie.add(new Categoria(8, ctx.getString(R.string.Category_8)));
        categorie.add(new Categoria(9, ctx.getString(R.string.Category_9)));
        categorie.add(new Categoria(10, ctx.getString(R.string.Category_10)));
        categorie.add(new Categoria(11, ctx.getString(R.string.Category_11)));
        categorie.add(new Categoria(12, ctx.getString(R.string.Category_12)));
        categorie.add(new Categoria(13, ctx.getString(R.string.Category_13)));
    }

    private void svuotaListe(){
        listeMie = null;
        listeCondivise = null;
        prodotti = null;
        prodottiInLista = null;
        prodottiConsigliati = new ArrayList<>();
        utenti = new ArrayList<>();
    }

    private void richiediProdotti(Context ctx,String userID){
        //richiediamo i prodotti, tutti quelli nostri e anche quelli che non sono nostri ma sono nelle nostre liste, ma comunque non tutti tutti tutti di tutti
        String[] parametriMie = {"req","userID"};
        String[] valoriMie = {"getAllUserProdotti",userID};
        Connettore.getInstance(ctx).GetDataFromWebsite(this,"prodotti",parametriMie,valoriMie);
    }
    private void richiediListeSpesa(Context ctx,String userID){
        //richiediamo le nostre liste
        String[] parametriMie = {"req","userID"};
        String[] valoriMie = {"getUserList",userID};
        Connettore.getInstance(ctx).GetDataFromWebsite(this,"listeSpesa",parametriMie,valoriMie);
        //richiediamo le liste condivise
        String[] parametriCondivise = {"req","userID"};
        String[] valoriCondivise = {"getCondivisioniUtente",userID};
        Connettore.getInstance(ctx).GetDataFromWebsite(this, "listeSpesaCondivise", parametriCondivise, valoriCondivise);
    }

    private void richiediProdottiInLista(Context ctx){
        //Se abbiamo sia le nostre liste che le liste degli altri di cui caricare i prodotti
        if(listeMie != null && listeCondivise != null) {
            listeDaCaricare = listeMie.size() + listeCondivise.size();
            for (Lista lista : listeMie) {
                String[] pars = {"req", "listID"};
                String[] vals = {"getProductList", lista.getID() + ""};
                Connettore.getInstance(ctx).GetDataFromWebsite(this, "ProdottiListaID="+lista.getID(), pars, vals);
            }
            for (Lista lista : listeCondivise) {
                String[] pars = {"req", "listID"};
                String[] vals = {"getProductList", lista.getID() + ""};
                Connettore.getInstance(ctx).GetDataFromWebsite(this, "ProdottiListaID="+lista.getID(), pars, vals);
            }
        }
    }

    private void richiediUtenti(Context ctx){
        if(listeMie != null) {
            for (Lista lista : listeMie) {
                String[] pars = {"req", "listID"};
                String[] vals = {"getSharedUsers", lista.getID() + ""};
                Connettore.getInstance(ctx).GetDataFromWebsite(this, "UtentiLista="+lista.getID(), pars, vals);
            }
        }
    }

    private void richiediProdottiConsigliati(){
        String[] pars = {"req", "userID"};
        String[] vals = {"getShouldBuy", userID+""};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success){
                    if (data != null && !data.equals("")) {
                        prodottiConsigliati = new ArrayList<>(Arrays.asList(WebsiteDataManager.getProdottiConsigliati(data)));
                    }
                }
            }
        }, "GetShouldBuy", pars, vals);
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success){
            if(nome.equals("prodotti")){
                Prodotto[] arrayProdotti = WebsiteDataManager.getProdotti(data);
                if(arrayProdotti != null) {
                    prodotti = new ArrayList<>(Arrays.asList(arrayProdotti));
                }
                richiediListeSpesa(ctx,userID);
            }
            if(nome.equals("listeSpesa")){
                Lista[] arrayListeMie = WebsiteDataManager.getListeUtente(data);
                if(arrayListeMie != null) {
                    listeMie = new ArrayList<>(Arrays.asList(arrayListeMie));
                    richiediUtenti(ctx); //Richiediamo anche gli utenti visualizzatori
                    richiediProdottiInLista(ctx);
                }
            }
            if(nome.equals("listeSpesaCondivise")){
                listeCondivise = new ArrayList<>(Arrays.asList(WebsiteDataManager.getListeUtente(data)));
                richiediProdottiInLista(ctx);
                richiediProdottiConsigliati();
            }
            if(nome.startsWith("UtentiLista")){
                //Se almeno la lista è condivisa con qualcuno
                if (data != null && !data.equals("")) {
                    int listID = Integer.parseInt(nome.split("=")[1]);
                    ArrayList<Utente> visualizzanti = new ArrayList<>(Arrays.asList(WebsiteDataManager.getUtenti(data)));
                    salvaUtenti(visualizzanti); //Salviamoceli in una lista
                    if (getListaByID(listID) != null) {
                        getListaByID(listID).setVistaDa(visualizzanti);
                    }
                }
            }
            if(nome.startsWith("ProdottiLista")){
                //Recuperiamo l'id della lista dal nome della richiesta
                int listID = Integer.parseInt(nome.split("=")[1]);
                if(prodottiInLista == null){
                    prodottiInLista = new ArrayList<ProdottoInLista>();
                }
                if(data != null) {
                    ProdottoInLista[] arrayProdotti = WebsiteDataManager.getProdottiInLista(data, listID);
                    if(prodottiInLista != null) {
                        ArrayList<ProdottoInLista> nuoviProdotti = new ArrayList<>(Arrays.asList(arrayProdotti));
                        prodottiInLista.addAll(nuoviProdotti);
                    }else{
                        System.out.println("Dati - ERRORE : Mancanod dei prodotti quindi non sono riuscito ad associarli tutti");
                    }
                }
                listeCaricate++;
            }
            if(prodotti != null && categorie != null && listeMie != null && listeCondivise != null && prodottiInLista != null && listeCaricate >= listeDaCaricare){
                LoadedDati();
            }
        }else{
            if(nome.equals("prodotti")){
                System.out.println("Dati - Non ci sono prodotti");
                prodotti = new ArrayList<>();
                richiediListeSpesa(ctx,userID);
            }
            if(nome.equals("listeSpesa")){
                System.out.println("Dati - Non ci sono liste della spesa");
                listeMie = new ArrayList<>();
            }
            if(nome.equals("listeSpesaCondivise")){
                listeCondivise = new ArrayList<>();
                richiediProdottiInLista(ctx);
            }
            //System.out.println("Dati - Prodotti : " + prodotti + " listeMie : " +listeMie + " listeCondivise : " + listeCondivise + " prodottiInLista : " + prodottiInLista);
            if (prodotti != null && categorie != null && listeMie != null && listeCondivise != null) {
                if(prodottiInLista == null){
                    prodottiInLista = new ArrayList<>();
                }
                if(prodottiConsigliati == null){
                    prodottiConsigliati = new ArrayList<>();
                }
                LoadedDati();
            }
        }
    }

    public static Prodotto getProdottoByID(int ID){
        if(prodotti != null && prodotti.size() > 0) {
            //C# -> foreach (Prodotto prod in prodotti)
            for(Prodotto prod : prodotti) {
                if(prod.getID() == ID) {
                    return prod;
                }
            }
            return null;
        }
        return null;
    }
    public static Categoria getCategoriaByID(int ID){
        if(categorie != null && categorie.size() > 0) {
            //ID 1-13
            return categorie.get(ID-1);
        }
        return null;
    }

    public static Lista getListaByID(int ID) {
        if(listeMie != null) {
            for(Lista lista : listeMie) {
                if(lista.getID() == ID) {
                    return lista;
                }
            }
            for(Lista lista : listeCondivise) {
                if(lista.getID() == ID) {
                    return lista;
                }
            }
        }
        return null;
    }

    public static ArrayList<ProdottoInLista> getProdottoInListaByListID(int ID){
        if(prodottiInLista != null) {
            ArrayList<ProdottoInLista> prodottiDellaLista = new ArrayList<>();
            for(ProdottoInLista prodottoInLista : prodottiInLista) {
                if(prodottoInLista.getIdLista() == ID) {
                    prodottiDellaLista.add(prodottoInLista);
                }
            }
            return prodottiDellaLista;
        }
        return null;
    }

    public static ArrayList<Lista> getListeMie() {
        if(listeMie != null) {
            return listeMie;
        }
        return new ArrayList<>();
    }

    public static ArrayList<Lista> getListeCondivise() {
        if(listeCondivise != null) {
            return listeCondivise;
        }
        return null;
    }

    public static ArrayList<Utente> getUtentiAmici(){
        return utenti;
    }

    private void LoadedDati() {
        if (datiLoadedListener != null) {
            if(!chiamatoLoaded) {
                chiamatoLoaded = true;
                datiLoadedListener.OnDatiLoaded();
            }
        }
    }

    public static ArrayList<Categoria> getCategorie(){
        return categorie;
    }

    public static ArrayList<String> getCategorieAsString(){
        ArrayList<String> stringhe = new ArrayList<>();
        for(Categoria c : categorie){
            stringhe.add(c.getNome());
        }
        return stringhe;
    }

    public static ArrayList<Prodotto> getProdottiByCategoria(int idCategoria, int idLista) {
        ArrayList<Prodotto> categorizzati = new ArrayList<>();
        if(idCategoria != 0) {
            for (Prodotto p : prodotti) {
                if (p.getCategoria().getID() == idCategoria) {
                    categorizzati.add(p);
                }
            }
        }else{
            //Se la categoria è 0 sono i prodotti consigliati
            for(ProdottoConsigliato pc : prodottiConsigliati){
                if (pc.getLista().getID() == idLista) {
                    categorizzati.add(pc.getProdotto());
                }
            }
            //Se la lista dei consigliati è vuota riempiamola con qualcosa a caso tipo
            if (categorizzati.size() == 0) {
                System.out.println("Dati - Non ci sono prodotti da consigliare");
                if (prodotti.size() > 0) {
                    categorizzati.add(prodotti.get((int) (Math.random() * prodotti.size())));
                }
            }
        }
        return categorizzati;
    }

    public static ArrayList<String> prodottiToString(ArrayList<Prodotto> prods){
        ArrayList<String> stringhe = new ArrayList<>();
        for(Prodotto p : prods){
            stringhe.add(p.getNome());
        }
        return stringhe;
    }
    public static void aggiungiProdottoInLista(ProdottoInLista prodottoInLista){
        prodottiInLista.add(prodottoInLista);
        getListaByID(prodottoInLista.getIdLista()).aggiungiQuantita();
    }

    public static void aggiungiProdotto(Prodotto prod){
        prodotti.add(prod);
    }

    public static void aggiungiLista(Lista lista){
        listeMie.add(lista);
    }

    private static void salvaUtenti(ArrayList<Utente> users){
        for(Utente u : users){
            boolean possoAggiungere = true;
            for(Utente su : utenti){
                if(u.getUsername().equals(su)){
                    possoAggiungere = false;
                    break;
                }
            }
            //Nel caso non sia stata già trovata corrispondenza aggiungiamo
            if(possoAggiungere){
                utenti.add(u);
            }
        }
    }

    private static void salvaUtente(Utente utente){
        for(Utente su : utenti){
            if(utente.getUsername().equals(su)){
                return;
            }
        }
        //Se arriviamo alla fine del ciclo senza essere usciti
        utenti.add(utente);
    }

    private static void aggiungiCondivisione(Lista list,Utente utente){
        list.getVistaDa().add(utente);
        System.out.println("Dati - Aggiunto " + utente.getNome() + " alla visualizzazione della lista " + list.getNome());
        //Ora salviamolo negli amici
        salvaUtente(utente);
    }

    public static void rimuoviProdottoInLista(final ProdottoInLista prodottoInLista, final ProdottiInListaEliminatiListener prodottiListener) {
        String[] pars = {"req", "listID","productID"};
        String[] vals = {"deleteProductInList", prodottoInLista.getIdLista() + "",prodottoInLista.getProdotto().getID()+""};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success) {
                    prodottiInLista.remove(prodottoInLista);
                    prodottiListener.OnProdottoInListaEliminato(prodottoInLista);
                }else{
                    prodottiListener.OnProdottoInListaNonEliminato(prodottoInLista, data);
                }
            }
        }, "eliminaProdotto", pars, vals);
    }

    public static void compraProdotto(final ProdottoInLista prodottoInLista, final ProdottiCompratiListener prodottiListener) {
        String[] pars = {"req","userID", "listID","productID"};
        String[] vals = {"buyProduct",userID, prodottoInLista.getIdLista() + "",prodottoInLista.getProdotto().getID()+""};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success) {
                    //Togliamolo dalla lista anche se non è stato eliminato
                    prodottiInLista.remove(prodottoInLista);
                    prodottiListener.OnProdottoComprato(prodottoInLista);
                }else{
                    prodottiListener.OnProdottoNonComprato(prodottoInLista, data);
                }
            }
        }, "compraProdotto", pars, vals);
    }

    //Elimina la lista solo se mia
    public static void eliminaLista(final Lista lista, final OnListeEliminaListener listeListener) {
        String[] pars = {"req","listID","userID"};
        String[] vals = {"deleteList", lista.getID() + "", userID};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success) {
                    listeMie.remove(lista);
                    listeListener.OnListaEliminata(lista);
                }else{
                    listeListener.OnListaNonEliminata(lista, data);
                }
            }
        }, "eliminaLista", pars, vals);
    }

    public static void creaProdottoEAggiungiloALista(final ProdottoInLista prodottoInLista, final ProdottiInListaCreatiListener prodottiListener) {
        String[] parametri = {"req","userID","name","price","brand","dimension","categoryID"};
        String[] valori = {"createProduct", userID, prodottoInLista.getProdotto().getNome(), prodottoInLista.getProdotto().getPrezzo() + "", prodottoInLista.getProdotto().getMarca(), prodottoInLista.getProdotto().getDimensione(), prodottoInLista.getProdotto().getCategoria().getID() + ""};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success) {
                    final Prodotto prod = prodottoInLista.getProdotto();
                    //Aggiungiamo il prodotto anche in locale senza che debba riscaricarli tutti
                    /*Dati.*/aggiungiProdotto(prod);
                    String[] parametri = {"req", "userID", "listID", "productID", "quantity", "description"};
                    String[] valori = {"addProduct", userID, prodottoInLista.getIdLista() + "", prod.getID() + "", prodottoInLista.getQuantita() + "", prodottoInLista.getDescrizione()};

                    Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
                        @Override
                        public void HandleData(String nome, boolean success, String data) {
                            if(success) {
                            /*Dati.*/
                                aggiungiProdottoInLista(prodottoInLista);
                                //Notifichiamo il listener che il prodotto è stato creato con successo
                                prodottiListener.OnProdottoInListaCreato(prodottoInLista);
                            }else{
                                prodottiListener.OnProdottoInListaNonCreato(prodottoInLista, data);
                                //Toast.makeText(ctx, ctx.getString(R.string.Error) + " : " + data, Toast.LENGTH_SHORT).show();
                            }
                        }
                    },"Aggiungi a lista",parametri,valori);

                }else{
                    //Toast.makeText(ctx, ctx.getString(R.string.Error) + " : " + data, Toast.LENGTH_SHORT).show();
                    prodottiListener.OnProdottoInListaNonCreato(prodottoInLista, data);
                }
            }
        }, "creaProdotto", parametri, valori);
    }

    public static void findUser(final String name, final OnRichiesteUtentiListener listener) {
        String[] parametri = {"req","username"};
        String[] valori = {"findUser",name};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success){
                    //System.out.println("Dati (Debug) - "+data);
                    ArrayList<Utente> users = new ArrayList<>(Arrays.asList(WebsiteDataManager.getUtenti(data)));
                    listener.OnRicercaUtentiCompletata(users);
                }else{
                    System.out.println("Dati - Non esiste nessun utente che si chiama " + name);
                    listener.OnRicercaUtentiVuota();
                }
            }
        },"trovaUtenti",parametri,valori);
    }

    public static void condividiLista(final Lista lista, final Utente user, final OnListeCondividiListener listaListener) {
        String[] parametri = {"req","listID","userID"};
        String[] valori = {"shareList",lista.getID()+"",user.getUserID()+""};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success){
                    aggiungiCondivisione(lista,user);
                    listaListener.OnListaCondivisa(lista, user);
                }else{
                    listaListener.OnListaNonCondivisa(lista, user, data);
                }
            }
        }, "condividiLista", parametri, valori);
    }

    public static void aggiungiProdottoALista(final ProdottoInLista prodottoInLista, final OnProdottiAggiungiListener onProdottiAggiungiListener) {
        String[] parametri = {"req", "userID", "listID", "productID", "quantity", "description"};
        String[] valori = {"addProduct", userID, prodottoInLista.getIdLista() + "", prodottoInLista.getProdotto().getID() + "", prodottoInLista.getQuantita() + "", prodottoInLista.getDescrizione()};
        //Chiediamo al sito di creare il prodotto
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nomeRichiesta, boolean success, String data) {
                if (success) {
                    onProdottiAggiungiListener.OnProdottoAggiunto(prodottoInLista);
                } else {
                    onProdottiAggiungiListener.OnProdottoNonAggiunto(prodottoInLista, data);
                }
            }
        }, "aggiungiProdotto", parametri, valori);
    }


    public interface DatiLoadedListener {
        void OnDatiLoaded();
    }

    public interface ProdottiCompratiListener {
        void OnProdottoComprato(ProdottoInLista prodottoInLista);

        void OnProdottoNonComprato(ProdottoInLista prodottoInLista, String errore);

    }

    public interface ProdottiInListaCreatiListener {
        void OnProdottoInListaCreato(ProdottoInLista prodottoInLista);
        void OnProdottoInListaNonCreato(ProdottoInLista prodottoInLista, String errore);
    }

    public interface ProdottiInListaEliminatiListener {
        void OnProdottoInListaEliminato(ProdottoInLista prodottoInLista);
        void OnProdottoInListaNonEliminato(ProdottoInLista prodottoInLista, String errore);
    }

    public interface OnProdottiAggiungiListener {
        void OnProdottoAggiunto(ProdottoInLista prodottoInLista);

        void OnProdottoNonAggiunto(ProdottoInLista prodottoInLista, String errore);
    }

    public interface OnListeCondividiListener {
        void OnListaCondivisa(Lista lista, Utente utente);
        void OnListaNonCondivisa(Lista lista, Utente utente, String errore);
    }

    public interface OnListeEliminaListener {
        void OnListaEliminata(Lista lista);

        void OnListaNonEliminata(Lista lista, String errore);
    }

    public interface OnRichiesteUtentiListener {
        void OnRicercaUtentiCompletata(ArrayList<Utente> utenti);

        void OnRicercaUtentiVuota();
    }
}
