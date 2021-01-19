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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.leticiafraga.receitas.ConfigFirebase;
import com.leticiafraga.receitas.R;
import com.leticiafraga.receitas.activity.CadastroActivity;
import com.leticiafraga.receitas.activity.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private Button btAcessar;
    private EditText editEmail, editSenha;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);


        final FirebaseAuth auth = ConfigFirebase.getAuth();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        inicializar();
        btAcessar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();
                if (email.isEmpty() || senha.isEmpty()) {
                    criarToast("Preencha todos os campos corretamente!");
                } else {
                    auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                criarToast("Login realizado!");
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }else{
                                String msgErro;
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException e) {
                                    msgErro = "E-mail ou senha inválidos!";
                                } catch (Exception e) {
                                    msgErro = e.getMessage();
                                    e.printStackTrace();
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
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btAcessar = findViewById(R.id.buttonAcessar);
    }

    private void criarToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void cadastrar(View view) {
        startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
        finish();
    }
}

