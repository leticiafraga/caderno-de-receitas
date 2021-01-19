package com.leticiafraga.receitas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterReceitas extends RecyclerView.Adapter<AdapterReceitas.MyViewHolder> {


    private Context c;
    private ArrayList<Receita> mDataset;
    private StorageReference storageRef;

    public AdapterReceitas(ArrayList<Receita> arrayList, Context context) {
        mDataset = arrayList;
        c = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Receita receita = mDataset.get(position);
        holder.textView.setText(receita.getTitulo());
        if (receita.getFoto() != null){
            Glide.with(c).load(receita.getFoto()).into(holder.imageReceita);
        }else{
            holder.imageReceita.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageReceita;

        public MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            imageReceita = view.findViewById(R.id.imageReceita);
        }
    }

}