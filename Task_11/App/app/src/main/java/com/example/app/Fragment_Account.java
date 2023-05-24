package com.example.app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_Account extends Fragment {

    private View fm_account;
    private ListView listView_2;
    String[] items = new String[]{"Đổi mật khẩu", "Đăng xuất"};
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private EditText txt_pass_4, txt_pass_5, txt_pass_6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fm_account = inflater.inflate(R.layout.fragment_account, container, false);

        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        anhXa();
        listView_2_Show();
        return fm_account;
    }

    // Ánh xạ ID
    private void anhXa() {
        listView_2 = fm_account.findViewById(R.id.listView_2);
    }

    // Hiển thị list View
    private void listView_2_Show() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        listView_2.setAdapter(adapter);

        listView_2.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    dialog_1_Show();
                    break;
                case 1:
                    mAuth.signOut();
                    mData.child("taikhoan/status").setValue(0);
                    Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getActivity(), Login_Activity.class);
                    startActivity(intent2);
                    break;
            }
        });
    }

    // Bật dialog thay đổi mật khẩu
    private void dialog_1_Show() {
        final Dialog dialog_1 = new Dialog(getActivity());
        dialog_1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_1.setContentView(R.layout.dialog_change_password);

        Window window_1 = dialog_1.getWindow();
        if (window_1 == null) {
            return;
        }

        window_1.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window_1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windownAttributes_1 = window_1.getAttributes();
        windownAttributes_1.gravity = Gravity.CENTER;
        window_1.setAttributes(windownAttributes_1);

        dialog_1.show();

        txt_pass_4 = dialog_1.findViewById(R.id.txt_pass_4);
        txt_pass_5 = dialog_1.findViewById(R.id.txt_pass_5);
        txt_pass_6 = dialog_1.findViewById(R.id.txt_pass_6);

        Button bt_cancel = dialog_1.findViewById(R.id.bt_cancel);
        Button bt_change_pass = dialog_1.findViewById(R.id.bt_change_pass);

        bt_cancel.setOnClickListener(v -> dialog_1.dismiss());
        bt_change_pass.setOnClickListener(v -> {
            String pass1 = txt_pass_4.getText().toString().trim();
            String pass2 = txt_pass_5.getText().toString().trim();
            String pass3 = txt_pass_6.getText().toString().trim();

            if (pass1.isEmpty() || pass2.isEmpty() || pass3.isEmpty())
                Toast.makeText(getActivity(), "Không được bỏ trống !!!", Toast.LENGTH_SHORT).show();
            else {
                mData.child("/taikhoan").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Account account = snapshot.getValue(Account.class);
                        assert account != null;

                        if (pass1.equals(account.pass)) {
                            if (pass2.equals(pass3)) {
                                user.updatePassword(pass2).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        dialog_1.dismiss();
                                        mAuth.signOut();
                                        mData.child("/taikhoan/status").setValue(0);
                                        Intent intent1 = new Intent(getActivity(), Login_Activity.class);
                                        startActivity(intent1);
                                    }
                                    else
                                        Toast.makeText(getActivity(), "Mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
                                });
                            }
                            else
                                Toast.makeText(getActivity(), "Nhập lại mật khẩu mới không đúng", Toast.LENGTH_SHORT).show();

                        }
                        else
                            Toast.makeText(getActivity(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}
