package com.example.appsocialparcial;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroVoluntarioActivity extends Activity {

    private Button btnSalvar;
    private Button btnCancelar;
    private EditText nomeCadastro;
    private EditText emailCadasro;
    private EditText senhaCadastro;
    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_voluntario);

        btnSalvar = findViewById(R.id.idBtnSalvarEditarVoluntario);
        btnCancelar = findViewById(R.id.idBtnCancelarEditarVoluntario);
        nomeCadastro = findViewById(R.id.idNomeEditarVoluntario);
        emailCadasro = findViewById(R.id.idEmailEditarVoluntari);
        senhaCadastro = findViewById(R.id.idSenha);
        bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);
        //criar tabela
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS voluntarios(_idVoluntario INTEGER PRIMARY KEY AUTOINCREMENT,nome VARCHAR NOT NULL, email VARCHAR NOT NULL, senha VARCHAR NOT NULL)");



        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(CadastroVoluntarioActivity.this, ListarProjetoActivity.class);
                startActivity(it);
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String nomeDigitado = nomeCadastro.getText().toString();
                String emailDigitado = emailCadasro.getText().toString();
                String senhaDigitado = senhaCadastro.getText().toString();

                //inserir dados
                bancoDados.execSQL("INSERT INTO  voluntarios (nome, email, senha) VALUES ('"+ nomeDigitado +"','"+emailDigitado+ "','"+senhaDigitado+"')");
                Cursor cursor = bancoDados.rawQuery("SELECT nome from voluntarios", null);

                int indiceNome = cursor.getColumnIndex("nome");
                cursor.moveToFirst();
                Log.i("Resultado Nome: ", cursor.getString(indiceNome));

                cursor.close();

                Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                Intent it = new Intent(CadastroVoluntarioActivity.this, MainActivity.class);
                startActivity(it);


            }
        });

    }
}