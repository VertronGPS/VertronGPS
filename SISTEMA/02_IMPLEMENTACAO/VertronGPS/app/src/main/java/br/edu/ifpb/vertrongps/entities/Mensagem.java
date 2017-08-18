package br.edu.ifpb.vertrongps.entities;

/**
 * Created by Emerson on 16/08/2017.
 */
public class Mensagem {

    private String texto;
    private String destinatario;
    private String data;

    public Mensagem(String texto, String destinatario, String data) {
        this.texto = texto;
        this.destinatario = destinatario;
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

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
