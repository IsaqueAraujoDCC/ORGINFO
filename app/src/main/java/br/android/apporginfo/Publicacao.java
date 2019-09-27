package br.android.apporginfo;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Publicacao {
    private List<String> comentarios;
    private String id;
    private String endereco, titulo, texto;
    private int quantCurtidas;
    private String meuComentario;
    private boolean curti;
    private List<String> idsComentarios;
    public Publicacao(String id, String endereco, String titulo, String texto, int quantCurtidas, List<String> comentarios, boolean curti){
        this.id = id;
        this.endereco = endereco;
        this.titulo = titulo;
        this.texto = texto;
        this.quantCurtidas = quantCurtidas;
        this.comentarios = new ArrayList<>();
        this.idsComentarios = new ArrayList<>();
        this.curti = curti;
        setComentarios(comentarios);
    }

    public List<String> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<String> comentarios) {
        this.comentarios = comentarios;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getQuantCurtidas() {
        return quantCurtidas;
    }

    public void setQuantCurtidas(int quantCurtidas) {
        this.quantCurtidas = quantCurtidas;
    }

    public String getMeuComentario() {
        return meuComentario;
    }

    public void setMeuComentario(String meuComentario) {
        this.meuComentario = meuComentario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCurti() {
        return curti;
    }

    public void setCurti(boolean curti) {
        this.curti = curti;
    }

    public List<String> getIdsComentarios() {
        return idsComentarios;
    }

    public void setIdsComentarios(List<String> idsComentarios) {
        this.idsComentarios = idsComentarios;
    }
}
