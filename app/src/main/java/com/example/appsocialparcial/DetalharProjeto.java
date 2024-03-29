package com.example.appsocialparcial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetalharProjeto extends AppCompatActivity {

    private TextView tvNomeProjeto;
    private TextView tvDescricaoProjeto;
    private ImageView tvUrlProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhar_projeto);

        tvNomeProjeto = findViewById(R.id.tvNomeProjeto);
        tvDescricaoProjeto = findViewById(R.id.tvDescricaoProjeto);
        tvUrlProjeto = findViewById(R.id.idImagemDetalhar);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        //Projeto projeto = (Projeto) dados.getSerializable("projeto");
        ProjetoBundle projeto = (ProjetoBundle) dados.getSerializable("projeto");

        tvNomeProjeto.setText(projeto.getNomeProjeto());
        tvDescricaoProjeto.setText(projeto.getDescricaoProjeto());

        tvUrlProjeto.getLayoutParams().width = 250;
        tvUrlProjeto.getLayoutParams().height = 250;
        //Faz com que a ImageView receba uma URL
        Glide.with(this).load(projeto.getFotoByte()).into(tvUrlProjeto);


    }
}
