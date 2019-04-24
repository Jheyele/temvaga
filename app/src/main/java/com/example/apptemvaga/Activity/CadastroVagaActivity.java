package com.example.apptemvaga.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.apptemvaga.Classes.Republica;
import com.example.apptemvaga.DAO.ConfiguracaoFirebase;
import com.example.apptemvaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class CadastroVagaActivity extends AppCompatActivity {

    private ImageView imagem;
    private EditText endereco;
    private EditText referencia;
    private EditText quantidadeVagas;
    private RadioButton repF;
    private RadioButton repM;
    private RadioButton repMisto;
    private Button cadVaga;
    private StorageReference storageReference;
    private DatabaseReference referenciaFirebase;
    private FirebaseAuth autenticacao;
    private Republica republica;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_vaga);

        storageReference = ConfiguracaoFirebase.getFirebaseStorageReference();

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        imagem = (ImageView) findViewById(R.id.imagem1);


        endereco = (EditText) findViewById(R.id.endereco);
        referencia = (EditText) findViewById(R.id.pontoReferencia);
        quantidadeVagas = (EditText) findViewById(R.id.quantVagas);
        repF = (RadioButton) findViewById(R.id.repF);
        repM = (RadioButton) findViewById(R.id.repMasc);
        repMisto = (RadioButton) findViewById(R.id.repMista);
        cadVaga = (Button) findViewById(R.id.cadastroVaga);



        imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 123);
            }
        });


        cadVaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarImagem();
            }
        });
    }

    private void cadastrarImagem(){

        StorageReference montaImagemReferencia = storageReference.child("Imagens/"+endereco.getText().toString()+quantidadeVagas.getText().toString()+".jpeg");

        imagem.setDrawingCacheEnabled(true);
        imagem.buildDrawingCache();

        Bitmap bitmap = imagem.getDrawingCache();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        byte[] data = byteArray.toByteArray();

        UploadTask uploadTask = montaImagemReferencia.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                republica = new Republica();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                republica.setImagem1(downloadUrl.toString());
                Toast.makeText(CadastroVagaActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                cadastrarRepublica(republica);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final int heigth = 300;
        final int width = 300;

        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == 123) {
                Uri imagemSelecionada = data.getData();
                Picasso.get().load(imagemSelecionada).resize(width, heigth).centerCrop().into(imagem);
            }
        }
    }




    private boolean cadastrarRepublica(Republica republica){

        if (repF.isChecked()){
            republica.setTipo("Republica Feminina");
        }else if (repM.isChecked()){
            republica.setTipo("Republica Masculina");
        }else if (repMisto.isChecked()){
            republica.setTipo("Republica Mista");
        }

        republica.setEndereco(endereco.getText().toString());
        republica.setReferencia(referencia.getText().toString());
        republica.setQuantidadeVagas(quantidadeVagas.getText().toString());


        try{
            referenciaFirebase = ConfiguracaoFirebase.getFirebase().child("republicas");
            String key = referenciaFirebase.push().getKey();
            republica.setKeyRepublica(key);
            referenciaFirebase.child(key).setValue(republica);
            Toast.makeText(CadastroVagaActivity.this, "Republica cadastrada com sucesso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CadastroVagaActivity.this, TelaPrincipalActivity.class);
            startActivity(intent);
            return true;
        }catch (Exception e){
            Toast.makeText(CadastroVagaActivity.this, "Erro ao cadastrar republica", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }


}
