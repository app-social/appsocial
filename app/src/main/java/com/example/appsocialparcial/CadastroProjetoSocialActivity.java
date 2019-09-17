package com.example.appsocialparcial;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CadastroProjetoSocialActivity extends AppCompatActivity {

    private Button btnSalvar;
    private Button btnCancelar;
    private EditText nomeProjeto;
    private EditText descricaoProjeto;
    private EditText urlProjeto;
    private SQLiteDatabase bancoDados;
    private String idVoluntario;
    private Integer idVoluntarioInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_projeto_social);

        btnSalvar = findViewById(R.id.idBtnSalvarEditarVoluntario);
        btnCancelar = findViewById(R.id.idBtnCancelarEditarVoluntario);
        nomeProjeto = findViewById(R.id.idNomeProjeto);
        descricaoProjeto = findViewById(R.id.tvDescricaoProjeto);
        urlProjeto = findViewById(R.id.idProjetoURL);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        idVoluntario = dados.getString("id_usuario");
        idVoluntarioInt = Integer.parseInt(idVoluntario);
        bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);


        //criar tabela

        // String  sql = "CREATE TABLE IF NOT EXISTS projetos(_idProjeto INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR NOT NULL,descricao VARCHAR NOT NULL, url VARCHAR, voluntario_id INTERGER, FOREIGN KEY(voluntario_id) REFERENCES voluntarios(_idVoluntario))";
        String  sql = "CREATE TABLE IF NOT EXISTS projetos(_idProjeto INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR NOT NULL,descricao VARCHAR NOT NULL, url VARCHAR, voluntario_id INTERGER)";
        bancoDados.execSQL(sql);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(CadastroProjetoSocialActivity.this, ListarProjetoActivity.class);
                startActivity(it);
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String nomeDigitado = nomeProjeto.getText().toString();
                String descricaoDigitada = descricaoProjeto.getText().toString();
                String urlDigitada = urlProjeto.getText().toString();
                idVoluntarioInt = Integer.parseInt(idVoluntario);
                int testeId = idVoluntarioInt;


                //inserir dados

                    String query = "INSERT INTO  projetos (nome, descricao, url,voluntario_id ) VALUES ('"+ nomeDigitado +"','"+descricaoDigitada+ "','"+urlDigitada+"', " + testeId + ")";

                    bancoDados.execSQL(query);
                    Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(CadastroProjetoSocialActivity.this, ListarProjetoActivity.class);
                    startActivity(it);
                finish();



                /*Cursor cursor = bancoDados.rawQuery("SELECT voluntario_id from projetos", null);
                int indiceNome = cursor.getColumnIndex("voluntario_id");
                cursor.moveToFirst();
                Log.i("Resultado Nome ID: ", cursor.getString(indiceNome));
                cursor.close();*/




            }
        });
    }
}
