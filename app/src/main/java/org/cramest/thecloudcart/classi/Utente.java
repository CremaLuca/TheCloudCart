package org.cramest.thecloudcart.classi;

public class Utente {

    private int userID;
    private String username;
    private String nome;

    public Utente(int userID,String username,String nome){
        this.userID = userID;
        this.nome = nome;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getUserID() {
        return userID;
    }

    public String getNome() {
        return nome;
    }
}
