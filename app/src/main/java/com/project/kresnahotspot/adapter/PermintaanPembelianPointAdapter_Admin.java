package com.project.kresnahotspot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.kresnahotspot.R;
import com.project.kresnahotspot.adminFragmentPointUser;
import com.project.kresnahotspot.model.PembelianPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermintaanPembelianPointAdapter_Admin extends RecyclerView.Adapter<PermintaanPembelianPointAdapter_Admin.MyViewHolder> {
    Context context;
    List<PembelianPoint> list;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    public PermintaanPembelianPointAdapter_Admin(Context context, List<PembelianPoint> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PermintaanPembelianPointAdapter_Admin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_admin_permintaan_pembelian_point,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PermintaanPembelianPointAdapter_Admin.MyViewHolder holder, int position) {
        PembelianPoint pembelianPoint = list.get(position);
        String idPembelianPoint= pembelianPoint.getIdPembelianPoint();
        String tanggalPembelian=pembelianPoint.getTanggal();
        String jumlahPembelianPoint=pembelianPoint.getJumlahPoint();
        String metodePembayaran=pembelianPoint.getMetodePembayaran();
        String status=pembelianPoint.getStatus();

        if(status.equals("Menunggu Pembayaran")){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
        else if(status.equals("Sukses")){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
        else if(status.equals("Ditolak")){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }else if(status.equals("Diproses")){
            holder.status.setTextColor(context.getResources().getColor(R.color.biruHotspotActive));
        }

        ambilDataNamaUser(pembelianPoint,holder.namaUser);

        holder.idPembelian.setText(idPembelianPoint);
        holder.jumlahPoint.setText(jumlahPembelianPoint);
        holder.tanggal.setText((tanggalPembelian));
        holder.metode.setText(metodePembayaran);
        holder.status.setText(status);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfirmasiDialogPembelianPoint(pembelianPoint);
            }
        });

    }

    private void konfirmasiDialogPembelianPoint(PembelianPoint pembelianPoint) {
        View view=LayoutInflater.from(context).inflate(R.layout.dialog_permintaan_pembelian_point_admin,null);
        TextView nama, point, bayar,tanggal;
        Button setuju, tolak;
        EditText alasan;
        PhotoView gambar;

        nama=(TextView) view.findViewById(R.id.dialog_pembelianPointAdmin_namaUser);
        point=(TextView) view.findViewById(R.id.dialog_pembelianPointAdmin_jumlahPoint);
        bayar=(TextView) view.findViewById(R.id.dialog_pembelianPointAdmin_totalBayar);
        tanggal=(TextView) view.findViewById(R.id.dialog_pembelianPointAdmin_tanggal);
        setuju=(Button) view.findViewById(R.id.dialog_pembelianPointAdmin_buttonSetuju);
        tolak=(Button) view.findViewById(R.id.dialog_pembelianPointAdmin_buttonTolak);
        gambar=(PhotoView) view.findViewById(R.id.dialog_pembelianPointAdmin_gambar);
        alasan=(EditText) view.findViewById(R.id.dialog_pembelianPointAdmin_inputAlasan);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);

        Glide.with(context)
                .load(pembelianPoint.getUrl())
                .into(gambar);

        nama.setText(pembelianPoint.getNamaUser());
        point.setText(pembelianPoint.getJumlahPoint());
        bayar.setText("Rp."+pembelianPoint.getJumlahPoint());
        tanggal.setText(pembelianPoint.getTanggal());
        AlertDialog dialog1=builder.create();
        dialog1.show();

        setuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = LayoutInflater.from(context).inflate(R.layout.dialog_konfirmasi_pembelian_point_admin,null);
                Button ya;
                ya=(Button) view.findViewById(R.id.dialog_konfirmasiPemblianPoint);
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setView(view);
                AlertDialog dialog=builder.create();
                dialog.show();
                ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updatePointUser(pembelianPoint,dialog,dialog1);
                        dialog.dismiss();
                    }
                });
            }
        });

        tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alasanTolak= alasan.getText().toString();
                if(alasanTolak.length()>0){
                    pembelianPointTolak(pembelianPoint,alasanTolak);
                }else{
                    Toast.makeText(context, "Berikan alasan penolakan", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void pembelianPointTolak(PembelianPoint pembelianPoint, String alasan) {
        DocumentReference documentReference=db.collection("pembelianPoint").document(pembelianPoint.getIdPembelianPoint());
        documentReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot1=task.getResult();
                            String idUser= documentSnapshot1.getString("idUser");
                            String jumlahPembelian= documentSnapshot1.getString("jumlahPembelianPoint");
                            String metodePembayaran= documentSnapshot1.getString("metodePembayaran");
                            String status= "Ditolak";
                            String urlFoto=documentSnapshot1.getString("UrlBuktiPembalianPoint");
                            String tanggal= documentSnapshot1.getString("tanggal");
                            String totalPembayaran= documentSnapshot1.getString("totalPembayaran");
                            Map<String,Object> dataPembelianPoint=new HashMap<>();
                            dataPembelianPoint.put("idUser",idUser);
                            dataPembelianPoint.put("jumlahPembelianPoint",jumlahPembelian);
                            dataPembelianPoint.put("metodePembayaran",metodePembayaran);
                            dataPembelianPoint.put("status",status);
                            dataPembelianPoint.put("tanggal",tanggal);
                            dataPembelianPoint.put("totalPembayaran",totalPembayaran);
                            dataPembelianPoint.put("UrlBuktiPembelian",urlFoto);
                            dataPembelianPoint.put("alasanDitolak",alasan);
                            documentReference.set(dataPembelianPoint)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            notifyDataSetChanged();
                                        }
                                    });
                        }
                    }
                });

    }


    private void updatePointUser(PembelianPoint pembelianPoint, AlertDialog alertDialog1, AlertDialog dialog) {

        DocumentReference documentReference=db.collection("dataUser").document(pembelianPoint.getIdUser());
        documentReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Point user berhasil ditambahkan sebesar "+pembelianPoint.getJumlahPoint()+"point", Toast.LENGTH_SHORT).show();
                            DocumentSnapshot documentSnapshot=task.getResult();
                            String point=documentSnapshot.getString("coin");
                            int tambahPoint = Integer.parseInt(point)+Integer.parseInt(pembelianPoint.getJumlahPoint());
                            HashMap<String,Object> dataUser=new HashMap<>();
                            dataUser.put("coin",Integer.toString(tambahPoint));
                            dataUser.put("emailUser",documentSnapshot.getString("emailUser"));
                            dataUser.put("statusLogin",documentSnapshot.getString("statusLogin"));
                            dataUser.put("namaUser",documentSnapshot.getString("namaUser"));
                            dataUser.put("phoneUser",documentSnapshot.getString("phoneUser"));
                            documentReference.set(dataUser);
                            dialog.dismiss();
                            alertDialog1.dismiss();
                            DocumentReference documentReference1=db.collection("pembelianPoint").document(pembelianPoint.getIdPembelianPoint());
                            documentReference1.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot1=task.getResult();
                                                String idUser= documentSnapshot1.getString("idUser");
                                                String jumlahPembelian= documentSnapshot1.getString("jumlahPembelianPoint");
                                                String metodePembayaran= documentSnapshot1.getString("metodePembayaran");
                                                String status= "Sukses";
                                                String alasanTolak=null;
                                                String urlFoto=documentSnapshot1.getString("UrlBuktiPembalianPoint");
                                                String tanggal= documentSnapshot1.getString("tanggal");
                                                String totalPembayaran= documentSnapshot1.getString("totalPembayaran");
                                                Map<String,Object> dataPembelianPoint=new HashMap<>();
                                                dataPembelianPoint.put("idUser",idUser);
                                                dataPembelianPoint.put("jumlahPembelianPoint",jumlahPembelian);
                                                dataPembelianPoint.put("metodePembayaran",metodePembayaran);
                                                dataPembelianPoint.put("status",status);
                                                dataPembelianPoint.put("tanggal",tanggal);
                                                dataPembelianPoint.put("totalPembayaran",totalPembayaran);
                                                dataPembelianPoint.put("UrlBuktiPembelian",urlFoto);
                                                dataPembelianPoint.put("alasanDitolak",alasanTolak);
                                                documentReference1.set(dataPembelianPoint)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        notifyDataSetChanged();
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(context, "Point user gagal ditambahkan", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void ambilDataNamaUser(PembelianPoint pembelianPoint, TextView namaUser) {

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference documentReference=db.collection("dataUser").document(pembelianPoint.getIdUser());
        documentReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                             pembelianPoint.setNamaUser(documentSnapshot.getString("namaUser"));
                             namaUser.setText(pembelianPoint.getNamaUser());
                        }else{
                            Toast.makeText(context, "gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView idPembelian, tanggal,namaUser, jumlahPoint, metode,status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idPembelian=(TextView) itemView.findViewById(R.id.permintaanPembelianPoint_idPembelian);
            jumlahPoint=(TextView) itemView.findViewById(R.id.permintaanPembelianPoint_jumlahPoint);
            metode=(TextView) itemView.findViewById(R.id.permintaanPembelianPoint_metode);
            tanggal=(TextView) itemView.findViewById(R.id.permintaanPembelianPoint_tanggal);
            namaUser=(TextView) itemView.findViewById(R.id.permintaanPembelianPoint_nama);
            status=(TextView)  itemView.findViewById(R.id.permintaanPembelianPoint_status);



        }
    }
}
