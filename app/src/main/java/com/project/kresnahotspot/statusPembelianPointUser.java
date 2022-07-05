package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.adapter.HistoryPermintaanPointAdapter_Admin;
import com.project.kresnahotspot.adapter.statusPembelianPointUserAdapter;
import com.project.kresnahotspot.model.PembelianPoint;

import java.util.ArrayList;
import java.util.List;

public class statusPembelianPointUser extends AppCompatActivity {
    ImageView kembali;
    private RecyclerView statusPembelianPoint;
    private List<PembelianPoint> list=new ArrayList<>();
    private statusPembelianPointUserAdapter statusPembelianPointUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_pembelian_point_user);
        kembali=findViewById(R.id.statusPemblianPointUser_kembali);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        statusPembelianPoint=findViewById(R.id.itemStatusPembelianPointUser);
        statusPembelianPointUserAdapter =new statusPembelianPointUserAdapter(statusPembelianPointUser.this,list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(statusPembelianPointUser.this,LinearLayoutManager.VERTICAL,false);

        statusPembelianPoint.setLayoutManager(layoutManager);
        statusPembelianPoint.setAdapter(statusPembelianPointUserAdapter);

        ambilDataPembelianPoint();
    }

    private void ambilDataPembelianPoint() {
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        db.collection("pembelianPoint")
                .whereEqualTo("idUser",auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                PembelianPoint pembelianPoint=new PembelianPoint(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("jumlahPembelianPoint"),
                                        queryDocumentSnapshot.getString("metodePembayaran"),
                                        queryDocumentSnapshot.getString("tanggal"),
                                        queryDocumentSnapshot.getString("status"),
                                        queryDocumentSnapshot.getString("UrlBuktiPembelian"),
                                        queryDocumentSnapshot.getString("alasanDitolak"));
                                list.add(pembelianPoint);
                            }
                            statusPembelianPointUserAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(statusPembelianPointUser.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}