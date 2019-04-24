package com.example.apptemvaga.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.apptemvaga.Activity.DetalhesVaga;
import com.example.apptemvaga.Activity.TelaPrincipalActivity;
import com.example.apptemvaga.Classes.Republica;
import com.example.apptemvaga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VagasAdapter extends RecyclerView.Adapter<VagasAdapter.ViewHolder> {

    List<Republica> mVagasList;
    private Context context;
    private DatabaseReference referenciaFirebase;
    private List<Republica> produtos;
    private Republica todosRepublica;


    public VagasAdapter(List<Republica> l , Context c){
        context = c;
        mVagasList = l;
    }


    @Override
    public VagasAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_vagas,viewGroup,false);
        return new VagasAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VagasAdapter.ViewHolder viewHolder, int position) {
        final Republica item = mVagasList.get(position);

        produtos = new ArrayList<>();

        referenciaFirebase = FirebaseDatabase.getInstance().getReference();

        referenciaFirebase.child("republicas").orderByChild("keyRepublica").equalTo(item.getKeyRepublica()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                produtos.clear();

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    todosRepublica = postSnapshot.getValue(Republica.class);

                    produtos.add(todosRepublica);

                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                    final int height = (displayMetrics.heightPixels/4);
                    final  int width = (displayMetrics.widthPixels/2);
                    Picasso.get().load(todosRepublica.getImagem1()).resize(width,height).centerCrop().into(viewHolder.imagemVagaLista);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.txtViewEndereco.setText("Endereço: "+item.getEndereco());
        viewHolder.txtViewReferencia.setText("Ponto de Referencia: "+item.getReferencia());
        viewHolder.txtViewQuantidade.setText("Quantidade de vagas: "+item.getQuantidadeVagas());
        viewHolder.txtViewTipo.setText("Tipo"+item.getTipo());

        viewHolder.linearLayoutVagaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetalhesVaga.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVagasList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtViewEndereco;
        protected TextView txtViewReferencia;
        protected TextView txtViewQuantidade;
        protected ImageView imagemVagaLista;
        protected LinearLayout linearLayoutVagaLista;
        protected TextView txtViewTipo;

        public ViewHolder(View itemView){
            super(itemView);

            txtViewEndereco = (TextView)itemView.findViewById(R.id.txtViewEndereco);
            txtViewReferencia = (TextView)itemView.findViewById(R.id.txtViewReferencia);
            txtViewQuantidade = (TextView)itemView.findViewById(R.id.txtViewQuantidade);
            txtViewTipo = (TextView)itemView.findViewById(R.id.txtViewTipo);
            imagemVagaLista = (ImageView)itemView.findViewById(R.id.imagemVagaLista);
            linearLayoutVagaLista = (LinearLayout) itemView.findViewById(R.id.layoutListaVagas);

        }

    }

}
