package com.example.demo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EditText edtPhone =(EditText) findViewById(R.id.edtPhone);
        EditText edtPassword =(EditText)  findViewById(R.id.edtPassword);
        Button bt_acceptSignIn = findViewById(R.id.bt_acceptSignIn);

        bt_acceptSignIn.setOnClickListener(v -> {
            final DatabaseReference table_user = FirebaseDatabase.getInstance().getReference("User");

            ProgressDialog dialog = new ProgressDialog(SignIn.this);
            dialog.setMessage("Vui lòng đợi...");
            dialog.show();
            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "onDataChange: ");

                    if (snapshot.child(edtPhone.getText().toString()).exists()) {
                        dialog.dismiss();
                        User user = snapshot.child(edtPhone.getText().toString()).getValue(User.class);
                        assert user != null;
                        if (user.getPassword().equals(edtPassword.getText().toString())) {
                            Toast.makeText(SignIn.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intentMainPage = new Intent(SignIn.this, MainPage.class);
                            startActivity(intentMainPage);
                        } else {
                            Toast.makeText(SignIn.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(SignIn.this,"Người dùng không hợp lệ!",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: Error");
                }
            }) ;
        });
    }
}
