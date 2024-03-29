package com.example.appsocialparcial;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.LineHolder> implements Filterable {

    private final List<Projeto> mProjetos;
    private final List<Projeto> todosProjetos;
    private final Activity mActivity;
    private RecycleViewOnClickListenerHack mRecycleViewOnClickListenerHack;

    public LineAdapter(List<Projeto> projetos, Activity activity) {
        mProjetos = projetos;
        mActivity = activity;
        todosProjetos = new ArrayList<>(mProjetos);
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Projeto> projetosFiltrados = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                projetosFiltrados.addAll(todosProjetos);
            }else {
                for(Projeto projeto: todosProjetos){
                    if(projeto.getNomeProjeto().toLowerCase().contains(constraint.toString().toLowerCase())){
                        projetosFiltrados.add(projeto);
                    }
                }
            }


            FilterResults filterResults = new FilterResults();
            filterResults.values = projetosFiltrados;


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mProjetos.clear();
            mProjetos.addAll((Collection<? extends Projeto>) results.values);
            notifyDataSetChanged();
        }
    };

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
