package com.project.kresnahotspot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.Voucher;

import java.util.List;

public class ManagementPaketAdapter extends RecyclerView.Adapter<ManagementPaketAdapter.MyViewHolder> {
    private Context context;
    private List<Voucher> list;

    public ManagementPaketAdapter(Context context, List<Voucher> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_management_paket_admin,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Voucher voucher=list.get(position);
        String nama=voucher.getNama();
        String kecepatan=voucher.getKecepatan();
        String durasi=voucher.getDurasi();
        String harga=voucher.getHarga();
        String idConfig=voucher.getIdConfig();

        holder.nama.setText(nama);
        holder.kecepatan.setText(kecepatan);
        holder.durasi.setText(durasi);
        holder.harga.setText(harga);

        holder.DetailVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailVoucher(idConfig);
            }
        });

        holder.DeleteVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteVoucher(voucher);

            }
        });

    }

    private void deleteVoucher(Voucher voucher) {
        View view=LayoutInflater.from(context).inflate(R.layout.dialog_delete_voucher_admin,null);
        Button ya,tidak;
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        ya=view.findViewById(R.id.dialogDeleteVoucher_buttonIya);
        tidak=view.findViewById(R.id.dialogDeleteVoucher_buttonTidak);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);

        AlertDialog dialog= builder.create();
        dialog.show();

        ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Voucher").document(voucher.getId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Voucher Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, "Voucher Tidak Berhasil Dihapus", Toast.LENGTH_SHORT).show();
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

    private void detailVoucher(String idConfig) {
        View view=LayoutInflater.from(context).inflate(R.layout.dialog_detail_konfigurasi_voucher_admin,null);
        TextView profile,rateLimit,server,shareUser,uptime;
        Button close;

        profile=view.findViewById(R.id.dialogDetailVoucher_profile);
        rateLimit=view.findViewById(R.id.dialogDetailVoucher_rateLimit);
        server=view.findViewById(R.id.dialogDetailVoucher_serverHotspot);
        shareUser=view.findViewById(R.id.dialogDetailVoucher_shareUser);
        uptime=view.findViewById(R.id.dialogDetailVoucher_uptime);
        close=view.findViewById(R.id.dialogDetailVoucher_buttonClose);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);

        AlertDialog dialog= builder.create();
        dialog.show();

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("configHotspot").document(idConfig)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            profile.setText(documentSnapshot.getString("namaProfile"));
                            rateLimit.setText(documentSnapshot.getString("rateLimit"));
                            server.setText(documentSnapshot.getString("server"));
                            shareUser.setText(documentSnapshot.getString("shareUser"));
                            uptime.setText(documentSnapshot.getString("uptime"));
                        }
                    }
                });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama, kecepatan, durasi, harga;
        Button DetailVoucher,DeleteVoucher;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView) itemView.findViewById(R.id.managementPaket_namaPaket);
            kecepatan=(TextView) itemView.findViewById(R.id.managementPaket_kecepatanPaket);
            durasi=(TextView) itemView.findViewById(R.id.managementPaket_durasiPaket);
            harga=(TextView) itemView.findViewById(R.id.managementPaket_hargaPaket);
            DetailVoucher=(Button) itemView.findViewById(R.id.managementPaket_ButtonDetail);
            DeleteVoucher=(Button) itemView.findViewById(R.id.managementPaket_ButtonDelete);

        }
    }
}
