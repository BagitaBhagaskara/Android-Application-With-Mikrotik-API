package com.project.kresnahotspot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.PembelianPoint;

import java.util.List;

public class HistoryPermintaanPointAdapter_Admin extends RecyclerView.Adapter<HistoryPermintaanPointAdapter_Admin.MyViewHolder>{
    Context context;
    List<PembelianPoint> list;

    public HistoryPermintaanPointAdapter_Admin(Context context, List<PembelianPoint>list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryPermintaanPointAdapter_Admin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.row_history_permintaan_point_admin,parent,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryPermintaanPointAdapter_Admin.MyViewHolder holder, int position) {
        PembelianPoint pembelianPoint = list.get(position);
        String idPembelianPoint= pembelianPoint.getIdPembelianPoint();
        String tanggalPembelian=pembelianPoint.getTanggal();
        String jumlahPembelianPoint=pembelianPoint.getJumlahPoint();
        String metodePembayaran=pembelianPoint.getMetodePembayaran();
        String status=pembelianPoint.getStatus();
        String alasanTolak=pembelianPoint.getAlasanTolak();



        if(status.equals("Menunggu Pembayaran")){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }else if(status.equals("Diproses")){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }else if(status.equals("Ditolak")){
            holder.status.setTextColor(context.getResources().getColor(R.color.merah));
            holder.visibilityAlasan.setVisibility(View.VISIBLE);
        }else if(status.equals("Sukses")){
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }

        ambilDataNamaUser(pembelianPoint,holder.namaUser);

        holder.idPembelian.setText(idPembelianPoint);
        holder.jumlahPoint.setText(jumlahPembelianPoint);
        holder.tanggal.setText((tanggalPembelian));
        holder.metode.setText(metodePembayaran);
        holder.status.setText(status);
        holder.alasanTolak.setText(alasanTolak);

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
        TextView idPembelian, tanggal,namaUser, jumlahPoint, metode,status,alasanTolak;
        LinearLayout visibilityAlasan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            idPembelian=(TextView) itemView.findViewById(R.id.historyPermintaanPoint_idPembelian);
            tanggal=(TextView) itemView.findViewById(R.id.historyPermintaanPoint_tanggal);
            namaUser=(TextView) itemView.findViewById(R.id.historyPermintaanPoint_nama);
            jumlahPoint=(TextView) itemView.findViewById(R.id.historyPermintaanPoint_jumlahPoint);
            metode=(TextView) itemView.findViewById(R.id.historyPermintaanPoint_metode);
            status=(TextView) itemView.findViewById(R.id.historyPermintaanPoint_status);
            alasanTolak=(TextView) itemView.findViewById(R.id.historyPermintaanPoint_alasanPenolakan);
            visibilityAlasan=(LinearLayout) itemView.findViewById(R.id.historyPermintaanPoint_alasanPenolakanVisibility);

        }

    }
}
