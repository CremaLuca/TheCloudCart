package org.cramest.thecloudcart.classi;

import android.content.Context;

import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.WebsiteDataManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by User on 20/01/2017.
 */

public class ListaProdotti implements DataHandler{

    private static ArrayList<Prodotto> prodotti;

    public void recuperaProdotti(Context ctx){
        if(prodotti != null){
            return;
        }
        //richiediamo le categorie
        Connettore.getInstance(ctx).GetDataFromWebsite(this,"prodotti","req","getAllProdotti");
    }


    public static Prodotto getProdottoFromID(int ID){
        if(prodotti != null) {
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

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success){
            if(nome.equals("prodotti")){
                //inizializziamo l'arrayList direttamente con l'array dato dal WebsiteDataManager
                prodotti = new ArrayList<Prodotto>(Arrays.asList(WebsiteDataManager.getProdotti(data)));
            }
        }
    }
}
