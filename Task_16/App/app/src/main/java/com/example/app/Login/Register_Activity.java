package com.example.app.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.app.R;
import com.google.firebase.auth.FirebaseAuth;

public class Register_Activity extends AppCompatActivity {

    private Button bt_Register;
    private EditText account_Register, password_Register, password_Register_1;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        anhXa();

        toolBar();

        registerEvent();
    }

    private void anhXa() {
        bt_Register = findViewById(R.id.bt_Register);
        account_Register = findViewById(R.id.account_Register);
        password_Register = findViewById(R.id.password_Register);
        password_Register_1 = findViewById(R.id.password_Register_1);
    }

    private void toolBar() {
        Toolbar toolbar_Register = findViewById(R.id.toolbar_Register);

        setSupportActionBar(toolbar_Register);
        toolbar_Register.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void registerEvent() {
        bt_Register.setOnClickListener(v -> {
            String email1 = account_Register.getText().toString();
            String pass1 = password_Register.getText().toString();
            String pass2 = password_Register_1.getText().toString();
            if (email1.isEmpty())
                Toast.makeText(Register_Activity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            else if (pass1.isEmpty())
                Toast.makeText(Register_Activity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            else if (pass2.isEmpty())
                Toast.makeText(Register_Activity.this, "Vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
            else {
                if (pass1.equals(pass2)) {
                    mAuth.createUserWithEmailAndPassword(email1, pass1).addOnCompleteListener(Register_Activity.this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register_Activity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(Register_Activity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    });
                } else
                    Toast.makeText(Register_Activity.this, "Nhập lại mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}