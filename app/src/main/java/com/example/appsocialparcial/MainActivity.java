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

public class MainActivity extends Activity {


    EditText email;
    EditText senha;
    Button cadastrarLogin;
    Button login;
    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.idEmailEditarVoluntari);
        senha = findViewById(R.id.idSenha);
        cadastrarLogin = findViewById(R.id.idBtnCancelarEditarVoluntario);
        login = findViewById(R.id.idBtnSalvarEditarVoluntario);
        bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailDigitado = email.getText().toString();
                String senhaDigitado = senha.getText().toString();

                Cursor cursor = bancoDados.rawQuery("SELECT * from voluntarios WHERE email = '" + emailDigitado + "' AND senha = '" + senhaDigitado +"'", null);
                cursor.moveToFirst();

                if((cursor != null) && (cursor.getCount() > 0)){
                    //String idUsuario = Integer.toString(cursor.getPosition());
                    int indiceId = cursor.getInt(0);
                    Log.i("Resultado Id: ", Integer.toString(indiceId));

                    Intent it = new Intent(getApplicationContext(), SelecaoActivity.class);
                    Bundle parametros = new Bundle();
                    parametros.putString("id_usuario", Integer.toString(indiceId));
                    it.putExtras(parametros);
                    startActivity(it);
                    cursor.close();
                }else{
                    Toast.makeText(getApplicationContext(), "Usuário não encontrado.", Toast.LENGTH_LONG).show();
                }



            }
        });

        cadastrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, CadastroVoluntarioActivity.class);
                startActivity(it);

            }
        });
    }
}