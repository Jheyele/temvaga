package com.example.apptemvaga.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apptemvaga.Classes.Usuario;
import com.example.apptemvaga.DAO.ConfiguracaoFirebase;
import com.example.apptemvaga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText telefone;
    private EditText email;
    private EditText senha;
    private EditText senha2;
    private Button cad;
    private Usuario user;
    private FirebaseAuth autenticacao;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = (EditText) findViewById(R.id.nomeUser);
        telefone = (EditText) findViewById(R.id.telefoneUser);
        email = (EditText) findViewById(R.id.emailUser);
        senha = (EditText) findViewById(R.id.senhaUser);
        senha2 = (EditText) findViewById(R.id.senha2);
        cad = (Button) findViewById(R.id.cadUser);

        cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(senha.getText().toString().equals("") || email.getText().toString().equals("") || nome.getText().toString().equals("") || telefone.getText().toString().equals(""))) {
                    if (senha.getText().toString().equals(senha2.getText().toString())) {

                        user = new Usuario();
                        user.setNome(nome.getText().toString());
                        user.setTelefone(telefone.getText().toString());
                        user.setEmail(email.getText().toString());
                        user.setSenha(senha.getText().toString());

                        cadastrarUsuario(user);

                    } else {
                        Toast.makeText(CadastroUsuarioActivity.this, "As senhas não se correspondem", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroUsuarioActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean cadastrarUsuario(final Usuario user){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(user.getEmail(), user.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    insereUser(user);
                    finish();
                }else{
                    String erroExecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExecao = "Digite uma senha mais forte, com no minimo 8 caracteres e cotenha letras e numeros";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExecao = "Email invalido, digite corretamente.";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExecao = "Esse email já esta cadastrado.";
                    }catch (Exception e){
                        erroExecao = "Erro ao efetuar cadastro.";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro" + erroExecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;
    }

    public boolean insereUser(Usuario user){
        try{
            reference = ConfiguracaoFirebase.getFirebase().child("usuarios");
            String key = reference.push().getKey();
            user.setKeyUser(key);
            reference.child(key).setValue(user);
            finish();
            Intent intent = new Intent(CadastroUsuarioActivity.this, TelaPrincipalActivity.class);
            startActivity(intent);
            Toast.makeText(CadastroUsuarioActivity.this, "Usuario cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception e){
            Toast.makeText(CadastroUsuarioActivity.this, "Erro ao gravar usuario", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

}
