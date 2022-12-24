package com.example.demo.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demo.CartActivity;
import com.example.demo.R;
import com.example.demo.eventbus.UpdateCartEvent;
import com.example.demo.model.CartModel;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private CartActivity context;
    private List<CartModel> cartModelList;

    public CartAdapter(CartActivity context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(cartModelList.get(position).getImage()).error(R.drawable.ic_baseline_error_24).into(holder.imageViewCart);
        holder.tvPrice.setText(new StringBuilder().append(cartModelList.get(position).getPrice()).append("đ"));
        holder.tvName.setText(new StringBuilder().append(cartModelList.get(position).getName()));
        holder.tvNumber.setText(new StringBuilder().append(cartModelList.get(position).getNumber()));

        holder.btMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusCartItem(holder,cartModelList.get(position));
            }
        });
        holder.btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusCartItem(holder,cartModelList.get(position));
            }
        });
        holder.btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogAlert = new AlertDialog.Builder(context).setTitle("Xóa nước uống!").setMessage("Bạn có chắc chắn không?")
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.dismiss();
                    }
                }).setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int which) {
                        notifyItemRemoved(position );
                        deleteFromFirebase(cartModelList.get(position));
                        dialog2.dismiss();
                    }
                }).create();
                dialogAlert.show();
            }
        });
    }

    private void deleteFromFirebase(CartModel cartModel) {
        FirebaseDatabase.getInstance().getReference("Cart").child("User_ID").child(cartModel.getKey()).removeValue().addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new UpdateCartEvent()));

    }

    private void plusCartItem(CartViewHolder holder, CartModel cartModel) {
        cartModel.setNumber(cartModel.getNumber()+1);
        cartModel.setTotalPrice(cartModel.getNumber()*cartModel.getPrice());

        holder.tvNumber.setText((new StringBuilder().append(cartModel.getNumber())));
        updateFirebase(cartModel);
    }

    private void minusCartItem(CartViewHolder holder, CartModel cartModel) {
        if(cartModel.getNumber() > 1){
            cartModel.setNumber(cartModel.getNumber()-1);
            cartModel.setTotalPrice(cartModel.getNumber()*cartModel.getPrice());

            holder.tvNumber.setText((new StringBuilder().append(cartModel.getNumber())));
            updateFirebase(cartModel);
        }

    }

    private void updateFirebase(CartModel cartModel) {
        FirebaseDatabase.getInstance().getReference("Cart").child("User_ID").child(cartModel.getKey()).setValue(cartModel).addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new UpdateCartEvent()));
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btMinus)
        ImageView btMinus;
        @BindView(R.id.btPlus)
        ImageView btPlus;
        @BindView(R.id.btDel)
        ImageView btDel;
        @BindView(R.id.imageViewCart)
        ImageView imageViewCart;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvNumber)
        TextView tvNumber;

        Unbinder unbinder;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
