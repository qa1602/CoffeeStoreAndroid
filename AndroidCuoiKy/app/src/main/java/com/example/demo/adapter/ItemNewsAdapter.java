package com.example.demo.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.model.ItemNews;
import com.example.demo.R;

import java.util.List;

public class ItemNewsAdapter extends RecyclerView.Adapter<ItemNewsAdapter.ViewHolder> {

    private Context context;
    private List<ItemNews> list;

    public ItemNewsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ItemNews> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_item_news,
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemNews item = list.get(position);
        if (item == null) {
            return;
        }
        holder.imageView.setImageResource(item.getResourceImage());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(
                        context,
                        item.getName(),
                        Toast.LENGTH_SHORT
                ).show();

            }
        });
        holder.textView.setText(item.getName());
        holder.tvDetail.setText(item.getDetailInfo());
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(
                holder.itemView.getContext(),
                R.anim.bounce
        ));

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView  textView;
        TextView  tvDetail;

        public ViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItemNews);
            textView  = itemView.findViewById(R.id.textViewItemNews);
            tvDetail  = itemView.findViewById(R.id.tvNewsDetail);
        }
    }
}