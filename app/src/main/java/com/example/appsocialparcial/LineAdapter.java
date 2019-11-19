package com.example.appsocialparcial;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.LineHolder> {

    private final List<Projeto> mProjetos;
    private final Activity mActivity;
    private RecycleViewOnClickListenerHack mRecycleViewOnClickListenerHack;

    public LineAdapter(List<Projeto> projetos, Activity activity) {
        mProjetos = projetos;
        mActivity = activity;
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LineHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false));
    }

    @Override
    public void onBindViewHolder(LineHolder holder, int position) {

        Projeto projeto = mProjetos.get(position);

        Glide.with(mActivity).load(projeto.getFotoBit()).into(holder.imgProjeto);
        holder.tvNomeProjeto.setText(projeto.getNomeProjeto());

        holder.projeto = projeto;

    }

    public void setmRecycleViewOnClickListenerHack(RecycleViewOnClickListenerHack r){
        mRecycleViewOnClickListenerHack = r;
    }

    @Override
    public int getItemCount() {
        return mProjetos != null ? mProjetos.size() : 0;
    }

    public class LineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imgProjeto;
        public TextView tvNomeProjeto;
        public Projeto projeto;

        public LineHolder(View itemView) {
            super(itemView);
            imgProjeto = (ImageView) itemView.findViewById(R.id.imgProjeto);
            tvNomeProjeto = (TextView) itemView.findViewById(R.id.tvNomeProjeto);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mRecycleViewOnClickListenerHack != null){

                mRecycleViewOnClickListenerHack.onClickListener(v, projeto);

            }

        }
    }

}
