package com.leticiafraga.receitas.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.leticiafraga.receitas.ConfigFirebase;
import com.leticiafraga.receitas.R;
import com.leticiafraga.receitas.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btAcessar;
    private EditText editEmail, editNome, editSenha;
    private String email, nome;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_cadastro);
        inicializar();
        btAcessar.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                auth = ConfigFirebase.getAuth();
                nome = editNome.getText().toString();
                email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();
                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    criarToast("Preencha todos os campos corretamente!");
                } else {
                    auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Usuario usuario = new Usuario();
                                usuario.setEmail(email);
                                usuario.setNome(nome);
                                ConfigFirebase.getFirebase().child(auth.getCurrentUser().getUid()).child("dados").setValue(usuario);
                                criarToast("Sucesso ao cadastrar!");
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();

                            }else{
                                String msgErro;
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    msgErro = "Erro: Senha fraca!";
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    msgErro = "Erro: E-mail inválido!";
                                } catch (FirebaseAuthUserCollisionException e) {
                                    msgErro = "Erro: Um usuário com esse e-mail já existe!";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    msgErro = e.getMessage();
                                }
                                criarToast("Erro: " + msgErro);
                            }

                        }
                    });
                }
            }
        });
    }

    private void inicializar() {
        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btAcessar = findViewById(R.id.buttonAcessar);
    }

    private void criarToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
