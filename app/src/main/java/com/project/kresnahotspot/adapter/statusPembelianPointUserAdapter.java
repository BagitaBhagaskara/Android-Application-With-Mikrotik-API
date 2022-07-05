package com.project.kresnahotspot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.PembelianPoint;

import java.util.List;

public class statusPembelianPointUserAdapter extends RecyclerView.Adapter<statusPembelianPointUserAdapter.MyViewHolder> {
    Context context;
    List<PembelianPoint> list;

    public statusPembelianPointUserAdapter(Context context, List<PembelianPoint>list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public statusPembelianPointUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_status_pembelian_point_user,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull statusPembelianPointUserAdapter.MyViewHolder holder, int position) {
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
        }else if(status.equals("Ditolak")){
            holder.status.setTextColor(context.getResources().getColor(R.color.merah));
            holder.visibilityAlasan.setVisibility(View.VISIBLE);
        }else if(status.equals("Sukses")){
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }else if(status.equals("Diproses")){
            holder.status.setTextColor(context.getResources().getColor(R.color.biruHotspotActive));
        }

        holder.idPembelian.setText(idPembelianPoint);
        holder.tanggal.setText(tanggalPembelian);
        holder.jumlahPoint.setText(jumlahPembelianPoint);
        holder.metode.setText(metodePembayaran);
        holder.status.setText(status);
        holder.alasanTolak.setText(alasanTolak);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView idPembelian, tanggal, jumlahPoint, metode,status,alasanTolak;
        LinearLayout visibilityAlasan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idPembelian=(TextView) itemView.findViewById(R.id.statusPemblianPointUser_idPembelian);
            tanggal=(TextView) itemView.findViewById(R.id.statusPemblianPointUser_tanggal);
            jumlahPoint=(TextView) itemView.findViewById(R.id.statusPemblianPointUser_jumlahPoint);
            metode=(TextView) itemView.findViewById(R.id.statusPemblianPointUser_metode);
            status=(TextView) itemView.findViewById(R.id.statusPemblianPointUser_status);
            alasanTolak=(TextView) itemView.findViewById(R.id.statusPemblianPointUser_alasan);
            visibilityAlasan=(LinearLayout) itemView.findViewById(R.id.statusPemblianPointUser_alasanVisibility);

        }
    }
}
