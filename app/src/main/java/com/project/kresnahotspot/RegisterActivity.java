package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText inputName,inputEmail, inputPhone, inputPass1, inputPass2;
    String name,email,phone,pass1,pass2;
    Button btnRegister;
    FirebaseAuth mAuth;
    TextView kembali;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        inputName=findViewById(R.id.editTextName);
        inputEmail=findViewById(R.id.editTextEmail);
        inputPhone=findViewById(R.id.editTextPhone);
        inputPass1=findViewById(R.id.editTextPassword);
        inputPass2=findViewById(R.id.editTextRePassword);
        btnRegister=findViewById(R.id.cirRegisterButton);
        kembali=findViewById(R.id.backLogin);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=inputName.getText().toString();
                email=inputEmail.getText().toString();
                phone=inputPhone.getText().toString();
                pass1=inputPass1.getText().toString();
                pass2=inputPass2.getText().toString();


                if(name.equals("")){
                    Toast.makeText(RegisterActivity.this, "Harap masukan nama", Toast.LENGTH_LONG).show();
                }
                else if(email.equals("")){
                    Toast.makeText(RegisterActivity.this, "Harap masukan email", Toast.LENGTH_LONG).show();
                }
                else if(phone.equals("")){
                    Toast.makeText(RegisterActivity.this, "Harap masukan phone number", Toast.LENGTH_LONG).show();
                }
                else if(pass1.equals("")){
                    Toast.makeText(RegisterActivity.this, "Harap masukan password", Toast.LENGTH_LONG).show();
                }
                else if(pass2.equals("")){
                    Toast.makeText(RegisterActivity.this, "Harap masukan Re-password", Toast.LENGTH_LONG).show();
                }
                else if(!pass2.equals(pass1)){
                    Toast.makeText(RegisterActivity.this, "Re-Password tidak sama dengan Password", Toast.LENGTH_LONG).show();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email,pass2)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        simpanDataUser(name,email,phone);
                                        Toast.makeText(RegisterActivity.this, "Registrasi Sukses", Toast.LENGTH_LONG).show();
                                        Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(i);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterActivity.this, "Registrasi Gagal", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }
            }
        });


        changeStatusBarColor();
    }

   private void simpanDataUser(String nama, String email, String telepon){
       FirebaseUser user=mAuth.getCurrentUser();
       DocumentReference documentReference= db.collection("dataUser").document(user.getUid());
        Map<String, Object> dataUser = new HashMap<>();
        dataUser.put("phoneUser", telepon);
        dataUser.put("emailUser", email);
        dataUser.put("namaUser", nama);
        dataUser.put("coin","0");
        dataUser.put("statusLogin","User");

        documentReference.set(dataUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });
    }
    void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
}