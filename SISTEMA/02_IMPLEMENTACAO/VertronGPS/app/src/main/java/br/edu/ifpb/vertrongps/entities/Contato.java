package br.edu.ifpb.vertrongps.entities;

/**
 * Created by emerson on 28/01/18.
 */

public class Contato {

    private String nome;
    private String numero;
    private int id;

    public Contato() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
