package com.leticiafraga.receitas.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.leticiafraga.receitas.ConfigFirebase;
import com.leticiafraga.receitas.R;
import com.leticiafraga.receitas.Usuario;

public class PerfilActivity extends AppCompatActivity {

    private Button btNome, btSenha, btConfirmarNome, btCancelarNome, btConfirmarSenha, btCancelarSenha;
    private String nomeUser;
    private TextView textOla;
    private FirebaseUser user;
    private Usuario usuario;
    private EditText editNome, editSenhaAtual, editSenhaNova;
    private LinearLayout layoutNome, layoutSenha;
    private DatabaseReference db;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_perfil);
        recuperarUser();
        inicializar();


        btNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutNome.setVisibility(View.VISIBLE);
                btCancelarNome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutNome.setVisibility(View.GONE);
                    }
                });
                btConfirmarNome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.child("nome").setValue(editNome.getText().toString());
                        layoutNome.setVisibility(View.GONE);
                    }
                });


            }
        });
        btSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutSenha.setVisibility(View.VISIBLE);
                btCancelarSenha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutSenha.setVisibility(View.GONE);
                    }
                });
                btConfirmarSenha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alterarSenha();
                        layoutSenha.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
    private void inicializar(){
        btNome = findViewById(R.id.btNome);
        btSenha = findViewById(R.id.btSenha);
        textOla = findViewById(R.id.textOla);
        editNome = findViewById(R.id.editAlterarNome);
        editNome.setText(nomeUser);
        editSenhaAtual = findViewById(R.id.editSenhaAtual);
        editSenhaNova = findViewById(R.id.editSenhaNova);
        layoutNome = findViewById(R.id.layoutNome);
        layoutSenha = findViewById(R.id.layoutSenha);
        btCancelarNome = findViewById(R.id.btCancelarNome);
        btCancelarSenha = findViewById(R.id.btCancelarSenha);
        btConfirmarNome = findViewById(R.id.btConfirmarNome);
        btConfirmarSenha = findViewById(R.id.btConfirmarSenha);

    }

    private void recuperarUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = ConfigFirebase.getFirebase().child(user.getUid()).child("dados");

        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                nomeUser = usuario.getNome();
                textOla.setText("Olá, " + nomeUser + "!");
                editNome.setText(nomeUser);
            }
        });

    }
    private void alterarSenha(){


        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = usuario.getEmail();
        String senha = editSenhaAtual.getText().toString();
        final String senhaNova = editSenhaNova.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(email, senha);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(senhaNova).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                String msgErro;
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    msgErro = e.getMessage();
                                    e.printStackTrace();
                                }
                                criarToast("Erro: " + msgErro);
                            }else {
                                criarToast("Senha alterada com sucesso!");
                            }
                            editSenhaAtual.setText("");
                            editSenhaNova.setText("");
                        }
                    });
                }else {
                    String msgErro;
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        msgErro = e.getMessage();
                        e.printStackTrace();
                    }
                    criarToast("Erro na autenticação: " + msgErro);
                }
            }
        });
    }

    public void criarToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
