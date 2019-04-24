package com.example.apptemvaga.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.apptemvaga.Adapter.VagasAdapter;
import com.example.apptemvaga.Classes.Republica;
import com.example.apptemvaga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TelaPrincipalActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewVagas;

    private List<Republica> vagas;

    private VagasAdapter adapter;

    private DatabaseReference referenciaFirebase;

    private Republica todosRepublica;

    private LinearLayoutManager mLayoutTodosProdutos;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        usuarioLogado();

        autenticacao = FirebaseAuth.getInstance();

        mRecyclerViewVagas = (RecyclerView) findViewById(R.id.recyclerViewTodosOsVagas);

        carregarTodosProdutos();
    }

    private void carregarTodosProdutos(){

        mRecyclerViewVagas.setHasFixedSize(true);

        mLayoutTodosProdutos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        mRecyclerViewVagas.setLayoutManager(mLayoutTodosProdutos);

        vagas = new ArrayList<>();

        referenciaFirebase = FirebaseDatabase.getInstance().getReference();

        referenciaFirebase.child("republicas").orderByChild("endereco").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    todosRepublica = postSnapshot.getValue(Republica.class);

                    vagas.add(todosRepublica);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        adapter = new VagasAdapter(vagas,this);

        mRecyclerViewVagas.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menuPrincipalSair){
            deslogar();
        }else if(id == R.id.menuPrincipalCadVaga){
            Intent intent = new Intent(TelaPrincipalActivity.this, CadastroVagaActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogar(){
        autenticacao.signOut();
        Intent intent = new Intent(TelaPrincipalActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean usuarioLogado() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }


}
