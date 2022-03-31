package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import javax.net.SocketFactory;

import examples.AnonymousSocketFactory;
import examples.Config;
import me.legrange.mikrotik.ApiConnection;


public class LoginActivity extends AppCompatActivity {
    EditText inputEmail, inputPassword;
    String email, password;
    TextView btnRegis2;
    ImageView btnRegis;
    Button btnLogin;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    protected ApiConnection con;
    ProgressBar progressBar;
    private String cekKoneksi;
    private static final String LOG_TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //untuk mengubah warna status bar icon
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        inputEmail=findViewById(R.id.editTextEmail);
        inputPassword=findViewById(R.id.editTextPassword);
        btnLogin=findViewById(R.id.cirLoginButton);
        btnRegis=findViewById(R.id.regis);
        btnRegis2=findViewById(R.id.regis2);
        progressBar=findViewById(R.id.login_progresBar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                ceklogin();
            }
        });
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRegister();
            }
        });
        btnRegis2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRegister();
            }
        });

    }


    private void ceklogin() {
        email=inputEmail.getText().toString();
        password=inputPassword.getText().toString();

        if(email.equals("")){
            Toast.makeText(LoginActivity.this, "Harap masukan email", Toast.LENGTH_LONG).show();
        }
        else if(password.equals("")){
            Toast.makeText(LoginActivity.this, "Harap masukan password", Toast.LENGTH_LONG).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LoginActivity.this, "Berasil Login", Toast.LENGTH_LONG).show();
                                MyTask mt = new MyTask();
                                mt.execute();
                                AuthResult authResult=task.getResult();
                                cekUserAdmin(authResult.getUser().getUid());
                                progressBar.setVisibility(View.GONE);

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Login Gagal, email atau password salah", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });
        }

    }

    private void cekUserAdmin(String Uid) {
        db.collection("dataUser").document(Uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())  {
                            DocumentSnapshot documentSnapshot=task.getResult();
                            if(documentSnapshot.getString("isAdmin")!=null){
                                Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(i);
                            }
                            else if( documentSnapshot.getString("isUser")!=null){

                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                            }
                        }
                    }
                });
    }

    public void gotoRegister(){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

//    protected void connect() throws Exception {
//        con = ApiConnection.connect(AnonymousSocketFactory.getDefault(), Config.HOST, ApiConnection.DEFAULT_TLS_PORT, ApiConnection.DEFAULT_CONNECTION_TIMEOUT);
//        con.login(Config.USERNAME, Config.PASSWORD);
//    }


    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //tvResult.setText("Begin");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

               // List<Map<String, String>> result = null;

                try
                {
                    Log.d(LOG_TAG, "start");
                    ApiConnection con = ApiConnection.connect(SocketFactory.getDefault(), Config.HOST, ApiConnection.DEFAULT_PORT, 2000);
                    con.login(Config.USERNAME, Config.PASSWORD);
//                   ApiConnection con = ApiConnection.connect("id-32.hostddns.us:3687");
//                   con.login("admin", "admin");
                    Log.d(LOG_TAG, "start2");
                    if(con.isConnected())
                    {
                        //tvResult.setText("OK!");
                        Log.d(LOG_TAG, "isConnected");
                    }
                   // con.execute("/ip/hotspot/user/add name=hai password=hai profile=default");
//                    for(Map<String, String> res : result)
//                    {
//                        Log.d(LOG_TAG, res.toString());
//                    }
                    con.close();
                }
                catch (Exception e)
                {
                    Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //tvResult.setText("End");
        }
    }

}