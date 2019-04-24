package com.example.apptemvaga.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.apptemvaga.DAO.ConfiguracaoFirebase;
import com.example.apptemvaga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button btnLogin;
    private TextView link;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        link = (TextView) findViewById(R.id.linkCadUser);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email.getText().toString().equals("") || senha.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Digite o email e senha", Toast.LENGTH_SHORT).show();
                }else {
                    validarLogin();
                }
            }
        });

    }

    public void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.signInWithEmailAndPassword(email.getText().toString(), senha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this,TelaPrincipalActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(MainActivity.this, "Email e Senha invalidos.Tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
