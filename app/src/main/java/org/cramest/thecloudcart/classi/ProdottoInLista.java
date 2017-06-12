package org.cramest.thecloudcart.classi;

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
