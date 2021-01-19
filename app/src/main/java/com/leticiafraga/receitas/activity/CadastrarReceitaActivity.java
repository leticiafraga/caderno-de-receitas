package com.leticiafraga.receitas.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leticiafraga.receitas.ConfigFirebase;
import com.leticiafraga.receitas.R;
import com.leticiafraga.receitas.Receita;

import dmax.dialog.SpotsDialog;

public class CadastrarReceitaActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button btSalvar;
    private EditText campoDesc, campoFonte, campoIngr, campoTitulo;
    private ImageView imagemReceita;
    private Receita receitaEditar;
    private String caminhoImagem;
    private StorageReference storage = ConfigFirebase.getStorage();
    private StorageReference referenceImagem;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_cadastrar_receita);
        campoTitulo = findViewById(R.id.editTitulo);
        campoIngr = findViewById(R.id.editIngr);
        campoDesc = findViewById(R.id.editDesc);
        campoFonte = findViewById(R.id.editFonte);
        btSalvar = findViewById(R.id.btSalvar);
        imagemReceita = findViewById(R.id.imageCadastrarReceita);

        carregarReceitaEditar();

        imagemReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherImagem();
            }
        });

        btSalvar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                salvarReceita();
            }
        });
    }

    public void carregarReceitaEditar(){
        receitaEditar = (Receita) getIntent().getSerializableExtra("receitaEditar");

        if (receitaEditar != null) {

            campoTitulo.setText(receitaEditar.getTitulo());
            campoIngr.setText(this.receitaEditar.getIngredientes());
            campoDesc.setText(this.receitaEditar.getPreparo());
            campoFonte.setText(this.receitaEditar.getFonte());
            if (receitaEditar.getFoto() != null && receitaEditar.getFoto().equals("")){
                Glide.with(this).load(receitaEditar.getFoto()).into(imagemReceita);

            }
        }
    }

    public void salvarReceita(){

        String titulo = campoTitulo.getText().toString();
        String ingredientes = campoIngr.getText().toString();
        if (!titulo.isEmpty()) {
            Receita receita = new Receita();
            receita.setTitulo(titulo);
            receita.setPreparo(campoDesc.getText().toString());
            receita.setIngredientes(ingredientes);
            receita.setFonte(campoFonte.getText().toString());
            if (caminhoImagem != null){
                receita.setFoto(caminhoImagem);
                salvarImagemStorage(caminhoImagem, receita);
            }else{
                receita.salvar();
                if (receitaEditar != null) {
                    receitaEditar.remover();
                }
                finish();
            }

        }else{
            criarToast("Preencha pelo menos o título!");
        }

    }

    public void escolherImagem(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Uri imagemSelecionada = data.getData();
            Glide.with(getApplicationContext()).load(imagemSelecionada).into(imagemReceita);
            caminhoImagem = imagemSelecionada.toString();
            //imagemReceita.setImageURI(imagemSelecionada);
        }
    }

    public void salvarImagemStorage(String url, final Receita receita){
        referenceImagem = storage.child("images").child(receita.getIdReceita());

        final AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Carregando").setCancelable(false).build();
        dialog.show();

        UploadTask uploadTask = referenceImagem.putFile(Uri.parse(url));
        /*uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               dialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });*/
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return referenceImagem.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    criarToast("Sucesso");

                    if (downloadUri != null) {

                        String urlImagem = downloadUri.toString();
                        receita.setFoto(urlImagem);
                        receita.salvar();
                        if (receitaEditar != null) {
                            receitaEditar.remover();
                        }
                        finish();

                    }

                } else {

                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        criarToast("Erro: " + e.getMessage());
                    }
                }
                dialog.dismiss();
            }
        });

    }

    public void criarToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
