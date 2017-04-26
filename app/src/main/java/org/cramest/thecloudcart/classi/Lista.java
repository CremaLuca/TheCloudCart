package org.cramest.thecloudcart.classi;

import java.util.ArrayList;

/**
 * Created by cremaluca on 11/03/2017.
 */
public class Lista {

    private int ID;
    private String nome;
    private int quantita;

    public Lista(int ID, String nome,int quantita) {
        this.ID = ID;
        this.nome = nome;
        this.quantita = quantita;
    }

    public int getID() {
        return ID;
    }

    public String getNome() {
        return nome;
    }


    public int getQuantita() { return quantita; }
}
