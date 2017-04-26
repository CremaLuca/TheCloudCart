package org.cramest.thecloudcart.classi;

import android.content.Context;
import android.support.annotation.Nullable;

import org.cramest.thecloudcart.fragments.ProdottiFragment;
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

    public Dati(Context ctx,String userID){
        System.out.println("User id passato : " + userID);
        //Ci salviamo il listener se esiste
        if (ctx instanceof OnDatiLoadedListener) {
            mListener = (OnDatiLoadedListener) ctx;
        } else {
            throw new RuntimeException(ctx.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        //richiediamo le categorie
        Connettore.getInstance(ctx).GetDataFromWebsite(this,"categorie","req","getAllCategorie");
        //richiediamo i prodotti
        Connettore.getInstance(ctx).GetDataFromWebsite(this,"prodotti","req","getAllProdotti");
        //richiediamo le nostre liste
        String[] parametriMie = {"req","userID"};
        String[] valoriMie = {"getUserList",userID};
        Connettore.getInstance(ctx).GetDataFromWebsite(this,"listeSpesa",parametriMie,valoriMie);
        //richiediamo le liste condivise
        String[] parametriCondivise = {"req","userID"};
        String[] valoriCondivise = {"getCondivisioniUtente",userID};
        Connettore.getInstance(ctx).GetDataFromWebsite(this, "listeSpesaCondivise", parametriCondivise, valoriCondivise);
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success){
            if(nome.equals("prodotti")){
                prodotti = new ArrayList<Prodotto>(Arrays.asList(WebsiteDataManager.getProdotti(data)));
            }
            if(nome.equals("categorie")){
                categorie = new ArrayList<Categoria>(Arrays.asList(WebsiteDataManager.getCategorie(data)));
            }
            if(nome.equals("listeSpesa")){
                listeMie = new ArrayList<Lista>(Arrays.asList(WebsiteDataManager.getListeUtente(data)));
            }
            if(nome.equals("listeSpesaCondivise")){
                listeCondivise = new ArrayList<Lista>(Arrays.asList(WebsiteDataManager.getListeUtente(data)));
            }

            if(prodotti != null && categorie != null && listeMie != null && listeCondivise != null){
                LoadedDati();
            }

        }
    }

    public static Prodotto getProdottoFromID(int ID){
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
    public static Categoria getCategoriaFromID(int ID){
        if(categorie != null && categorie.size() > 0) {
            //ID 1-13
            return categorie.get(ID-1);
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
}
