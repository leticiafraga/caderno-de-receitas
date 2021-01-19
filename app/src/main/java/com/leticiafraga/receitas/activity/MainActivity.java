package com.leticiafraga.receitas.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.leticiafraga.receitas.AdapterReceitas;
import com.leticiafraga.receitas.ConfigFirebase;
import com.leticiafraga.receitas.R;
import com.leticiafraga.receitas.Receita;
import com.leticiafraga.receitas.RecyclerItemClickListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    private AdapterReceitas adapter;
    private FirebaseAuth auth = ConfigFirebase.getAuth();
    private Receita rSelecionada;
    private List<Receita> receitas = new ArrayList();
    private RecyclerView recyclerAnuncios;
    private DatabaseReference ref;
    private StorageReference storageRef;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ref = ConfigFirebase.getFirebase().child(ConfigFirebase.getIdUsuario()).child("receitas");
        storageRef = ConfigFirebase.getStorage().child("images");

        inicializarRecycler();
        recuperarReceitas();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CadastrarReceitaActivity.class));
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.menu_conta) {
            startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
        } else if (itemId == R.id.menu_sair) {
            auth.signOut();
            startActivity(new Intent(this, SplashActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void inicializarRecycler(){

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerAnuncios = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnuncios.setHasFixedSize(true);
        AdapterReceitas adapterReceitas = new AdapterReceitas((ArrayList<Receita>) receitas, this);
        adapter = adapterReceitas;
        recyclerAnuncios.setAdapter(adapterReceitas);
        recyclerAnuncios.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerAnuncios,
                new RecyclerItemClickListener.OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    }

                    @Override
                    public void onItemClick(View view, int i) {
                        Intent intent = new Intent(MainActivity.this, DetalhesActivity.class);
                        intent.putExtra("receitaSelecionada", receitas.get(i));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int i) {
                        rSelecionada = receitas.get(i);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Certeza de que quer excluir?");
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogInterface, int i) {
                                rSelecionada.remover();
                                recuperarReceitas();
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }));

    }

    private void recuperarReceitas() {
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Carregando").setCancelable(false).build();
        dialog.show();

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                receitas.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    receitas.add(ds.getValue(Receita.class));
                }
                Collections.reverse(receitas);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }
}