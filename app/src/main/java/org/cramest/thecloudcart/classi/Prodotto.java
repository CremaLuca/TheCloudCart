package org.cramest.thecloudcart.classi;

public class Prodotto {

    private int ID;
    private String nome;
    private double prezzo;
    private String marca;
    private String dimensione;
    private Categoria categoria;

    public Prodotto(int ID,String nome, double prezzo, String marca, String dimensione, Categoria categoria) {
        this.ID = ID;
        this.nome = nome;
        this.prezzo = prezzo;
        this.marca = marca;
        this.dimensione = dimensione;
        this.categoria = categoria;
    }

    public Prodotto(String nome){
        this.ID = -1;
        this.nome = nome;
    }

    public int getID() { return ID; }
    public String getNome() {
        return nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public String getMarca() {
        return marca;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getDimensione() {
        return dimensione;
    }

    @Override
    public String toString(){
        return nome + "( marca: " + marca + ", prez: " + prezzo + ", cat: " + categoria.getNome() +")";
    }
}
