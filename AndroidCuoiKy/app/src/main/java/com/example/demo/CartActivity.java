package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.adapter.CartAdapter;
import com.example.demo.eventbus.UpdateCartEvent;
import com.example.demo.listener.CartLoad;
import com.example.demo.model.CartModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements CartLoad {
    @BindView(R.id.recyclerCart)
    RecyclerView recyclerViewCart;
    @BindView(R.id.mainLayoutMenu)
    RelativeLayout mainLayoutCart;
    @BindView(R.id.btBack)
    ImageView btBackCart;
    @BindView(R.id.tvPrice)
    TextView tvPriceCart;
    @BindView(R.id.btBuy)
    ImageView btBuy;
    AlertDialog.Builder alertDialog;
    CartLoad cartLoadCart;

    @Override
    protected void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent.class)) EventBus.getDefault().removeStickyEvent(UpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(UpdateCartEvent event){
        loadCartFromFireBase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        alertDialog = new AlertDialog.Builder(this);
        init();
        loadCartFromFireBase();
        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this,"Thanh toán thành công",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CartActivity.this,MainPage.class));

            }
        });
    }

    private void loadCartFromFireBase() {
        List<CartModel> cartModelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Cart").child("User_ID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot cartSnapshot:snapshot.getChildren())
                    {
                        CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                        cartModel.setKey(cartSnapshot.getKey());
                        cartModelList.add(cartModel);
                    }
                    cartLoadCart.onCartLoadSuccess(cartModelList);
                }else{
                    cartLoadCart.onCartLoadFail("Giỏ hàng trống!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cartLoadCart.onCartLoadFail(error.getMessage());
            }
        });
    }

    private void init(){
        ButterKnife.bind(this);
        cartLoadCart = this;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        recyclerViewCart.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.getOrientation()));

        btBackCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        double sum = 0;
        for(CartModel cartModel:cartModelList){
            sum+=cartModel.getTotalPrice();
        }
        tvPriceCart.setText(new StringBuilder().append(sum).append("đ"));
        CartAdapter adapter = new CartAdapter(this,cartModelList);
        recyclerViewCart.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFail(String message) {
        Snackbar.make(mainLayoutCart,message,Snackbar.LENGTH_LONG).show();
    }
}