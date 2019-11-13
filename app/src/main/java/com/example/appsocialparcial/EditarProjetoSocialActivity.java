package com.example.appsocialparcial;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditarProjetoSocialActivity extends AppCompatActivity {

    private EditText editarDescricao;
    private EditText editarUrl;
    private EditText editarNome;
    private Button btnSalvarEditarProjeto;
    private Button btnCancelarEditarProjeto;
    private SQLiteDatabase bancoDados;
    private String idVoluntario;
    private Integer idVoluntarioInt;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_projeto);

        editarDescricao = findViewById(R.id.tvEditarDescricaoProjeto);
        editarUrl = findViewById(R.id.idEditarProjetoURL);
        editarNome = findViewById(R.id.idEditarNomeProjeto);
        btnSalvarEditarProjeto = findViewById(R.id.idBtnSalvarEditarProjeto);
        btnCancelarEditarProjeto = findViewById(R.id.idBtnCancelarEditarProjeto);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        idVoluntario = dados.getString("id_usuario");
        idVoluntarioInt = Integer.parseInt(idVoluntario);
        bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);

        String sql = "SELECT * FROM projetos WHERE voluntario_id = " + idVoluntarioInt;
        Cursor cursor = bancoDados.rawQuery(sql, null);
        cursor.moveToFirst();

        int indiceIdProjeto = cursor.getColumnIndex("_idProjeto");
        int indiceNomeProjeto = cursor.getColumnIndex("nome");
        int indiceDescricaoProjeto = cursor.getColumnIndex("descricao");
        int indiceUrl= cursor.getColumnIndex("url");

        editarNome.setText(cursor.getString(indiceNomeProjeto));
        editarDescricao.setText(cursor.getString(indiceDescricaoProjeto));
        editarUrl.setText(cursor.getString(indiceUrl));
        cursor.close();

        btnCancelarEditarProjeto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSalvarEditarProjeto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                boolean isOk = validarCampos();

                if(!isOk){
                    ContentValues values = new ContentValues();
                    values.put("nome", editarNome.getText().toString());
                    values.put("descricao", editarDescricao.getText().toString());
                    values.put("url", editarUrl.getText().toString());
                    String[] args = {idVoluntarioInt.toString()};
                    bancoDados.update("projetos", values, "voluntario_id=?", args);
                    Toast.makeText(getApplicationContext(), "Alteração realizada com sucesso!", Toast.LENGTH_LONG).show();
                    bancoDados.close();
                    finish();
                }

            }
        });

    }

    private boolean validarCampos(){

        boolean res = false;
        String nome = editarNome.getText().toString();
        String descricao = editarDescricao.getText().toString();
        String url = editarUrl.getText().toString();

        if(res = isCampoVazio(nome)){
            editarNome.requestFocus();
        }else if(res = isCampoVazio(descricao)){
            editarDescricao.requestFocus();
        }else if(res = isCampoVazio(url)){
            editarUrl.requestFocus();
        }

        if(res){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage("Há campo(s) inválido(s) ou em branco.");
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }

        return  res;
    }

    private boolean isCampoVazio(String valor){

        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());

        return resultado;
    }

}
