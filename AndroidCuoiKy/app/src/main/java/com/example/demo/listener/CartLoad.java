package com.example.demo.listener;

import com.example.demo.model.CartModel;

import java.util.List;

public interface CartLoad {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFail(String message);
}
