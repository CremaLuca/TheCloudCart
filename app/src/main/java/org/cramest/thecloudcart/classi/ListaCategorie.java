package org.cramest.thecloudcart.classi;

import android.content.Context;

import org.cramest.thecloudcart.classi.*;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.WebsiteDataManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by User on 20/01/2017.
 */

public class ListaCategorie implements DataHandler {

    private static ArrayList<Categoria> categorie;

    public static Categoria getCategoriaFromID(int ID){
        if(categorie != null) {
            //ID 1-13
            return categorie.get(ID-1);
        }
        return null;
    }

    public void recuperaCategorie(Context ctx){
        if(categorie != null){
            return;
        }
        //richiediamo le categorie
        Connettore.getInstance(ctx).GetDataFromWebsite(ListaCategorie.this,"categorie","req","getAllCategorie");
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success){
            if(nome.equals("categorie")){
                //inizializziamo l'arrayList direttamente con l'array dato dal WebsiteDataManager
                categorie = new ArrayList<Categoria>(Arrays.asList(WebsiteDataManager.getCategorie(data)));
            }
        }
    }
}
