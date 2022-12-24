package com.example.demo.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demo.CoffeeMenu;
import com.example.demo.R;
import com.example.demo.listener.CartLoad;
import com.example.demo.listener.RecyclerClick;
import com.example.demo.model.CartModel;
import com.example.demo.model.DrinkModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    private CoffeeMenu context;
    private List<DrinkModel> drinkModelList;
    private CartLoad cartLoad;

    public DrinkAdapter(CoffeeMenu context, List<DrinkModel> drinkModelList, CartLoad cartLoad) {
        this.context = context;
        this.drinkModelList = drinkModelList;
        this.cartLoad = cartLoad;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DrinkViewHolder(LayoutInflater.from(context).inflate(R.layout.drink_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(drinkModelList.get(position).getImage()).error(R.drawable.ic_baseline_error_24).into(holder.imageView);
        holder.tvPrice.setText(new StringBuilder().append(drinkModelList.get(position).getPrice()).append("đ"));
        holder.tvName.setText(new StringBuilder().append(drinkModelList.get(position).getName()));
        holder.setRecyclerClickListener(new RecyclerClick() {
            @Override
            public void onRecyclerClick(View view, int adapterPosition) {
                addToCart(drinkModelList.get(position));
            }
        });
    }

    private void addToCart(DrinkModel drinkModel) {
        DatabaseReference userCart = FirebaseDatabase.getInstance().getReference("Cart").child("User_ID");
        userCart.child(drinkModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CartModel cartModel = snapshot.getValue(CartModel.class);
                    cartModel.setNumber(cartModel.getNumber()+1);
                    Map<String,Object> updateData = new HashMap<>();
                    updateData.put("quantity",cartModel.getNumber()+1);
                    updateData.put("totalPrice",cartModel.getNumber()*(cartModel.getPrice()));
                    userCart.child(drinkModel.getKey()).updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            cartLoad.onCartLoadFail("Đã thêm vào giỏ hàng!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            cartLoad.onCartLoadFail(e.getMessage());
                        }
                    });
                }else{
                    CartModel cartModel = new CartModel();
                    cartModel.setKey(drinkModel.getKey());
                    cartModel.setName(drinkModel.getName());
                    cartModel.setImage(drinkModel.getImage());
                    cartModel.setPrice(drinkModel.getPrice());
                    cartModel.setNumber(1);
                    cartModel.setTotalPrice(drinkModel.getPrice());
                    userCart.child(drinkModel.getKey()).setValue(cartModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            cartLoad.onCartLoadFail("Đã thêm vào giỏ hàng!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            cartLoad.onCartLoadFail(e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cartLoad.onCartLoadFail(error.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return drinkModelList.size();
    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageViewItem)
        ImageView imageView;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvPrice)
        TextView tvPrice;

        RecyclerClick recyclerClickListener;

        public void setRecyclerClickListener(RecyclerClick recyclerClickListener) {
            this.recyclerClickListener = recyclerClickListener;
        }

        private Unbinder unbinder;
        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerClickListener.onRecyclerClick(v,getAdapterPosition());
        }
    }
}
