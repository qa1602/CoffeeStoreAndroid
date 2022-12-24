package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.example.demo.SignIn;


public class MainActivity extends AppCompatActivity {
    Button btSignUp,btSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btSignIn = findViewById(R.id.bt_signin);
        btSignUp = findViewById(R.id.bt_signup);


        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSignIn = new Intent(MainActivity.this, SignIn.class);
                startActivity(intentSignIn);
            }
        });
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSignUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(intentSignUp);
            }
        });

    }
}