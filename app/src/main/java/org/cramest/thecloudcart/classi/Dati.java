package org.cramest.thecloudcart.classi;

import android.content.Context;
import android.widget.Toast;

import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.WebsiteDataManager;

import java.util.ArrayList;
import java.util.Arrays;


public class Dati implements DataHandler{

    private static OnDatiListener mListener;

    public static Dati instance;

    private static ArrayList<Lista> listeMie;
    private static ArrayList<Lista> listeCondivise;
    private static ArrayList<Categoria> categorie;
    private static ArrayList<Prodotto> prodotti;
    private static ArrayList<ProdottoInLista> prodottiInLista;
    private static ArrayList<ProdottoConsigliato> prodottiConsigliati = new ArrayList<ProdottoConsigliato>();
    private static ArrayList<Utente> utenti = new ArrayList<Utente>();

    private String userID;
    private static Context ctx;

    private boolean chiamatoLoaded = false;

    private int listeCaricate = 0;
    private int listeDaCaricare;

    public Dati(Context ctx,String userID){
        System.out.println("Dati - User id : " + userID);
        //Ci salviamo il listener se esiste
        if (ctx instanceof OnDatiListener) {
            mListener = (OnDatiListener) ctx;
        } else {
            throw new RuntimeException(ctx.toString()
                    + " must implement OnDatiListener");
        }
        this.userID = userID;
        this.ctx = ctx;
        instance = this;
        //Svuotiamo le liste nel caso contengano qualcosa
        svuotaListe();

        richiediCategorie(ctx);
    }

    private void svuotaListe(){
        listeMie = null;
        listeCondivise = null;
        categorie = null;
        prodotti = null;
        prodottiInLista = null;
        prodottiConsigliati = new ArrayList<ProdottoConsigliato>();
        utenti = new ArrayList<Utente>();
    }

