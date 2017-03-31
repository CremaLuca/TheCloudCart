package org.cramest.thecloudcart.classi;

import java.sql.Date;

/**
 * Created by cremaluca on 09/03/2017.
 */
public class ProdottoInLista {

    private Prodotto prodotto;
    private int quantita;
    private String descrizione;

    public ProdottoInLista(Prodotto prodotto, int quantita, String descrizione) {
        this.prodotto = prodotto;
        this.quantita = quantita;
        this.descrizione = descrizione;
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
