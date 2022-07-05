package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.api.Distribution;
import com.google.api.Documentation;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class tambahVoucher_admin extends AppCompatActivity {

    Button simpan;
    ImageView kembali;
    LinearLayout warning;
    EditText namaVoucher,durasiVoucher,kecepatanVoucher,hargaVoucher;
    Spinner profileSpinner;
    private ArrayList<String>arrayProfile;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_voucher_admin);

        warning=findViewById(R.id.tambahVoucher_warning);
        kembali=findViewById(R.id.tambahVoucher_kembali);
        namaVoucher=findViewById(R.id.tambahVoucher_namaVoucher);
        durasiVoucher=findViewById(R.id.tambahVoucher_durasiVoucher);
        kecepatanVoucher=findViewById(R.id.tambahVoucher_kecepatanVoucher);
        hargaVoucher=findViewById(R.id.tambahVoucher_hargaVoucher);
        profileSpinner=findViewById(R.id.tambahVoucher_spinnerProfile);
        simpan=findViewById(R.id.tambahVoucher_buttonSimpan);

        setDataProfileSpinner(profileSpinner);

        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ambilDataProfileSpinner(profileSpinner.getSelectedItem().toString());
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
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama=namaVoucher.getText().toString();
                String durasi=durasiVoucher.getText().toString();
                String kecepatan=kecepatanVoucher.getText().toString();
                String harga=hargaVoucher.getText().toString();

                if(nama.equals("")){
                    Toast.makeText(tambahVoucher_admin.this, "Nama Voucher Belum Terisi", Toast.LENGTH_SHORT).show();
                }else if(durasi.equals("")){
                    Toast.makeText(tambahVoucher_admin.this, "Durasi Voucher Belum Terisi", Toast.LENGTH_SHORT).show();
                }else if(kecepatan.equals("")){
                    Toast.makeText(tambahVoucher_admin.this, "Kecepatan Voucher Belum Terisi", Toast.LENGTH_SHORT).show();
                }else if(harga.equals("")){
                Toast.makeText(tambahVoucher_admin.this, "Harga Voucher Belum Terisi", Toast.LENGTH_SHORT).show();
                }else{
                    simpanVoucher(profileSpinner.getSelectedItem().toString(),nama,durasi,kecepatan,harga);
                }
            }
        });
    }

    private void ambilDataProfileSpinner(String profile) {
        TextView rateLimit,shareuser,uptime;
        rateLimit=findViewById(R.id.tambahVoucher_configRateLimit);
        shareuser=findViewById(R.id.tambahVoucher_configShareUser);
        uptime=findViewById(R.id.tambahVoucher_configUptime);
        db.collection("configHotspot")
                .whereEqualTo("namaProfile",profile)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                rateLimit.setText(documentSnapshot.getString("rateLimit"));
                                shareuser.setText(documentSnapshot.getString("shareUser"));
                                uptime.setText(documentSnapshot.getString("uptime"));
                            }
                        }else{
                            Toast.makeText(tambahVoucher_admin.this, "gagal mengambil data configHotspot", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void simpanVoucher(String Profile, String nama, String durasi, String kecepatan, String harga) {
        View view= LayoutInflater.from(tambahVoucher_admin.this).inflate(R.layout.dialog_tambah_voucher_admin,null);
        Button iya, tidak;


        iya=(Button)view.findViewById(R.id.dialogTambahVoucher_buttonIya);
        tidak=(Button)view.findViewById(R.id.dialogTambahVoucher_buttonTidak);

        AlertDialog.Builder builder=new AlertDialog.Builder(tambahVoucher_admin.this);
        builder.setView(view);

        AlertDialog dialog=builder.create();
        dialog.show();

        iya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("configHotspot")
                        .whereEqualTo("namaProfile",Profile)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                                        String idProfile=queryDocumentSnapshot.getId();
                                        HashMap<String,Object> Voucher=new HashMap<>();
                                        Voucher.put("namaVoucher",nama);
                                        Voucher.put("durasiVoucher",durasi);
                                        Voucher.put("kecepatanVoucher",kecepatan);
                                        Voucher.put("hargaVoucher",harga);
                                        Voucher.put("idConfig",idProfile);
                                        db.collection("Voucher")
                                                .add(Voucher)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(tambahVoucher_admin.this, "Voucher Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(tambahVoucher_admin.this, "Voucher Gagal Ditambahkan, Periksa Koneksi Internet", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        });

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void setDataProfileSpinner(Spinner profileSpinner) {
        arrayProfile =new ArrayList<>();
        ArrayAdapter<String> profileAdapter = new ArrayAdapter<String>(tambahVoucher_admin.this, android.R.layout.simple_spinner_item, arrayProfile);
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
                            profileSpinner.setAdapter(profileAdapter);

                        }else{
                            Toast.makeText(tambahVoucher_admin.this, "gagal mengambil data spinner", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}