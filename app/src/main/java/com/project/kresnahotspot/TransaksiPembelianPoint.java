package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.kresnahotspot.adapter.PembelianPointAdapter;
import com.project.kresnahotspot.adapter.PenjualanAdapter;
import com.project.kresnahotspot.model.PembelianPoint;
import com.project.kresnahotspot.model.Penjualan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransaksiPembelianPoint extends AppCompatActivity /*implements PembelianPointAdapter.MyViewHolder.CallbackInterface*/ {
    public Object startActivityForResult;
    private RecyclerView transaksiPembelianPoint;
    private List<PembelianPoint>list=new ArrayList<>();
    private PembelianPointAdapter pembelianPointAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_pembelian_point);

        transaksiPembelianPoint=findViewById(R.id.itemTransaksiPembelianPoint);
        pembelianPointAdapter=new PembelianPointAdapter(TransaksiPembelianPoint.this,list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(TransaksiPembelianPoint.this,LinearLayoutManager.VERTICAL,false);

        transaksiPembelianPoint.setLayoutManager(layoutManager);
        transaksiPembelianPoint.setAdapter(pembelianPointAdapter);

        ambilDataPembelianPoint();

    }

    private void ambilDataPembelianPoint() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        FirebaseAuth auth=FirebaseAuth.getInstance();
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
                                PembelianPoint pembelianPoint=new PembelianPoint(queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("jumlahPembelianPoint"),
                                        queryDocumentSnapshot.getString("metodePembayaran"),
                                        queryDocumentSnapshot.getString("tanggal"),
                                        queryDocumentSnapshot.getString("status"));
                                list.add(pembelianPoint);
                            }
                            pembelianPointAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(TransaksiPembelianPoint.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        ImageView fotobukti;
        View view= LayoutInflater.from(TransaksiPembelianPoint.this).inflate(R.layout.dialog_pembayaran_point,null);
        fotobukti=(ImageView) view.findViewById(R.id.dialog_pembayaranImage);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10&&resultCode==RESULT_OK){
        }
        if(requestCode==20&&resultCode==RESULT_OK&&data!=null){
            final Uri path= data.getData();
            Thread thread = new Thread(()->{
                try{
                    InputStream inputStream=getContentResolver().openInputStream(path);
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    fotobukti.post(()->{
                        fotobukti.setImageBitmap(bitmap);
                    });
                }
                catch (IOException e){
                    Toast.makeText(TransaksiPembelianPoint.this, "gagal menampilkan gambar", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
//    private void uploadFoto (){
//
//    }
//    private void uploadFotoFireStore(String urlFoto) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String,Object> pembelianPoint=new HashMap<>();
//        pembelianPoint.put("fotoBuktiPembayaran",urlFoto);
//        db.collection("pembelianPoint").document(id)
//                .set(pembelianPoint)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(TransaksiPembelianPoint.this, "Foto Berhasil Diuplaod", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        }

}