package org.cramest.thecloudcart.classi;

import java.sql.Date;

/**
 * Created by cremaluca on 09/03/2017.
 */
public class ProdottoInLista {

    private int idLista;
    private Prodotto prodotto;
    private int quantita;
    private String descrizione;

    public ProdottoInLista(int idLista,Prodotto prodotto, int quantita, String descrizione) {
        this.idLista = idLista;
        this.prodotto = prodotto;
        this.quantita = quantita;
        this.descrizione = descrizione;
    }

    public int getIdLista() {
        return idLista;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
