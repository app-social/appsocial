package com.example.appsocialparcial;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                ContentValues values = new ContentValues();
                values.put("nome", editarNome.getText().toString());
                values.put("email", editarEmail.getText().toString());
                values.put("senha", editarSenha.getText().toString());
                String[] args = {idVoluntarioInt.toString()};
                bancoDados.update("voluntarios", values, "_idVoluntario=?", args);

               // Intent intent = new Intent(getApplicationContext(), SelecaoActivity.class);

               // startActivity(intent);
                finish();
            }
        });


    }
}
