package com.example.appsocialparcial;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SelecaoActivity extends AppCompatActivity {

    private Button adicionarProjeto;
    private Button editarPerfil;
    private Button editarProjeto;
    private ImageView sair;
    private String idVoluntario;
    private SQLiteDatabase bancoDados;
    private Integer idVoluntarioInt;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        //verificarUser();

        setContentView(R.layout.activity_selecao);

        adicionarProjeto = findViewById(R.id.idBtnAdicionarProjeto);
        editarPerfil = findViewById(R.id.idBtnEditarVoluntario);
        editarProjeto = findViewById(R.id.idBtnEditarProjeto);
        sair = findViewById(R.id.idSairLogin);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(SelecaoActivity.this, ListarProjetoActivity.class);
                startActivity(it);
                finish();
            }
        });

        editarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);
                Intent intent = getIntent();
                Bundle dados = intent.getExtras();
                idVoluntario = dados.getString("id_usuario");

                Intent it = new Intent(SelecaoActivity.this, EditarVoluntarioActivity.class);
                Bundle parametros = new Bundle();
                parametros.putString("id_usuario", idVoluntario);
                it.putExtras(parametros);
                startActivity(it);

            }
        });

        adicionarProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);
                Intent intent = getIntent();
                Bundle dados = intent.getExtras();
                idVoluntario = dados.getString("id_usuario");
                idVoluntarioInt = Integer.parseInt(idVoluntario);

                String sql = "SELECT * FROM projetos WHERE voluntario_id = " + idVoluntarioInt;
                Cursor cursor = bancoDados.rawQuery(sql, null);
                cursor.moveToFirst();

                if(cursor.getCount() > 0){
                    Toast.makeText(getApplicationContext(), "Usuário já possui um projeto cadastrado!", Toast.LENGTH_LONG).show();

                }else{
                    Intent it = new Intent(SelecaoActivity.this, CadastroProjetoSocialActivity.class);
                    Bundle parametros = new Bundle();
                    parametros.putString("id_usuario", idVoluntario);
                    it.putExtras(parametros);
                    startActivity(it);
                }
            }
        });

        editarProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);
                Intent intent = getIntent();
                Bundle dados = intent.getExtras();
                idVoluntario = dados.getString("id_usuario");
                idVoluntarioInt = Integer.parseInt(idVoluntario);
                String sql = "SELECT * FROM projetos WHERE voluntario_id = " + idVoluntarioInt;
                Cursor cursor = bancoDados.rawQuery(sql, null);
                cursor.moveToFirst();

                if(cursor.getCount() > 0){

                    Intent it = new Intent(SelecaoActivity.this, EditarProjetoSocialActivity.class);
                    Bundle parametros = new Bundle();
                    parametros.putString("id_usuario", idVoluntario);
                    it.putExtras(parametros);
                    startActivity(it);

                }else{

                    Toast.makeText(getApplicationContext(), "Você não possui um projeto cadastrado. Por favor cadastre.", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void verificarUser() {

        if(user == null){
            finish();
        }else{

        }

    }
}
