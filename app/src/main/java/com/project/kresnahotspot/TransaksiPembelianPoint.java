package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private ImageView gambar,kembali;
    ArrayList<String>arrayAlatPembayaran;
    Spinner spinnerPembayaran;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_pembelian_point);
        transaksiPembelianPoint=findViewById(R.id.itemTransaksiPembelianPoint);
        pembelianPointAdapter=new PembelianPointAdapter(TransaksiPembelianPoint.this,list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(TransaksiPembelianPoint.this,LinearLayoutManager.VERTICAL,false);
        transaksiPembelianPoint.setLayoutManager(layoutManager);
        transaksiPembelianPoint.setAdapter(pembelianPointAdapter);
        kembali=findViewById(R.id.transaksiPembelianPoint_kembali);



        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        pembelianPointAdapter.setDialog(new PembelianPointAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                View view=LayoutInflater.from(TransaksiPembelianPoint.this).inflate(R.layout.dialog_pembayaran_point,null);
                TextView metodeBayar, totalBayar;
                Button upload, detail,close;
                PembelianPoint pembelianPoint= list.get(pos);
                String metode1=pembelianPoint.getMetodePembayaran();
                String total=pembelianPoint.getJumlahPoint();
                metodeBayar=(TextView) view.findViewById(R.id.dialog_pembayaranMetode);
                gambar=(ImageView) view.findViewById(R.id.dialog_pembayaranImage);
                upload=(Button) view.findViewById(R.id.dialog_pembayaranButtonUpload);
                spinnerPembayaran= (Spinner) view.findViewById(R.id.dialog_pembayaranSpinner);
                detail=(Button) view.findViewById(R.id.dialogkonfirmasipembayaran_buttonDetail);
                close=(Button) view.findViewById(R.id.dialogPembayaranClose);
                AlertDialog.Builder builder=new AlertDialog.Builder(TransaksiPembelianPoint.this);
                builder.setView(view);



                metodeBayar.setText(metode1);
                if(!pembelianPoint.getStatus().equals("Menunggu Pembayaran")){
                    Glide.with(TransaksiPembelianPoint.this).load(pembelianPoint.getUrl()).into(gambar);
                }

                AlertDialog dialog= builder.create();
                dialog.show();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tampilkanDataAlatPembayaran(spinnerPembayaran);

                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        tampilDetailPembayaran(total,spinnerPembayaran.getSelectedItem().toString());

                    }
                });
                gambar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateFotoBuktiPembayaran(TransaksiPembelianPoint.this);
                    }
                });
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadFoto(pembelianPoint.getIdPembelianPoint());
                        dialog.dismiss();
                    }
                });
            }
        });

        ambilDataPembelianPoint();
    }

    private void tampilDetailPembayaran(String totalPembayaran, String spinnerSelect) {
        View viewDetail=LayoutInflater.from(TransaksiPembelianPoint.this).inflate(R.layout.dialog_detail_pembayaran_poin,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(TransaksiPembelianPoint.this);
        builder.setView(viewDetail);


        TextView jumlahpoin, media_pembayaran, total, nomor, atasnama;
        Button close;
        jumlahpoin=(TextView) viewDetail.findViewById(R.id.dialogDetailPembayaran_jumlahPoin);
        media_pembayaran=(TextView) viewDetail.findViewById(R.id.dialogDetailPembayaran_mediaPembayaran);
        total=(TextView) viewDetail.findViewById(R.id.dialogDetailPembayaran_totalPembayaran);
        nomor=(TextView) viewDetail.findViewById(R.id.dialogDetailPembayaran_nomorpembayaran);
        atasnama=(TextView) viewDetail.findViewById(R.id.dialogDetailPembayaran_atasNama);
        close=(Button) viewDetail.findViewById(R.id.dialogDetailPembayaran_buttonClose);

        AlertDialog dialog= builder.create();
        dialog.show();
       total.setText("Rp. "+totalPembayaran);
       jumlahpoin.setText(totalPembayaran);
       media_pembayaran.setText(spinnerSelect);
       tampilkanNomorPembayaran(nomor,atasnama,spinnerSelect);

       close.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dialog.dismiss();
           }
       });

    }

    private void tampilkanNomorPembayaran(TextView nomor, TextView atasnama, String spinnerSelect) {
        db.collection("dataAlatPembayaran")
                .whereEqualTo("mediaPembayaran",spinnerSelect)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                nomor.setText(documentSnapshot.getString("nomorPembayaran"));
                                atasnama.setText(documentSnapshot.getString("atasNama"));
                            }
                        }else{
                            Toast.makeText(TransaksiPembelianPoint.this, "gagal mengambil data configHotspot", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void tampilkanDataAlatPembayaran(Spinner spinnerPembayaran) {
        arrayAlatPembayaran =new ArrayList<>();
        ArrayAdapter<String> pembayaranAdapter = new ArrayAdapter<String>(TransaksiPembelianPoint.this, android.R.layout.simple_spinner_item, arrayAlatPembayaran);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("dataAlatPembayaran")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot :task.getResult()){
                                String mediaPembayaran=documentSnapshot.getString("mediaPembayaran");
                                arrayAlatPembayaran.add(mediaPembayaran);
                            }
                            pembayaranAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerPembayaran.setAdapter(pembayaranAdapter);

                        }else{
                            Toast.makeText(TransaksiPembelianPoint.this, "gagal mengambil data spinner", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadFoto(String idPembelianPoint) {
        gambar.setDrawingCacheEnabled(true);
        gambar.buildDrawingCache();
        Bitmap bitmap=((BitmapDrawable)gambar.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[]data=byteArrayOutputStream.toByteArray();

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference("buktiPembayaranPoint").child("BPP"+new Date().getTime()+".jpeg");

        UploadTask uploadTask =storageReference.putBytes(data);
        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if(taskSnapshot.getMetadata()!=null){
                    if(taskSnapshot.getMetadata().getReference()!=null){
                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
                                        uploadFotoFireStore(task.getResult().toString(),idPembelianPoint);
                                        Toast.makeText(TransaksiPembelianPoint.this, ""+idPembelianPoint, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });
    }

    private void uploadFotoFireStore(String urlFoto, String idPembelian) {
        DocumentReference documentReference = db.collection("pembelianPoint").document(idPembelian);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            String idUser= documentSnapshot.getString("idUser");
                            String jumlahPembelian= documentSnapshot.getString("jumlahPembelianPoint");
                            String metodePembayaran= documentSnapshot.getString("metodePembayaran");
                            String status= "Diproses";
                            String tanggal= documentSnapshot.getString("tanggal");
                            String totalPembayaran= documentSnapshot.getString("totalPembayaran");
                            String alasanTolak=null;
                            Map<String,Object>dataPembelianPoint=new HashMap<>();
                            dataPembelianPoint.put("idUser",idUser);
                            dataPembelianPoint.put("jumlahPembelianPoint",jumlahPembelian);
                            dataPembelianPoint.put("metodePembayaran",metodePembayaran);
                            dataPembelianPoint.put("status",status);
                            dataPembelianPoint.put("tanggal",tanggal);
                            dataPembelianPoint.put("totalPembayaran",totalPembayaran);
                            dataPembelianPoint.put("UrlBuktiPembelian",urlFoto);
                            dataPembelianPoint.put("alasanDitolak",alasanTolak);
                            documentReference.set(dataPembelianPoint).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        ambilDataPembelianPoint();
                                    }
                                }
                            });

                            Toast.makeText(TransaksiPembelianPoint.this, "Foto Berhasil Diuplaod", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(TransaksiPembelianPoint.this, "Foto Gagal di Upload", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateFotoBuktiPembayaran(TransaksiPembelianPoint transaksiPembelianPoint) {
        final CharSequence[] items={"Ambil Foto","Pilih Dari Galeri","Batal"};
        AlertDialog.Builder builder=new AlertDialog.Builder(TransaksiPembelianPoint.this);
        builder.setTitle("Pilih Foto Bukti Pembayaran Poin");
        builder.setItems(items,(dialog,item)->{
            if (items[item].equals("Ambil Foto")){
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,10);
            }
           else if (items[item].equals("Pilih Dari Galeri")){
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i,"Pilih Foto"),20);

            }
           else if (items[item].equals("Batal")){
               dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10&&resultCode==RESULT_OK){
            final Bundle extras= data.getExtras();
            Thread thread = new Thread(()->{
                Bitmap bitmap =(Bitmap) extras.get("data");
                gambar.post(()->{
                    gambar.setImageBitmap(bitmap);

                });
            });
            thread.start();
        }
        if(requestCode==20&&resultCode==RESULT_OK&&data!=null){
            final Uri path= data.getData();
            Thread thread = new Thread(()->{
                try{
                    InputStream inputStream=getContentResolver().openInputStream(path);
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    gambar.post(()->{
                        gambar.setImageBitmap(bitmap);

                    });
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            });
            thread.start();
        }
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
                                        queryDocumentSnapshot.getString("status"),
                                        queryDocumentSnapshot.getString("UrlBuktiPembelian"),
                                        queryDocumentSnapshot.getString("alasanDitolak"));
                                list.add(pembelianPoint);
                            }
                            pembelianPointAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(TransaksiPembelianPoint.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

}