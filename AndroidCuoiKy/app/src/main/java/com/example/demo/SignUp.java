package com.example.demo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText edtPhone =(EditText) findViewById(R.id.edtPhone);
        EditText edtPassword =(EditText)  findViewById(R.id.edtPassword);
        EditText edtName =(EditText) findViewById(R.id.edtName);
        Button bt_acceptSignUp = findViewById(R.id.bt_acceptSignUp);
        DatabaseReference table_user = FirebaseDatabase.getInstance().getReference("User");

        bt_acceptSignUp.setOnClickListener(v -> {
            ProgressDialog dialog = new ProgressDialog(SignUp.this);
            dialog.setMessage("Vui lòng đợi...");
            dialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(edtPhone.getText().toString()).exists()){
                        dialog.dismiss();
                        Toast.makeText(SignUp.this, "Số điện thoại này đã đăng ký!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        dialog.dismiss();
                        User user = new User(edtName.getText().toString(),edtPassword.getText().toString());
                        table_user.child(edtPhone.getText().toString()).setValue(user);
                        Toast.makeText(SignUp.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: Error");

                }
            });
        });

    }
}