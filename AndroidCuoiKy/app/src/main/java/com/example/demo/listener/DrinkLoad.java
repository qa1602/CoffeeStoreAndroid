package com.example.demo.listener;

import com.example.demo.model.DrinkModel;

import java.util.List;

public interface DrinkLoad {
    void onDrinkLoadSuccess(List<DrinkModel> drinkModelList);
    void onDrinkLoadFail(String message);
}
