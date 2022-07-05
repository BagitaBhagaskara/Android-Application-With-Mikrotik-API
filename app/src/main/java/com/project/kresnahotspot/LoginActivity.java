package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
    ApiConnection con;
    ProgressBar progressBar;
    private String cekKoneksi;
    private static final String LOG_TAG = "LoginActivity";
    SharedPreferences.Editor setLoginMikrotik;
//    Config config =new Config();

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
//        config.ambilDataLogin();

        setLoginMikrotik=getSharedPreferences("data_login_mikrotik",MODE_PRIVATE).edit();

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
    private void ambilDataLoginMikrotik() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("loginMikrotik")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                                String id=queryDocumentSnapshot.getId();
                                DocumentReference documentReference=db.collection("loginMikrotik").document(id);
                                documentReference.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot documentSnapshot= task.getResult();
                                                setLoginMikrotik.putString("ipLoginMikrotik",documentSnapshot.getString("Connect"));
                                                setLoginMikrotik.putString("usernameLoginMikrotik",documentSnapshot.getString("Username"));
                                                setLoginMikrotik.putString("passwordLoginMikrotik",documentSnapshot.getString("Password"));
//                                                setLoginMikrotik.putString("portLoginMikrotik",documentSnapshot.getString("Port"));
                                                int p=Integer.parseInt(documentSnapshot.getString("Port"));
                                                setLoginMikrotik.putInt("portLoginMikrotikInt",p);
                                                setLoginMikrotik.commit();
                                                //share prefere
                                            }
                                        });
                            }
                        }
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
                                ambilDataLoginMikrotik();
//                                printInterface printInterface=new printInterface();
//                                printInterface.execute();
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
                            if(documentSnapshot.getString("statusLogin").equals("Admin")){
                                finish();
                                Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(i);
                            }
                            else if( documentSnapshot.getString("statusLogin").equals("User")){
                                finish();
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(LoginActivity.this, "Login Gagal, email atau password salah", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
    }

    public void gotoRegister(){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }



//    class loginMikrotik extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try
//            {
//                con =ApiConnection.connect(SocketFactory.getDefault(), config.HOST, ApiConnection.DEFAULT_PORT, 2000);
//                con.login(config.USERNAME, config.PASSWORD);
//                Log.d(LOG_TAG, "Login Mikrotik Berhasil");
//            }
//            catch (Exception e)
//            {
//
//                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot login mikrotik");
//
//            }
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            //tvResult.setText("End");
//            Log.d(LOG_TAG, "data config:   "+config.HOST+" "+config.USERNAME+" "+config.PASSWORD);
//        }
//    }
//    class printInterface extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try
//            {
//                List<Map<String, String>> rs =con.execute("/interface/print");
//                for (Map<String, String> map : rs) {
//                    Log.d(LOG_TAG, "interface Print" + map);
//                }
//
//            }
//            catch (Exception e)
//            {
//
//                Log.d(LOG_TAG, "");
//            }
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            //tvResult.setText("End");
//        }
//    }

//    class MyTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //tvResult.setText("Begin");
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            Config config =new Config();
//            config.ambilDataLogin();
//            try {
//               List<Map<String, String>> result = null;
//                try
//                {
//                    Log.d(LOG_TAG, "start");
//                    ApiConnection con = ApiConnection.connect(SocketFactory.getDefault(), config.HOST, ApiConnection.DEFAULT_PORT, 2000);
//                    con.login(config.USERNAME, config.PASSWORD);
////                   ApiConnection con = ApiConnection.connect("id-32.hostddns.us:3687");
////                   con.login("admin", "admin");
//                    Log.d(LOG_TAG, "start2");
////                    if(con.isConnected())
////                    {
////                        //tvResult.setText("OK!");
////                        Log.d(LOG_TAG, "isConnected");
////                    }
//                    con.execute("/system/resource/print");
//                    for(Map<String, String> res : result)
//                    {
//                        Log.d(LOG_TAG, res.toString());
//                        Log.d(LOG_TAG, result.toString());
//                    }
////                    con.close();
//                }
//                catch (Exception e)
//                {
//                    Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot");
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            //tvResult.setText("End");
//        }
//    }

}