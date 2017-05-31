package org.cramest.thecloudcart.network;

import org.cramest.thecloudcart.classi.*;

/**
 * Created by User on 20/01/2017.
 */

public class WebsiteDataManager {

    final static String sepRighe = "§";
    final static String sepColonne = "♦";

    public static Lista[] getListeUtente(String data){
        if(data != null) {
            String[] strListe = data.split(sepRighe);
            Lista[] liste = new Lista[strListe.length];
            for (int i = 0; i < strListe.length; i++) {
                String[] pezziLista = strListe[i].split(sepColonne);
                liste[i] = new Lista(Integer.parseInt(pezziLista[0]), pezziLista[1], Integer.parseInt(pezziLista[2]));
            }
            return liste;
        }
        return null;
    }

    public static Categoria[] getCategorie(String data){
        if(data != null) {
            String[] strCategorie = data.split(sepRighe);
            Categoria[] categorie = new Categoria[strCategorie.length];
            for (int i = 0; i < strCategorie.length; i++) {
                String[] pezziCategoria = strCategorie[i].split(sepColonne);
                categorie[i] = new Categoria(Integer.parseInt(pezziCategoria[0]), pezziCategoria[1]);
            }
            return categorie;
        }
        return null;
    }

    public static Prodotto[] getProdotti(String data){
        if(data != null) {
            String[] strProdotti = data.split(sepRighe);
            Prodotto[] prodotti = new Prodotto[strProdotti.length];
            for (int i = 0; i < strProdotti.length; i++) {
                String[] strProdotto = strProdotti[i].split(sepColonne);
                int ID = Integer.parseInt(strProdotto[0]);
                String nome = strProdotto[1];
                double prezzo = 0;
                try {
                    prezzo = Double.parseDouble(strProdotto[2]);
                } catch (Exception e) {
                }
                String marca = strProdotto[3];
                String dimensione = strProdotto[4];
                Categoria categoria = Dati.getCategoriaByID(Integer.parseInt(strProdotto[5]));
                prodotti[i] = new Prodotto(ID, nome, prezzo, marca, dimensione, categoria);
                System.out.println("Creato nuovo prodotto : " + prodotti[i]);
            }
            return prodotti;
        }
        return null;
    }

    public static ProdottoInLista[] getProdottiInLista(String data,int listID){
        if(data != null){
            String[] strProdottiInLista = data.split(sepRighe);
            if(strProdottiInLista.length > 0) {
                ProdottoInLista[] prodottiInLista = new ProdottoInLista[strProdottiInLista.length];
                for (int i = 0; i < strProdottiInLista.length; i++) {
                    String[] strProdottoInLista = strProdottiInLista[i].split(sepColonne);
                    System.out.println("IDProdotto : " + strProdottoInLista[0]);

                    Prodotto prodotto = Dati.getProdottoByID(Integer.parseInt(strProdottoInLista[0]));

                    int qta = Integer.parseInt(strProdottoInLista[1]);

                    String descrizione = "";
                    if (strProdottoInLista.length > 2) {
                        descrizione = strProdottoInLista[2];
                    }

                    prodottiInLista[i] = new ProdottoInLista(listID, prodotto, qta, descrizione);
                }
                return prodottiInLista;
            }
            return null;
        }
        return null;
    }

    public static Utente[] getUtenti(String data){
        if(data!=null){
            String[] strUtenti = data.split(sepRighe);
            if(strUtenti.length > 0){
                Utente[] utenti = new Utente[strUtenti.length];
                for (int i = 0; i < strUtenti.length; i++) {
                    String[] strUtente = strUtenti[i].split(sepColonne);

                    int userID = Integer.parseInt(strUtente[0]);
                    String username = strUtente[1];
                    String nome = strUtente[2];

                    utenti[i] = new Utente(userID,username,nome);
                }
                return utenti;
            }
        }
        return null;
    }

    public static ProdottoConsigliato[] getProdottiConsigliati(String data){
        if(data!=null){
            String[] strProdotti = data.split(sepRighe);
            if(strProdotti.length > 0){
                ProdottoConsigliato[] prodotti = new ProdottoConsigliato[strProdotti.length];
                for (int i = 0; i < strProdotti.length; i++) {
                    String[] strProdotto = strProdotti[i].split(sepColonne);

                    int productID = Integer.parseInt(strProdotto[0]);
                    Prodotto p = Dati.getProdottoByID(productID);
                    int listID = Integer.parseInt(strProdotto[1]);
                    Lista l = Dati.getListaByID(listID);
                    int giorni = Integer.parseInt(strProdotto[2]);

                    prodotti[i] = new ProdottoConsigliato(p,l,giorni);
                }
                return prodotti;
            }
        }
        return null;
    }

}
