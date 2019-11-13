package com.example.appsocialparcial;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditarVoluntarioActivity extends AppCompatActivity {

    private EditText editarEmail;
    private EditText editarSenha;
    private EditText editarNome;
    private Button btnSalvarEditarVoluntario;
    private Button btnCancelarEditarVoluntario;
    private SQLiteDatabase bancoDados;
    private String idVoluntario;
    private Integer idVoluntarioInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_voluntario);

        editarEmail = findViewById(R.id.idEmailEditarVoluntari);
        editarSenha = findViewById(R.id.idSenhaEditarVoluntario);
        editarNome = findViewById(R.id.idNomeEditarVoluntario);
        btnSalvarEditarVoluntario = findViewById(R.id.idBtnSalvarEditarVoluntario);
        btnCancelarEditarVoluntario = findViewById(R.id.idBtnCancelarEditarVoluntario);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        idVoluntario = dados.getString("id_usuario");
        idVoluntarioInt = Integer.parseInt(idVoluntario);
        bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);

        String sql = "SELECT * FROM voluntarios WHERE _idVoluntario = " + idVoluntarioInt;
        Cursor cursor = bancoDados.rawQuery(sql, null);
        cursor.moveToFirst();

        int indiceNome = cursor.getColumnIndex("nome");
        int indiceEmail = cursor.getColumnIndex("email");
        int indiceSenha = cursor.getColumnIndex("senha");

        editarNome.setText(cursor.getString(indiceNome));
        editarEmail.setText(cursor.getString(indiceEmail));
        editarSenha.setText(cursor.getString(indiceSenha));
        cursor.close();
        btnCancelarEditarVoluntario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSalvarEditarVoluntario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String sqlUpdate = "UPDATE voluntarios SET nome = " + editarNome.getText().toString() + ", email = " + editarEmail.getText().toString() + ", senha= "+ editarSenha.getText().toString() + " WHERE _id = " + idVoluntarioInt;

                //String sqlUpdate = "UPDATE voluntarios SET nome = "+ editarNome.getText().toString() + " WHERE _idVoluntario = "+ idVoluntarioInt;
                //bancoDados.execSQL(sqlUpdate);

                boolean isOk = validarCampos();

                if(!isOk){
                    ContentValues values = new ContentValues();
                    values.put("nome", editarNome.getText().toString());
                    values.put("email", editarEmail.getText().toString());
                    values.put("senha", editarSenha.getText().toString());
                    String[] args = {idVoluntarioInt.toString()};
                    bancoDados.update("voluntarios", values, "_idVoluntario=?", args);
                    bancoDados.close();
                    finish();
                }

               // Intent intent = new Intent(getApplicationContext(), SelecaoActivity.class);

               // startActivity(intent);

            }
        });


    }

    private boolean validarCampos(){


        boolean res = false;
        String nome = editarNome.getText().toString();
        String email = editarEmail.getText().toString();
        String senha = editarSenha.getText().toString();

        if(res = isCampoVazio(nome)){
            editarNome.requestFocus();
        }else if(res = !isEmailVaido(email)){
            editarEmail.requestFocus();
        }else if(res = isCampoVazio(senha)){
            editarSenha.requestFocus();
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

    private boolean isEmailVaido(String email){
        boolean resultado = (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());

        return  resultado;
    }
}
