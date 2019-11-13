package com.example.appsocialparcial;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;


public class CadastroProjetoSocialActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Button btnSalvar;
    private Button btnCancelar;
    private Button btnSelecionarImagem;
    private ImageView imageViewProeto;
    private EditText nomeProjeto;
    private EditText descricaoProjeto;
    private EditText urlProjeto;
    private SQLiteDatabase bancoDados;
    private String idVoluntario;
    private Integer idVoluntarioInt;

    private ImageView profileImageView;
    private Button pickImage;

    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;

    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private boolean hasImageChanged = false;
    DbHelper dbHelper;

    Bitmap thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_projeto_social);

        btnSalvar = findViewById(R.id.idBtnSalvarEditarVoluntario);
        btnCancelar = findViewById(R.id.idBtnCancelarEditarVoluntario);
        nomeProjeto = findViewById(R.id.idNomeProjeto);
        descricaoProjeto = findViewById(R.id.tvDescricaoProjeto);
        urlProjeto = findViewById(R.id.idProjetoURL);

        //Trabalhando a imagem
        btnSelecionarImagem = findViewById(R.id.btnSelecionarImagem);
        imageViewProeto = findViewById(R.id.ivImagemProjeto);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        idVoluntario = dados.getString("id_usuario");
        idVoluntarioInt = Integer.parseInt(idVoluntario);
        bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);


        //criar tabela

       //  String  sqlProjetos = "CREATE TABLE IF NOT EXISTS projetos(_idProjeto INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR NOT NULL,descricao VARCHAR NOT NULL, url VARCHAR, voluntario_id INTERGER, FOREIGN KEY(voluntario_id) REFERENCES voluntarios(_idVoluntario))";

        //query que funciona
        //String  sql = "CREATE TABLE IF NOT EXISTS projetos(_idProjeto INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR NOT NULL,descricao VARCHAR NOT NULL, url VARCHAR, voluntario_id INTERGER)";
        String sql = "CREATE TABLE IF NOT EXISTS projetos(_idProjeto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome VARCHAR NOT NULL,descricao VARCHAR NOT NULL, url VARCHAR, voluntario_id INTERGER)";
        bancoDados.execSQL(sql);


        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CAPTURE_PHOTO);
            }
        }


        btnSelecionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it, SELECT_PHOTO);
            }
        });

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
                boolean isOk = validarCampos();

                if(!isOk){
                    String query = "INSERT INTO  projetos (nome, descricao, url,voluntario_id ) VALUES ('"+ nomeDigitado +"','"+descricaoDigitada+ "','"+urlDigitada+"', " + testeId + ")";

                    bancoDados.execSQL(query);
                    Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(CadastroProjetoSocialActivity.this, ListarProjetoActivity.class);
                    startActivity(it);
                    bancoDados.close();
                    finish();
                }

                /*Cursor cursor = bancoDados.rawQuery("SELECT voluntario_id from projetos", null);
                int indiceNome = cursor.getColumnIndex("voluntario_id");
                cursor.moveToFirst();
                Log.i("Resultado Nome ID: ", cursor.getString(indiceNome));
                cursor.close();*/

            }
        });
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
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            imageViewProeto.setImageBitmap(thumbnail);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          String permissions[], int[] grantResults) {
        if(requestCode== CAPTURE_PHOTO) {

            if(grantResults.length> 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // A permissão foi concedida. Pode continuar
            } else{
                // A permissão foi negada. Precisa ver o que deve ser desabilitado
            }
            return;
        }
    }

    public void addToDb(View view){

        profileImageView.setDrawingCacheEnabled(true);
        profileImageView.buildDrawingCache();
        Bitmap bitmap = profileImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        dbHelper.addToDb(data);
        Toast.makeText(this, "Image saved to DB successfully", Toast.LENGTH_SHORT).show();

    }

    private boolean validarCampos(){


        boolean res = false;
        String nome = nomeProjeto.getText().toString();
        String descricao = descricaoProjeto.getText().toString();
        String url = urlProjeto.getText().toString();

        if(res = isCampoVazio(nome)){
            nomeProjeto.requestFocus();
        }else if(res = isCampoVazio(descricao)){
            descricaoProjeto.requestFocus();
        }else if(res = isCampoVazio(url)){
            urlProjeto.requestFocus();
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
