package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.adapter.DrinkAdapter;
import com.example.demo.eventbus.UpdateCartEvent;
import com.example.demo.listener.CartLoad;
import com.example.demo.listener.DrinkLoad;
import com.example.demo.model.CartModel;
import com.example.demo.model.DrinkModel;
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

public class CoffeeMenu extends AppCompatActivity implements DrinkLoad, CartLoad {
    @BindView(R.id.recyclerMenu)
    RecyclerView recyclerViewMenu;
    @BindView(R.id.mainLayoutMenu)
    RelativeLayout relativeLayoutMenu;
    @BindView(R.id.btCart)
    FrameLayout btCart;
    @BindView(R.id.imageBackButton)
    ImageView btBack;

    DrinkLoad drinkLoadListener;
    CartLoad cartLoadListener;

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
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_menu);

        init();
        loadModelFromFireBase();
        countCartItem();
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoffeeMenu.this,MainPage.class));
            }
        });
    }

    private void loadModelFromFireBase() {
        List<DrinkModel> drinkModelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot drinkSnapshot:snapshot.getChildren() ){
                        DrinkModel drinkModel = drinkSnapshot.getValue(DrinkModel.class);
                        drinkModel.setKey(drinkSnapshot.getKey());
                        drinkModelList.add(drinkModel);
                    }
                    drinkLoadListener.onDrinkLoadSuccess(drinkModelList);
                } else {
                    drinkLoadListener.onDrinkLoadFail("Chưa có đồ uống trong giỏ hang!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                drinkLoadListener.onDrinkLoadFail(error.getMessage());
            }
        });
    }

    private void init(){
        ButterKnife.bind(this);
        drinkLoadListener = this;
        cartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewMenu.setLayoutManager(gridLayoutManager);
        recyclerViewMenu.addItemDecoration(new CartItemDecoration());

        btCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoffeeMenu.this,CartActivity.class));
            }
        });
    }

    @Override
    public void onDrinkLoadSuccess(List<DrinkModel> drinkModelList) {
        DrinkAdapter adapterDrink = new DrinkAdapter(this,drinkModelList,cartLoadListener);
        recyclerViewMenu.setAdapter(adapterDrink);
    }

    @Override
    public void onDrinkLoadFail(String message) {
        Snackbar.make(relativeLayoutMenu,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
//        int cartSum = 0;
//        for(CartModel cartModel: cartModelList)
//            cartSum += cartModel.getNumber();
    }

    @Override
    public void onCartLoadFail(String message) {
        Snackbar.make(relativeLayoutMenu,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        List<CartModel> cartModelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Cart").child("User_ID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot cartSnapshot:snapshot.getChildren()){
                    CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                    cartModel.setKey(cartSnapshot.getKey());
                    cartModelList.add(cartModel);
                }
                cartLoadListener.onCartLoadSuccess(cartModelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
