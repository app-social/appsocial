package com.example.appsocialparcial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CadastroVoluntarioActivity extends Activity {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Button btnSalvar;
    private Button btnCancelar;
    private EditText nomeCadastro;
    private EditText emailCadasro;
    private EditText senhaCadastro;
    private SQLiteDatabase bancoDados;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        auth = Conexao.getFirebaseAuth();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_voluntario);
        db = FirebaseFirestore.getInstance();
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
                String nomeDigitado = nomeCadastro.getText().toString().trim();
                String emailDigitado = emailCadasro.getText().toString().trim();
                String senhaDigitado = senhaCadastro.getText().toString().trim();
                boolean isOk = validarCampos();
                boolean isEmailExistente = isUsuarioCadastrado(emailDigitado);
                if(!isOk){
                    if(!isEmailExistente){
                        //inserir dados
                        bancoDados.execSQL("INSERT INTO  voluntarios (nome, email, senha) VALUES ('"+ nomeDigitado +"','"+emailDigitado+ "','"+senhaDigitado+"')");
                        Cursor cursor = bancoDados.rawQuery("SELECT nome from voluntarios", null);

                        int indiceNome = cursor.getColumnIndex("nome");
                        cursor.moveToFirst();
                        Log.i("Resultado Nome: ", cursor.getString(indiceNome));

                        cursor.close();

                        // criarUsuario(nomeDigitado, emailDigitado, senhaDigitado);
                        bancoDados.close();
                        Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                        Intent it = new Intent(CadastroVoluntarioActivity.this, MainActivity.class);
                        startActivity(it);
                    }else {
                        Toast.makeText(getApplicationContext(), "Email de cadastro já existente!", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }




    private void criarUsuario(String nomeDigitado,String emailDigitado,String senhaDigitado){
          Map<String, Object> user = new HashMap<>();
          user.put("nome", nomeDigitado);
          user.put("email", emailDigitado);
          user.put("senha", senhaDigitado);

           db.collection("voluntarios")
          .document(UUID.randomUUID().toString())
         .set(user)
         .addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
               Intent it = new Intent(CadastroVoluntarioActivity.this, MainActivity.class);
               startActivity(it);
           }
         })
         .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
             Toast.makeText(getApplicationContext(), "Não foi possível realizar o cadastro!", Toast.LENGTH_LONG).show();
              Intent it = new Intent(CadastroVoluntarioActivity.this, MainActivity.class);
              startActivity(it);
          }
            });
    }

    private void criarUser(String nome, String email, String senha){
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadastroVoluntarioActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                            Intent it = new Intent(CadastroVoluntarioActivity.this, MainActivity.class);
                            startActivity(it);
                        }else{
                            Toast.makeText(getApplicationContext(), "Não foi possível realizar o cadastro!", Toast.LENGTH_LONG).show();
                            Intent it = new Intent(CadastroVoluntarioActivity.this, MainActivity.class);
                            startActivity(it);
                        }

                    }
                });
    }


    private boolean validarCampos(){

        boolean res = false;
        String nome = nomeCadastro.getText().toString();
        String email = emailCadasro.getText().toString();
        String senha = senhaCadastro.getText().toString();

        if(res = isCampoVazio(nome)){
            nomeCadastro.requestFocus();
        }else if(res = !isEmailVaido(email)){
            emailCadasro.requestFocus();
        }else if(res = isCampoVazio(senha)){
            senhaCadastro.requestFocus();
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


    private boolean isUsuarioCadastrado(String email){
        String emailExistente = "";
        bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);
        //criar tabela
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS voluntarios(_idVoluntario INTEGER PRIMARY KEY AUTOINCREMENT,nome VARCHAR NOT NULL, email VARCHAR NOT NULL, senha VARCHAR NOT NULL)");
        Cursor cursor = bancoDados.rawQuery("select * from  voluntarios  where email = ? ", new String[]{email});
        cursor.moveToFirst();

        int indiceEmail = cursor.getColumnIndex("email");

        if((cursor != null) && (cursor.getCount() > 0)){
            emailExistente = cursor.getString(indiceEmail);
        }else{
            cursor.close();
            return false;
        }


        if(!email.trim().equals(emailExistente)){
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }

    }
}