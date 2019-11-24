package com.example.appsocialparcial;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ListarProjetoActivity extends AppCompatActivity implements RecycleViewOnClickListenerHack{

    private ListView lista;
    private String[] projetos = new String[12];
    private String[] perfil = new String[12];
    public Button cadastrarVoluntario;
    private SQLiteDatabase bancoDados;

    RecyclerView mRecyclerView;
    private LineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_projeto);

        bancoDados = openOrCreateDatabase("cadastroVoluntariosProjetos", MODE_PRIVATE, null);
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS voluntarios(_idVoluntario INTEGER PRIMARY KEY AUTOINCREMENT,nome VARCHAR NOT NULL, email VARCHAR NOT NULL, senha VARCHAR NOT NULL)");
        String  sql = "CREATE TABLE IF NOT EXISTS projetos(_idProjeto INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR NOT NULL,descricao VARCHAR NOT NULL, url VARCHAR, foto BLOB, voluntario_id INTERGER)";

       // String sql = "CREATE TABLE IF NOT EXISTS projetos(_idProjeto INTEGER PRIMARY KEY AUTOINCREMENT, " +
               // "nome VARCHAR NOT NULL,descricao VARCHAR NOT NULL, imagename BLOB NOT NULL, voluntario_id INTERGER)";
        bancoDados.execSQL(sql);
        cadastrarVoluntario = findViewById((R.id.idCadastrarVoluntario));
        mRecyclerView = findViewById(R.id.recyclerView);

        setupRecycler();


        cadastrarVoluntario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListarProjetoActivity.this, MainActivity.class);
                startActivity(it);
            }
        });


        bancoDados.close();
    }



    private List<Projeto> gerarProjetos() {


       // String query = "SELECT * FROM " + projetos;
        Cursor cursor = bancoDados.rawQuery("SELECT * from projetos", null);
        List<Projeto> projetos = new ArrayList<>();

        if((cursor != null) && (cursor.getCount() > 0)){
            while (cursor.moveToNext()){
                Projeto projeto = new Projeto();
                projeto.setUrlImage(cursor.getString(cursor.getColumnIndex("url")));
                byte[]fotoBanco = cursor.getBlob(cursor.getColumnIndex("foto"));
                ByteArrayInputStream imageStream = new ByteArrayInputStream(fotoBanco);
                Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                projeto.setFotoBit(imageBitmap);
                projeto.setNomeProjeto(cursor.getString(cursor.getColumnIndex("nome")));
                projeto.setDescricaoProjeto(cursor.getString(cursor.getColumnIndex("descricao")));
                projetos.add(projeto);
            }
        }else{
            Projeto projeto = new Projeto();
            projeto.setUrlImage("https://imgur.com/ZAN0a0b.jpg");
            projeto.setNomeProjeto("Amor");
            projeto.setDescricaoProjeto("Venha fazer parte desta família!");

            Projeto p1 = new Projeto();
            p1.setNomeProjeto("Pão Nosso de Cada Noite");
            p1.setUrlImage("https://i.imgur.com/xArBRpb.jpg");

            Projeto p2 = new Projeto();
            p2.setNomeProjeto("Ajudar PE");
            p2.setUrlImage("https://imgur.com/iyCGQ9m.jpg");

            Projeto p3 = new Projeto();
            p3.setNomeProjeto("União");
            p3.setUrlImage("https://imgur.com/nzcioLu.jpg");

            Projeto p4 = new Projeto();
            p4.setNomeProjeto("Amor");
            p4.setUrlImage("https://imgur.com/ZAN0a0b.jpg");

            Projeto p5 = new Projeto();
            p5.setNomeProjeto("Gratos");
            p5.setUrlImage("https://imgur.com/j1vvasa.jpg");

            Projeto p6 = new Projeto();
            p6.setNomeProjeto("Arte de amar");
            p6.setUrlImage("https://imgur.com/mY3p8Or.jpg");

            Projeto p7 = new Projeto();
            p7.setNomeProjeto("Herdeiros");
            p7.setUrlImage("https://imgur.com/NOY068Z.jpg");
            projetos.add(projeto);
            projetos.add(p1);
            projetos.add(p2);
            projetos.add(p3);
            projetos.add(p4);
            projetos.add(p5);
            projetos.add(p6);
            projetos.add(p7);
        }


        cursor.close();
        /*Projeto p1 = new Projeto();
        p1.setNomeProjeto("Pão Nosso de Cada Noite");
        p1.setUrlImage("https://i.imgur.com/xArBRpb.jpg");

        Projeto p2 = new Projeto();
        p2.setNomeProjeto("Ajudar PE");
        p2.setUrlImage("https://imgur.com/iyCGQ9m.jpg");

        Projeto p3 = new Projeto();
        p3.setNomeProjeto("União");
        p3.setUrlImage("https://imgur.com/nzcioLu.jpg");

        Projeto p4 = new Projeto();
        p4.setNomeProjeto("Amor");
        p4.setUrlImage("https://imgur.com/ZAN0a0b.jpg");

        Projeto p5 = new Projeto();
        p5.setNomeProjeto("Gratos");
        p5.setUrlImage("https://imgur.com/j1vvasa.jpg");

        Projeto p6 = new Projeto();
        p6.setNomeProjeto("Arte de amar");
        p6.setUrlImage("https://imgur.com/mY3p8Or.jpg");

        Projeto p7 = new Projeto();
        p7.setNomeProjeto("Herdeiros");
        p7.setUrlImage("https://imgur.com/NOY068Z.jpg");

        //https://imgur.com/WyVyjw9

        return new ArrayList<>(Arrays.asList(p1, p2, p3,p4, p5, p6, p7));*/
        return projetos;
    }

    private void setupRecycler() {

        // Configurando o gerenciador de layout para ser uma lista.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = new LineAdapter(gerarProjetos(), this);
        mAdapter.setmRecycleViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(mAdapter);

        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClickListener(View view, Projeto projeto) {

        // Classe nova: ProjetoBundle
        // Em vez de Bitmap ter o byte[]
        // Converter Projeto para ProjetoBundle (Bitmap -> byte[])
        ProjetoBundle pb = new ProjetoBundle();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        projeto.getFotoBit().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        pb.setDescricaoProjeto(projeto.getDescricaoProjeto());
        pb.setNomeProjeto(projeto.getNomeProjeto());
        pb.setFotoByte(byteArray);

        Bundle bundle = new Bundle();
        bundle.putSerializable("projeto", pb);

        Intent intent = new Intent(this, DetalharProjeto.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
