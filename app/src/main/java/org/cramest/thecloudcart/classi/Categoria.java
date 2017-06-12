package org.cramest.thecloudcart.classi;


public class Categoria {

    private int ID;
    private String nome;

    public Categoria(int ID, String nome) {
        this.ID = ID;
        this.nome = nome;
    }

    public int getID() {
        return ID;
    }

    public String getNome() {
        return nome;
    }
}
