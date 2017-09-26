package br.edu.ifpb.vertrongps.entities;

/**
 * Created by Emerson on 16/08/2017.
 */
public class Mensagem {

    private String texto;
    private String remetente;
    private String data;

    public Mensagem(String texto, String remetente, String data) {
        this.texto = texto;
        this.remetente = remetente;
        this.data = data;
    }

    public Mensagem() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
