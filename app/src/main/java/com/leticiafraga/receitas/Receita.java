package com.leticiafraga.receitas;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Receita implements Serializable {
    private String fonte;
    private String foto;
    private String idReceita;
    private String ingredientes;
    private String preparo;
    private String titulo;

    public Receita() {
        this.idReceita = ConfigFirebase.getFirebase().child(ConfigFirebase.getIdUsuario()).push().getKey();
    }

    public void salvar() {
        ConfigFirebase.getFirebase().child(ConfigFirebase.getIdUsuario())
                .child("receitas").child(this.idReceita).setValue(this);
    }

    public void remover() {
        ConfigFirebase.getFirebase().child(ConfigFirebase.getIdUsuario())
                .child("receitas").child(getIdReceita()).removeValue();
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFoto() {
        return this.foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIngredientes() {
        return this.ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getPreparo() {
        return this.preparo;
    }

    public void setPreparo(String preparo) {
        this.preparo = preparo;
    }

    public String getFonte() {
        return this.fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public String getIdReceita() {
        return this.idReceita;
    }

    public void setIdReceita(String idReceita) {
        this.idReceita = idReceita;
    }
}

