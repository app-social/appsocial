package com.example.appsocialparcial;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class EditarProjetoSocialActivity extends AppCompatActivity {

    private EditText editarDescricao;
    //private EditText editarUrl;
    private EditText editarNome;
    private Button btnSalvarEditarProjeto;
    private Button btnCancelarEditarProjeto;
    private Button btnSelecionarImagem;
    private SQLiteDatabase bancoDados;
    private String idVoluntario;
    private Integer idVoluntarioInt;
    private ImageView imageViewFoto;
    private Bitmap thumbnail;
    private static final int SELECT_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_projeto);

        editarDescricao = findViewById(R.id.tvEditarDescricaoProjeto);
        //editarUrl = findViewById(R.id.idEditarProjetoURL);
        editarNome = findViewById(R.id.idEditarNomeProjeto);
        imageViewFoto = findViewById(R.id.imageViewFotoEditar);
        btnSalvarEditarProjeto = findViewById(R.id.idBtnSalvarEditarProjeto);
        btnSelecionarImagem = findViewById(R.id.btnEditarImagemProjeto);
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
        byte[]fotoBanco = cursor.getBlob(cursor.getColumnIndex("foto"));
        ByteArrayInputStream imageStream = new ByteArrayInputStream(fotoBanco);
        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);


        editarNome.setText(cursor.getString(indiceNomeProjeto));
        editarDescricao.setText(cursor.getString(indiceDescricaoProjeto));
        //editarUrl.setText(cursor.getString(indiceUrl));
        imageViewFoto.setImageBitmap(imageBitmap);
        imageViewFoto.getLayoutParams().width = 150;
        imageViewFoto.getLayoutParams().height = 150;
        cursor.close();

        btnCancelarEditarProjeto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSelecionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it, SELECT_PHOTO);
            }
        });

        btnSalvarEditarProjeto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                boolean isOk = validarCampos();

                if(!isOk){
                    ContentValues values = new ContentValues();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if(thumbnail != null){
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    }
                    byte[] data = stream.toByteArray();
                    values.put("foto", data);
                    values.put("nome", editarNome.getText().toString());
                    values.put("descricao", editarDescricao.getText().toString());
                    //values.put("url", editarUrl.getText().toString());
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
        //String url = editarUrl.getText().toString();

        if(res = isCampoVazio(nome)){
            editarNome.requestFocus();
        }else if(res = isCampoVazio(descricao)){
            editarDescricao.requestFocus();
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

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode== SELECT_PHOTO) {
            Uri selectedImage= data.getData();
            String[] filePath= { MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
            c.moveToFirst();
            int columnIndex= c.getColumnIndex(filePath[0]);
            String picturePath= c.getString(columnIndex);
            c.close();
            thumbnail = (BitmapFactory.decodeFile(picturePath));
            imageViewFoto.setImageBitmap(thumbnail);
        }
    }

}
