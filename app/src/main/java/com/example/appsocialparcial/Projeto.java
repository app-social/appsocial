package com.example.appsocialparcial;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Projeto implements Serializable {

    private String urlImage;
    private String nomeProjeto;
    private String descricaoProjeto;
    private Bitmap fotoBit;


    public Bitmap getFotoBit() {
        return fotoBit;
    }

    public void setFotoBit(Bitmap fotoBit) {
        this.fotoBit = fotoBit;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public void setNomeProjeto(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }

    public String getDescricaoProjeto() {
        return descricaoProjeto;
    }
    public void setDescricaoProjeto(String descricaoProjeto) {
        this.descricaoProjeto = descricaoProjeto;
    }

}
