package com.example.app.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.Account;
import com.example.app.Menu_Bottom_Nav.Main_Activity;
import com.example.app.MyService;
import com.example.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {

    private Button bt_login;
    private TextView txt_Register, txt_forgot;
    private EditText account_logIn, password_logIn;

    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public Intent intent_Service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        anhXa();

        realTimeAccount();

        loginEvent();

        registerEvent();

        forgotEvent();
    }

    private void anhXa() {
        bt_login = findViewById(R.id.bt_login);
        txt_Register = findViewById(R.id.txt_Register);
        txt_forgot = findViewById(R.id.txt_forgot);

        account_logIn = findViewById(R.id.account_logIn);
        password_logIn = findViewById(R.id.password_logIn);
    }

    private void realTimeAccount() {
        mData.child("Account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Account account = snapshot.getValue(Account.class);

                assert account != null;
                account_logIn.setText(account.email);
                password_logIn.setText(account.pass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loginEvent() {
        bt_login.setOnClickListener(v -> {
            String email1 = account_logIn.getText().toString();
            String pass1 = password_logIn.getText().toString();

            // Kiểm tra xem đã nhập email và password chưa
            if (email1.isEmpty()) {
                Toast.makeText(Login_Activity.this, "Vui lòng nhập địa chỉ Email", Toast.LENGTH_SHORT).show();
            } else if (pass1.isEmpty()) {
                Toast.makeText(Login_Activity.this, "Vui lòng nhập Password", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email1, pass1).addOnCompleteListener(Login_Activity.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login_Activity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Login_Activity.this, Main_Activity.class);
                        startActivity(intent);

                        Account account = new Account(email1, pass1, 1);
                        mData.child("Account").setValue(account);

                        intent_Service = new Intent(Login_Activity.this, MyService.class);
                        startService(intent_Service);

                    } else {
                        Toast.makeText(Login_Activity.this, "Tên tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void registerEvent() {
        txt_Register.setOnClickListener(v -> {
            Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
            startActivity(intent);
        });
    }

    private void forgotEvent() {
        txt_forgot.setOnClickListener(v -> {
            String email1 = account_logIn.getText().toString();

            // Kiểm tra xem đã nhập email chưa
            if (email1.isEmpty())
                Toast.makeText(Login_Activity.this, "Vui lòng nhập địa chỉ mail", Toast.LENGTH_SHORT).show();
            else {
                mAuth.sendPasswordResetEmail(email1).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Toast.makeText(Login_Activity.this, "Đã gửi mã xác nhận, vui lòng check mail", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Login_Activity.this, "Gửi yêu cầu thất bại", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}