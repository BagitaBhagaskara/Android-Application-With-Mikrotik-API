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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.adapter.HistoryPermintaanPointAdapter_Admin;
import com.project.kresnahotspot.adapter.PermintaanPembelianPointAdapter_Admin;
import com.project.kresnahotspot.model.PembelianPoint;

import java.util.ArrayList;
import java.util.List;

public class historyPermintaanPoint_admin extends AppCompatActivity {
    private RecyclerView permintaanPoint;
    private List<PembelianPoint> list=new ArrayList<>();
    private HistoryPermintaanPointAdapter_Admin historyPermintaanPointAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_permintaan_point_admin);
        ImageView kembali;
        kembali=findViewById(R.id.historyPermintaanPoint_kembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        permintaanPoint=findViewById(R.id.itemHistoryPermintaanPointUser);
        historyPermintaanPointAdapter=new HistoryPermintaanPointAdapter_Admin(historyPermintaanPoint_admin.this,list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(historyPermintaanPoint_admin.this,LinearLayoutManager.VERTICAL,false);

        permintaanPoint.setLayoutManager(layoutManager);
        permintaanPoint.setAdapter(historyPermintaanPointAdapter);

        ambilDataPembelianPoint();

    }

    private void ambilDataPembelianPoint() {
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        db.collection("pembelianPoint")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                PembelianPoint pembelianPoint=new PembelianPoint(queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("jumlahPembelianPoint"),
                                        queryDocumentSnapshot.getString("metodePembayaran"),
                                        queryDocumentSnapshot.getString("tanggal"),
                                        queryDocumentSnapshot.getString("status"),
                                        queryDocumentSnapshot.getString("UrlBuktiPembelian"),
                                        queryDocumentSnapshot.getString("alasanDitolak"));
                                pembelianPoint.setIdUser(queryDocumentSnapshot.getString("idUser"));
                                list.add(pembelianPoint);
                            }
                            historyPermintaanPointAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(historyPermintaanPoint_admin.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}