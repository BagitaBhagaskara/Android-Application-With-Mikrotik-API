package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import javax.net.SocketFactory;

import me.legrange.mikrotik.ApiConnection;
//
//public class tambahProfileHotspot_admin extends AppCompatActivity {
//    ImageView kembali;
//    LinearLayout warning;
//    EditText namaProfile,rateLimit,server,shareUser,uptime;
//    Button simpanProfile;
//    private static final String LOG_TAG = "Tambah Profile";
//    String nama,rate,serverHotspot,up,share;
//    ApiConnection con;
//    SharedPreferences getDataLoginMikrotik;
//    public static boolean cekLoginMikrotik=true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_tambah_profile_hotspot_admin);
//
//        warning=findViewById(R.id.tambahProfile_warning);
//        kembali=findViewById(R.id.tambahProfile_kembali);
//        namaProfile=findViewById(R.id.tambahProfile_namaProfile);
//        rateLimit=findViewById(R.id.tambahProfile_rateLimit);
//        server=findViewById(R.id.tambahProfile_server);
//        shareUser=findViewById(R.id.tambahProfile_shareUser);
//        uptime=findViewById(R.id.tambahProfile_uptime);
//        simpanProfile=findViewById(R.id.tambahProfile_buttonSimpan);
//        Log.d(LOG_TAG, "serverHotspot: ");
//        getDataLoginMikrotik= tambahProfileHotspot_admin.this.getSharedPreferences("data_login_mikrotik", Context.MODE_PRIVATE);
//        kembali.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(tambahProfileHotspot_admin.this,tambahVoucher_admin.class));
//                overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
//            }
//        });
//
//        simpanProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                nama=namaProfile.getText().toString();
////                rate=rateLimit.getText().toString();
////                serverHotspot=server.getText().toString();
////                share=shareUser.getText().toString();
////                up=uptime.getText().toString();
//
//                Log.d(LOG_TAG, "serverHotspotPencet: ");
//
////                if(nama.equals("")){
////                    Toast.makeText(tambahProfileHotspot_admin.this, "Nama Profile Belum Terisi", Toast.LENGTH_SHORT).show();
////                }else if(rate.equals("")){
////                    Toast.makeText(tambahProfileHotspot_admin.this, "Rate Limit Belum Terisi", Toast.LENGTH_SHORT).show();
////                }else if(serverHotspot.equals("")){
////                    Toast.makeText(tambahProfileHotspot_admin.this, "Server Hotspot Belum Terisi", Toast.LENGTH_SHORT).show();
////                }else if(share.equals("")){
////                    Toast.makeText(tambahProfileHotspot_admin.this, "Share User Belum Terisi", Toast.LENGTH_SHORT).show();
////                }else if(up.equals("")) {
////                    Toast.makeText(tambahProfileHotspot_admin.this, "Uptime Belum Terisi", Toast.LENGTH_SHORT).show();
////                }else{
//
//                    dialogSimpanProfile();
////                }
//
//            }
//        });
//
//
//    }
//
//
//    private void dialogSimpanProfile() {
//        View view= LayoutInflater.from(tambahProfileHotspot_admin.this).inflate(R.layout.dialog_simpan_profile_admin,null);
//        Button iya, tidak;
//        iya=findViewById(R.id.dialogTambahProfile_buttonIya);
//        tidak=findViewById(R.id.dialogTambahProfile_buttonTidak);
//
//        AlertDialog.Builder builder=new AlertDialog.Builder(tambahProfileHotspot_admin.this);
//        builder.setView(view);
//
//        AlertDialog dialog=builder.create();
//        dialog.show();
//
//        iya.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tambahProfileMikrotik profileMikrotik=new tambahProfileMikrotik();
//                profileMikrotik.execute();
//            }
//        });
//
//    }
//    class tambahProfileMikrotik extends AsyncTask<Void,Void,Void> {
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try
//            {
//                con = ApiConnection.connect(SocketFactory.getDefault(), getDataLoginMikrotik.getString("ipLoginMikrotik",null), ApiConnection.DEFAULT_PORT, 2000);
//                con.login(getDataLoginMikrotik.getString("usernameLoginMikrotik",null), getDataLoginMikrotik.getString("passwordLoginMikrotik",null));
//                if(con.isConnected()){
//                    con.execute("/ip/hotspot/user/profile/add name="+nama+" idle-timeout=none keepalive-timeout=2m status-autorefresh=1m shared-users="+share+" add-mac-cookie=true mac-cookie-timeout=3d parent-queue=none rate-limit="+rate);
//                    simpanProfileHotspot(nama,rate,serverHotspot,share,up);
//                }else{
//                    Toast.makeText(tambahProfileHotspot_admin.this, "Anda belum terhubung pada mikrotik, periksa konfigurasi login Mikrotik", Toast.LENGTH_SHORT).show();
//                }
//
//                Log.d(LOG_TAG, "Login Mikrotik Berhasil");
//            }
//            catch (Exception e)
//            {
//                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot login mikrotik");
//            }
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            //tvResult.setText("End");
//        }
//    }
//
//    private void simpanProfileHotspot(String nama, String rate, String serverHotspot, String share, String up) {
//        FirebaseFirestore db= FirebaseFirestore.getInstance();
//        HashMap<String,Object> profile=new HashMap<>();
//        profile.put("namaProfile",nama);
//        profile.put("rateLimit",rate);
//        profile.put("server",serverHotspot);
//        profile.put("shareUser",share);
//        profile.put("uptime",up);
//        db.collection("configHotspot")
//                .add(profile)
//                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(tambahProfileHotspot_admin.this, "Data Profile Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//}