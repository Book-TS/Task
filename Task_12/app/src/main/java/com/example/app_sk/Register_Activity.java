package com.example.app_sk;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class Register_Activity extends AppCompatActivity {

    private Toolbar toolbar_signIn;
    private Button bt_sign_in;
    private EditText account_sign_in, password_signIn, password_signIn_1;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        anhXa();

        sign_in();

        toolBar();
    }

    private void anhXa() {
        toolbar_signIn = findViewById(R.id.toolbar_signIn);
        bt_sign_in = findViewById(R.id.bt_sign_in);
        account_sign_in = findViewById(R.id.account_sign_in);
        password_signIn = findViewById(R.id.password_signIn);
        password_signIn_1 = findViewById(R.id.password_signIn_1);
    }

    private void toolBar() {
        setSupportActionBar(toolbar_signIn);
        toolbar_signIn.setNavigationOnClickListener(v -> onBackPressed());
    }

    // Xử lý đăng ký
    private void sign_in() {
        bt_sign_in.setOnClickListener(v -> {
            String email1 = account_sign_in.getText().toString();
            String pass1 = password_signIn.getText().toString();
            String pass2 = password_signIn_1.getText().toString();
            if (email1.isEmpty())
                Toast.makeText(Register_Activity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            else if (pass1.isEmpty())
                Toast.makeText(Register_Activity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            else if (pass2.isEmpty())
                Toast.makeText(Register_Activity.this, "Vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
            else {
                if (pass1.equals(pass2)) {
                    mAuth.createUserWithEmailAndPassword(email1, pass1).addOnCompleteListener(Register_Activity.this, task -> {
                        if (task.isSuccessful())
                            Toast.makeText(Register_Activity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Register_Activity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    });
                } else
                    Toast.makeText(Register_Activity.this, "Nhập lại mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}