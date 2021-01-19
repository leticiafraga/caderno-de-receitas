package com.leticiafraga.receitas;

import java.util.List;

public class Usuario {

    private String email;
    private List<String> listaReceitas;
    private String nome;

    public Usuario() {
    }

    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getListaReceitas() {
        return this.listaReceitas;
    }

    public void setListaReceitas(List<String> listaReceitas) {
        this.listaReceitas = listaReceitas;
    }
}

