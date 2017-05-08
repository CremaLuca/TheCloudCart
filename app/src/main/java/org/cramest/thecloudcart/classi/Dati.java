package org.cramest.thecloudcart.classi;

import android.content.Context;

import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.WebsiteDataManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by User on 26/04/2017.
 */

public class Dati implements DataHandler{

    private OnDatiLoadedListener mListener;

    private static ArrayList<Lista> listeMie;
    private static ArrayList<Lista> listeCondivise;
    private static ArrayList<Categoria> categorie;
    private static ArrayList<Prodotto> prodotti;
    private static ArrayList<ProdottoInLista> prodottiInLista;

    private String userID;
    private Context ctx;

    public Dati(Context ctx,String userID){
        System.out.println("User id passato : " + userID);
        //Ci salviamo il listener se esiste
        if (ctx instanceof OnDatiLoadedListener) {
            mListener = (OnDatiLoadedListener) ctx;
        } else {
            throw new RuntimeException(ctx.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.userID = userID;
        this.ctx = ctx;
        richiediCategorie(ctx);
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

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success){
            if(nome.equals("categorie")){
                categorie = new ArrayList<Categoria>(Arrays.asList(WebsiteDataManager.getCategorie(data)));
                richiediProdotti(ctx,userID);
            }
            if(nome.equals("prodotti")){
                prodotti = new ArrayList<Prodotto>(Arrays.asList(WebsiteDataManager.getProdotti(data)));
                richiediListeSpesa(ctx,userID);
            }
            if(nome.equals("listeSpesa")){
                listeMie = new ArrayList<Lista>(Arrays.asList(WebsiteDataManager.getListeUtente(data)));
                richiediProdottiInLista(ctx);
            }
            if(nome.equals("listeSpesaCondivise")){
                listeCondivise = new ArrayList<Lista>(Arrays.asList(WebsiteDataManager.getListeUtente(data)));
                richiediProdottiInLista(ctx);
            }
            if(nome.startsWith("ProdottiLista")){
                //Recuperiamo l'id della lista dal nome della richiesta
                int listID = Integer.parseInt(nome.split("=")[1]);
                if(prodottiInLista == null){
                    prodottiInLista = new ArrayList<ProdottoInLista>();
                }
                if(data != null) {
                    ArrayList<ProdottoInLista> nuoviProdotti = new ArrayList<ProdottoInLista>(Arrays.asList(WebsiteDataManager.getProdottiInLista(data, listID)));
                    if(nuoviProdotti != null) {
                        prodottiInLista.addAll(nuoviProdotti);
                    }
                }
            }
            if(prodotti != null && categorie != null && listeMie != null && listeCondivise != null && prodottiInLista != null){
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

    public interface OnDatiLoadedListener{
        void OnDatiLoaded();
    }

    public void LoadedDati() {
        if (mListener != null) {
            mListener.OnDatiLoaded();
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

    public static ArrayList<Prodotto> getProdottiByCategoria(int idCategoria){
        ArrayList<Prodotto> categorizzati = new ArrayList<Prodotto>();
        for(Prodotto p : prodotti){
            if(p.getCategoria().getID() == idCategoria){
                categorizzati.add(p);
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
}