    private void richiediCategorie(Context ctx){
        //richiediamo le categorie
        Connettore.getInstance(ctx).GetDataFromWebsite(this,"categorie","req","getAllCategorie");
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
                    if(data != null && data != ""){
                        prodottiConsigliati = new ArrayList<>(Arrays.asList(WebsiteDataManager.getProdottiConsigliati(data)));
                    }
                }
            }
        }, "GetShouldBuy", pars, vals);
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success){
            if(nome.equals("categorie")){
                categorie = new ArrayList<Categoria>(Arrays.asList(WebsiteDataManager.getCategorie(data)));
                richiediProdotti(ctx,userID);
            }
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
                prodotti = new ArrayList<Prodotto>();
                richiediListeSpesa(ctx,userID);
            }
            if(nome.equals("listeSpesa")){
                System.out.println("Dati - Non ci sono liste della spesa");
                listeMie = new ArrayList<Lista>();
            }
            if(nome.equals("listeSpesaCondivise")){
                listeCondivise = new ArrayList<Lista>();
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
            ArrayList<ProdottoInLista> prodottiDellaLista = new ArrayList<ProdottoInLista>();
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
        return null;
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
        if (mListener != null) {
            if(!chiamatoLoaded) {
                chiamatoLoaded = true;
                mListener.OnDatiLoaded();
            }
        }
    }

    private void OnProdottoInListaEliminato(int listID) {
        if (mListener != null) {
            mListener.OnProdottoInListaEliminato(listID);
        }
    }

    public static ArrayList<Categoria> getCategorie(){
        return categorie;
    }

    public static ArrayList<String> getCategorieAsString(){
        ArrayList<String> stringhe = new ArrayList<String>();
        for(Categoria c : categorie){
            stringhe.add(c.getNome());
        }
        return stringhe;
    }

    public static ArrayList<Prodotto> getProdottiByCategoria(int idCategoria, int idLista) {
        ArrayList<Prodotto> categorizzati = new ArrayList<Prodotto>();
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
        ArrayList<String> stringhe = new ArrayList<String>();
        for(Prodotto p : prods){
            stringhe.add(p.getNome());
        }
        return stringhe;
    }
    public static void aggiungiProdottoInLista(ProdottoInLista prodottoInLista){
        prodottiInLista.add(prodottoInLista);
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
        ArrayList<Utente> vistaDa = list.getVistaDa();
        vistaDa.add(utente);
        list.setVistaDa(vistaDa);
        System.out.println("Dati - Aggiunto " + utente.getNome() + " alla visualizzazione della lista " + list.getNome());
        //Controllo se la lista in cui abbiamo aggiunto è la stessa che teniamo salvata noi, dovrebbe esserlo
        Lista listaBoh = getListaByID(list.getID());
        System.out.println("Dati - Controllo: " + listaBoh.getVistaDa().get(listaBoh.getVistaDa().size()-1).getUsername());
        //Ora salviamolo negli amici
        salvaUtente(utente);
    }

    public void rimuoviProdottoInLista(final ProdottoInLista prodottoInLista){
        String[] pars = {"req", "listID","productID"};
        String[] vals = {"deleteProductInList", prodottoInLista.getIdLista() + "",prodottoInLista.getProdotto().getID()+""};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success) {
                    Toast.makeText(ctx, "Prodotto eliminato con successo", Toast.LENGTH_SHORT).show();
                    int listID = Integer.parseInt(nome.split("=")[1]);
                    prodottiInLista.remove(prodottoInLista);
                    OnProdottoInListaEliminato(listID);
                }else{
                    Toast.makeText(ctx, "Errore: " + data, Toast.LENGTH_SHORT).show();
                }
            }
        }, "eliminaProdotto=" + prodottoInLista.getIdLista(), pars, vals);
    }

    public static void compraProdotto(String userID,final ProdottoInLista prodottoInLista){
        String[] pars = {"req","userID", "listID","productID"};
        String[] vals = {"buyProduct",userID, prodottoInLista.getIdLista() + "",prodottoInLista.getProdotto().getID()+""};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success) {
                    //Togliamolo dalla lista anche se non è stato eliminato
                    prodottiInLista.remove(prodottoInLista);
                    mListener.OnProdottoComprato(prodottoInLista.getIdLista());
                    Toast.makeText(ctx, data, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ctx, data, Toast.LENGTH_SHORT).show();
                }
            }
        }, "compraProdotto=" + prodottoInLista.getIdLista(), pars, vals);
    }

    //Elimina la lista solo se mia
    public static void eliminaLista(final int listID,final String userID){
        String[] pars = {"req","listID","userID"};
        String[] vals = {"deleteList",listID+"",userID};
        System.out.println("Dati - Elimino la lista " + listID);
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success) {
                    listeMie.remove(getListaByID(listID));
                    Toast.makeText(ctx, data, Toast.LENGTH_SHORT).show();
                    mListener.OnListaEliminata();
                }else{
                    Toast.makeText(ctx, data,Toast.LENGTH_SHORT);
                }
            }
        }, "eliminaLista", pars, vals);
    }

    public static void creaProdottoEAggiungiloALista(final String userID, final String nomeProdotto, final Double prezzo, final String marca, final String dimensione, final Categoria categoria, final int quantita, final String descrizione, final int listID) {
        String[] parametri = {"req","userID","name","price","brand","dimension","categoryID"};
        String[] valori = {"createProduct", userID, nomeProdotto, prezzo + "", marca, dimensione, categoria.getID() + ""};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success) {
                    //Ricostruiamo il prodotto con il nuovo ID
                    int ID = Integer.parseInt(data);
                    final Prodotto prod = new Prodotto(ID, nomeProdotto, prezzo, marca, dimensione, categoria);

                    //Toast.makeText(ctx,"Prodotto salvato con successo (debug-ID: "+data+")", Toast.LENGTH_SHORT).show();

                    //Aggiungiamo il prodotto anche in locale senza che debba riscaricarli tutti
                    /*Dati.*/aggiungiProdotto(prod);

                    String[] parametri = {"req", "userID", "listID", "productID", "quantity", "description"};
                    String[] valori = {"addProduct",userID,listID+"",prod.getID()+"",quantita+"",descrizione};
                    Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
                        @Override
                        public void HandleData(String nome, boolean success, String data) {
                            if(success) {
                                ProdottoInLista tmpProdottoInLista = new ProdottoInLista(listID, prod, quantita, descrizione);
                            /*Dati.*/
                                aggiungiProdottoInLista(tmpProdottoInLista);

                                //Notifichiamo la mainActivity che il prodotto è stato creato con successo
                                mListener.OnProdottoInListaCreato(tmpProdottoInLista);
                            }else{
                                //TODO : GESTIRE TUTTI GLI ERRORI DI AGGIUNTA DEL PRODOTTO
                                Toast.makeText(ctx,data, Toast.LENGTH_SHORT).show();
                            }
                        }
                    },"Aggiungi a lista",parametri,valori);

                }else{
                    Toast.makeText(ctx,data, Toast.LENGTH_SHORT).show();
                    /*mListener.onProdottoNonCreato(listID);*/
                }
            }
        }, "creaProdotto", parametri, valori);
    }

    public static void findUser(final String name,final OnRichiesteUtentiListener listener){
        String[] parametri = {"req","username"};
        String[] valori = {"findUser",name};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success){
                    //System.out.println("Dati (Debug) - "+data);
                    ArrayList<Utente> users = new ArrayList<Utente>(Arrays.asList(WebsiteDataManager.getUtenti(data)));
                    listener.OnRicercaUtentiCompletata(users);
                }else{
                    System.out.println("Dati - Non esiste nessun utente che si chiama " + name);
                }
            }
        },"trovaUtenti",parametri,valori);
    }

    public static void condividiLista(final Lista lista, final Utente user){
        String[] parametri = {"req","listID","userID"};
        String[] valori = {"shareList",lista.getID()+"",user.getUserID()+""};
        Connettore.getInstance(ctx).GetDataFromWebsite(new DataHandler() {
            @Override
            public void HandleData(String nome, boolean success, String data) {
                if(success){
                    aggiungiCondivisione(lista,user);
                    mListener.OnListaCondivisa(lista,user);
                }else{
                    Toast.makeText(ctx, nome + " - " + data, Toast.LENGTH_SHORT).show();
                    mListener.OnListaNonCondivisa();
                }
            }
        }, "condividiLista", parametri, valori);
    }

    public interface OnDatiListener{
        void OnDatiLoaded();
        void OnProdottoInListaEliminato(int listID);
        void OnProdottoComprato(int listID);
        void OnListaEliminata();
        void OnProdottoInListaCreato(ProdottoInLista prodottoInLista);
        void OnListaCondivisa(Lista lista, Utente utente);
        void OnListaNonCondivisa();
    }

    public interface OnRichiesteUtentiListener{
        void OnRicercaUtentiCompletata(ArrayList<Utente> utentiCorrispondenti);
    }
}
