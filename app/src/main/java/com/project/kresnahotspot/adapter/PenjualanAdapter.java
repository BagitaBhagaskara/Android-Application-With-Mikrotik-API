package com.project.kresnahotspot.adapter;

import android.content.Context;
import android.hardware.lights.LightState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.Penjualan;

import java.util.List;

public class PenjualanAdapter extends RecyclerView.Adapter<PenjualanAdapter.MyViewHolder> {
    private Context context;
    private List<Penjualan>list;

    public PenjualanAdapter(Context context, List<Penjualan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.row_voucher_penjualan,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Penjualan penjualan=list.get(position);
        ambilDataVoucher(penjualan,holder.namaVoucher,holder.kecepatanVoucher,holder.durasiVoucher);
        holder.idPenjualan.setText(penjualan.getIdPenjualan());
        holder.tanggalVoucher.setText(penjualan.getTanggalPembelian());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogDetailVoucher(penjualan);
            }
        });


    }

    private void dialogDetailVoucher(Penjualan penjualan) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_detail_voucher,null);
        TextView namaVoucher, durasi, kecepatan, username, password ;
        namaVoucher=(TextView) view.findViewById(R.id.dialogVoucher_namaVoucher);
        durasi=(TextView) view.findViewById(R.id.dialogVoucher_durasi);
        kecepatan=(TextView) view.findViewById(R.id.dialogVoucher_kecepatan);
        username=(TextView) view.findViewById(R.id.dialogVoucher_username);
        password=(TextView) view.findViewById(R.id.dialogVoucher_password);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);


        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference reference=db.collection("Voucher").document(penjualan.getIdVoucher());
        DocumentReference documentReference=db.collection("penjualanVoucher").document(penjualan.getIdPenjualan());
        documentReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            username.setText(documentSnapshot.getString("Username Login Hotspot"));
                            password.setText(documentSnapshot.getString("Password Login Hotspot"));
                            reference.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot1=task.getResult();
                                                namaVoucher.setText(documentSnapshot1.getString("namaVoucher"));
                                                kecepatan.setText(documentSnapshot1.getString("kecepatanVoucher"));
                                                durasi.setText(documentSnapshot1.getString("durasiVoucher"));

                                            }else{
                                                Toast.makeText(context, "gagal mengabil data voucher", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(context, "Data voucher gagal diambil", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog dialog= builder.create();
        dialog.show();


    }




    private void ambilDataVoucher(Penjualan penjualan,TextView namaVoucher, TextView kecepatanVoucher, TextView durasiVoucher) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference reference=db.collection("Voucher").document(penjualan.getIdVoucher());
            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot= task.getResult();
                                namaVoucher.setText(documentSnapshot.getString("namaVoucher"));
                                durasiVoucher.setText(documentSnapshot.getString("durasiVoucher"));
                                kecepatanVoucher.setText(documentSnapshot.getString("kecepatanVoucher"));
                            }else {
                                Toast.makeText(context, "Data voucher gagal diambil", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView idPenjualan, namaVoucher, durasiVoucher, kecepatanVoucher, tanggalVoucher, username, password;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            idPenjualan=(TextView) itemView.findViewById(R.id.penjualanVoucher);
            namaVoucher=(TextView) itemView.findViewById(R.id.penjualan_namaVoucher);
            durasiVoucher=(TextView) itemView.findViewById(R.id.penjualan_durasiVoucher);
            kecepatanVoucher=(TextView) itemView.findViewById(R.id.penjualan_kecepatanVoucher);
            tanggalVoucher=(TextView) itemView.findViewById(R.id.penjualan_tanggal);



        }
    }
}
