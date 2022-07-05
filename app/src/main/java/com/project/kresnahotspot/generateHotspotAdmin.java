package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.net.SocketFactory;

import examples.Config;
import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.impl.ApiConnectionImpl;

public class generateHotspotAdmin extends AppCompatActivity {
    private ArrayList<String>arrayProfile;
    private ArrayList<String>arrayServer;
    public static boolean cekLoginMikrotik=true;
    private static final String LOG_TAG = "Generate Hotspot";
    ApiConnection con;
//    ApiConnectionImpl con1;
    String usernameInput,passwordInput,profileInput,serverInput,uptime,kecepatan;
    ImageView kembali;
    LinearLayout warning;
    EditText user,password;
    TextView durasi,rateLimit;
    Button generate;
    Spinner profile,server;
    SharedPreferences getDataLoginMikrotik;
//    String dataSpinnerProfile,dataSpinnerServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_hotspot_admin);

        durasi=findViewById(R.id.generateHotspot_durasiHotspot);
        warning=findViewById(R.id.hotspot_generate_warning);
        kembali=findViewById(R.id.hotspot_generate_kembali);
        profile=findViewById(R.id.generateHotspot_spinnerProfile);
        server=findViewById(R.id.generateHotspot_spinnerServer);
        user=findViewById(R.id.generateHotspot_nama);
        password=findViewById(R.id.generateHotspot_password);
        generate=findViewById(R.id.generateHotspot_buttonGenerate);
        rateLimit=findViewById(R.id.generateHotspot_rateLimit);

        getDataLoginMikrotik= generateHotspotAdmin.this.getSharedPreferences("data_login_mikrotik", Context.MODE_PRIVATE);

        loginMikrotik loginMikrotik=new loginMikrotik();
        loginMikrotik.execute();

        setDataProfileSpinner(profile);
        setDataServerSpinner(server);

        if(cekLoginMikrotik==false){
            warning.setVisibility(View.VISIBLE);

        }else if(cekLoginMikrotik==true){
            warning.setVisibility(View.GONE);

        }

        profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ambilDataDurasidanRateLimit(profile.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateHotspot(user,password,profile,server);
            }
        });



    }

    private void ambilDataDurasidanRateLimit(String profile) {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("configHotspot")
                .whereEqualTo("namaProfile",profile)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot :task.getResult()){
                                uptime=documentSnapshot.getString("uptime");
                                kecepatan=documentSnapshot.getString("rateLimit");
                                rateLimit.setText(kecepatan);
                                durasi.setText(uptime);

                            }
                        }else{
                            Toast.makeText(generateHotspotAdmin.this, "gagal mengambil data Uptime", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void generateHotspot(EditText user, EditText password, Spinner profile, Spinner server) {
        loginMikrotik loginMikrotik=new loginMikrotik();
        loginMikrotik.execute();
        String dataSpinnerProfile=profile.getSelectedItem().toString();
       String dataSpinnerServer=server.getSelectedItem().toString();
       usernameInput=user.getText().toString();
       passwordInput=password.getText().toString();
       profileInput=dataSpinnerProfile;
       serverInput=dataSpinnerServer;
       String durasiHotspot=durasi.getText().toString();
//       String kecepatan=rateLimit.getText().toString();
       if(usernameInput.equals("")){
           Toast.makeText(generateHotspotAdmin.this, "Nama User Belum Terisi", Toast.LENGTH_SHORT).show();
       }else if(passwordInput.equals("")){
           Toast.makeText(generateHotspotAdmin.this, "Password Belum Terisi", Toast.LENGTH_SHORT).show();
       }else if(cekLoginMikrotik==true){
           generateUserHotspotMikrotik generate =new generateUserHotspotMikrotik();
           generate.execute();
           Toast.makeText(generateHotspotAdmin.this, "User Hotspot Berhasil Dibuat", Toast.LENGTH_SHORT).show();
           Log.d(LOG_TAG,"isian data uptimee: "+durasiHotspot);
           Log.d(LOG_TAG,"status cek login: "+cekLoginMikrotik);
       }else if(cekLoginMikrotik==false){
           Toast.makeText(generateHotspotAdmin.this, "Tidak Terhubung Pada Jaringan Mikrotik!", Toast.LENGTH_SHORT).show();


       }
    }
    class loginMikrotik extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                con =ApiConnection.connect(SocketFactory.getDefault(), getDataLoginMikrotik.getString("ipLoginMikrotik",null), getDataLoginMikrotik.getInt("portLoginMikrotikInt",0 ), 2000);
                con.login(getDataLoginMikrotik.getString("usernameLoginMikrotik",null), getDataLoginMikrotik.getString("passwordLoginMikrotik",null));
                Log.d(LOG_TAG, "Login Mikrotik Berhasil");
            }
            catch (Exception e)
            {
                cekLoginMikrotik=false;
                Log.d(LOG_TAG, "erorr: "+e);

                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot login mikrotik");
            }
            return null;
        }
    }
    class generateUserHotspotMikrotik extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String durasiHotspot=durasi.getText().toString();
            try
            {
                //cekLoginMikrotik=true;
                con.execute("/ip/hotspot/user/add server="+serverInput+" name=" +usernameInput + " password=" + passwordInput + " profile=" + profileInput + " limit-uptime=" + durasiHotspot);
                Toast.makeText(generateHotspotAdmin.this, "Hotspot "+usernameInput+" Berhasil Dibuat", Toast.LENGTH_SHORT).show();

            }
            catch (Exception e)
            {
                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot : "+serverInput+" "+usernameInput+" "+passwordInput+" "+profileInput+" "+uptime);
            }
            return null;
        }
    }
    int i=0;
    private void setDataServerSpinner(Spinner server) {
        arrayServer =new ArrayList<>();
        ArrayAdapter<String> ServerAdapter = new ArrayAdapter<String>(generateHotspotAdmin.this, android.R.layout.simple_spinner_item, arrayServer);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("configHotspot")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot :task.getResult()){
                                if(i!=0){
                                    String namaServer=documentSnapshot.getString("server");
                                    String sebelum=arrayServer.get(i);
                                    if(sebelum!=namaServer){
                                        arrayServer.add(namaServer);
                                    }
                                    i=i+1;
                                }else{
                                    String namaServer=documentSnapshot.getString("server");
                                    arrayServer.add(namaServer);
                                }

                            }
                            server.setAdapter(ServerAdapter);
                        }else{
                            Toast.makeText(generateHotspotAdmin.this, "gagal mengambil data spinner", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setDataProfileSpinner(Spinner profile) {
        arrayProfile =new ArrayList<>();
        ArrayAdapter<String> profileAdapter = new ArrayAdapter<String>(generateHotspotAdmin.this, android.R.layout.simple_spinner_item, arrayProfile);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("configHotspot")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot :task.getResult()){
                                String namaProfile=documentSnapshot.getString("namaProfile");
                                arrayProfile.add(namaProfile);
                            }
                            profileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            profile.setAdapter(profileAdapter);

                        }else{
                            Toast.makeText(generateHotspotAdmin.this, "gagal mengambil data spinner", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}