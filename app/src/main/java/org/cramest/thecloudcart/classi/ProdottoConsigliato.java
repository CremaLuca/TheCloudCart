package org.cramest.thecloudcart.classi;

public class ProdottoConsigliato {

    private Prodotto prodotto;
    private Lista lista;
    private int giorniDiRitardo;

    public ProdottoConsigliato(Prodotto prodotto, Lista lista,int giorniDiRitardo) {
        this.prodotto = prodotto;
        this.lista = lista;
        this.giorniDiRitardo = giorniDiRitardo;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public Lista getLista() {
        return lista;
    }

    public int getGiorniDiRitardo() {
        return giorniDiRitardo;
    }
}
