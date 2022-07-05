package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.adapter.ProfileHotspotAdminAdapter;
import com.project.kresnahotspot.model.ProfileHotspotAdmin_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import javax.net.SocketFactory;

import me.legrange.mikrotik.ApiConnection;

public class profileHotspotAdmin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProfileHotspotAdminAdapter profileHotspotAdminAdapter;
    private List<ProfileHotspotAdmin_model> list=new ArrayList<>();
    ImageView kembali, tambahHotspot;
    EditText namaProfile,rateLimit,server,shareUser,uptime;
    String nama,rate,serverHotspot,up,share;
    Button simpanProfile;
    ApiConnection con;
    SharedPreferences getDataLoginMikrotik;
    private static final String LOG_TAG = "Tambah Profile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_hotspot_admin);
        recyclerView=findViewById(R.id.itemProfileHotspotAdmin);
        profileHotspotAdminAdapter = new ProfileHotspotAdminAdapter(profileHotspotAdmin.this,list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(profileHotspotAdmin.this,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(profileHotspotAdminAdapter);
        kembali=(ImageView) findViewById(R.id.profileHotspotAdmin_kembali);
        tambahHotspot=(ImageView) findViewById(R.id.profileHotspotAdmin_tambahProfile);

        getDataLoginMikrotik= profileHotspotAdmin.this.getSharedPreferences("data_login_mikrotik", Context.MODE_PRIVATE);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tambahHotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTampilTambahHotspot();
            }
        });

        ambilDataProfileHotspot();

    }

    private void dialogTampilTambahHotspot() {
        View view= LayoutInflater.from(profileHotspotAdmin.this).inflate(R.layout.activity_tambah_profile_hotspot_admin,null);
        ImageView dialogKembali;
        dialogKembali=(ImageView) view.findViewById(R.id.tambahProfile_kembali);
        simpanProfile=(Button) view.findViewById(R.id.tambahProfile_buttonSimpan);
        namaProfile=(EditText) view.findViewById(R.id.tambahProfile_namaProfile);
        rateLimit=(EditText) view.findViewById(R.id.tambahProfile_rateLimit);
        server=(EditText) view.findViewById(R.id.tambahProfile_server);
        shareUser=(EditText) view.findViewById(R.id.tambahProfile_shareUser);
        uptime=(EditText) view.findViewById(R.id.tambahProfile_uptime);
        AlertDialog.Builder builder=new AlertDialog.Builder(profileHotspotAdmin.this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        builder.setView(view);

        AlertDialog dialog=builder.create();
        dialog.show();

        dialogKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        simpanProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama=namaProfile.getText().toString();
                rate=rateLimit.getText().toString();
                serverHotspot=server.getText().toString();
                share=shareUser.getText().toString();
                up=uptime.getText().toString();

                Log.d(LOG_TAG, "serverHotspotPencet: ");

                if(nama.equals("")){
                    Toast.makeText(profileHotspotAdmin.this, "Nama Profile Belum Terisi", Toast.LENGTH_SHORT).show();
                }else if(rate.equals("")){
                    Toast.makeText(profileHotspotAdmin.this, "Rate Limit Belum Terisi", Toast.LENGTH_SHORT).show();
                }else if(serverHotspot.equals("")){
                    Toast.makeText(profileHotspotAdmin.this, "Server Hotspot Belum Terisi", Toast.LENGTH_SHORT).show();
                }else if(share.equals("")){
                    Toast.makeText(profileHotspotAdmin.this, "Share User Belum Terisi", Toast.LENGTH_SHORT).show();
                }else if(up.equals("")) {
                    Toast.makeText(profileHotspotAdmin.this, "Uptime Belum Terisi", Toast.LENGTH_SHORT).show();
                }else{

                dialogSimpanProfile();
                }

            }
        });

    }
    private void dialogSimpanProfile() {
        View Itemview= LayoutInflater.from(profileHotspotAdmin.this).inflate(R.layout.dialog_simpan_profile_admin,null);
        Button iya, tidak;
        iya=(Button) Itemview.findViewById(R.id.dialogTambahProfile_buttonIya);
        tidak=(Button) Itemview.findViewById(R.id.dialogTambahProfile_buttonTidak);

        AlertDialog.Builder builder=new AlertDialog.Builder(profileHotspotAdmin.this);
        builder.setView(Itemview);

        AlertDialog dialog=builder.create();
        dialog.show();

        iya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View Itemview) {
                tambahProfileMikrotik profileMikrotik=new tambahProfileMikrotik();
                profileMikrotik.execute();
                dialog.dismiss();
            }
        });
        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View Itemview) {
                dialog.dismiss();
            }
        });

    }
    class tambahProfileMikrotik extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                con = ApiConnection.connect(SocketFactory.getDefault(), getDataLoginMikrotik.getString("ipLoginMikrotik",null), getDataLoginMikrotik.getInt("portLoginMikrotikInt",0 ), 2000);
                con.login(getDataLoginMikrotik.getString("usernameLoginMikrotik",null), getDataLoginMikrotik.getString("passwordLoginMikrotik",null));
                if(con.isConnected()){
                    con.execute("/ip/hotspot/user/profile/add name="+nama+" idle-timeout=none keepalive-timeout=2m status-autorefresh=1m shared-users="+share+" add-mac-cookie=true mac-cookie-timeout=3d parent-queue=none rate-limit="+rate);
                    simpanProfileHotspot(nama,rate,serverHotspot,share,up);
                }else{
                    Toast.makeText(profileHotspotAdmin.this, "Anda belum terhubung pada mikrotik, periksa konfigurasi login Mikrotik", Toast.LENGTH_SHORT).show();
                }

                Log.d(LOG_TAG, "Login Mikrotik Berhasil");
            }
            catch (Exception e)
            {
                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot login mikrotik");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //tvResult.setText("End");
        }
    }
    private void simpanProfileHotspot(String nama, String rate, String serverHotspot, String share, String up) {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        HashMap<String,Object> profile=new HashMap<>();
        profile.put("namaProfile",nama);
        profile.put("rateLimit",rate);
        profile.put("server",serverHotspot);
        profile.put("shareUser",share);
        profile.put("uptime",up);
        db.collection("configHotspot")
                .add(profile)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(profileHotspotAdmin.this, "Data Profile Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void ambilDataProfileHotspot() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("configHotspot")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    @SuppressLint("NotifyDataSetChanged")
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                ProfileHotspotAdmin_model profileHotspotAdmin_model=new ProfileHotspotAdmin_model(
                                        queryDocumentSnapshot.getString("namaProfile"),
                                        queryDocumentSnapshot.getString("rateLimit"),
                                        queryDocumentSnapshot.getString("server"),
                                        queryDocumentSnapshot.getString("shareUser"),
                                        queryDocumentSnapshot.getString("uptime"),
                                        queryDocumentSnapshot.getId());
                                list.add(profileHotspotAdmin_model);
                            }
                            profileHotspotAdminAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(profileHotspotAdmin.this, "gagal mengambil data profile hotspot", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}