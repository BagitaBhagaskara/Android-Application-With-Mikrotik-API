package com.project.kresnahotspot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.Voucher;

import org.w3c.dom.Text;

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

        holder.nama.setText(nama);
        holder.kecepatan.setText(kecepatan);
        holder.durasi.setText(durasi);
        holder.harga.setText(harga);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama, kecepatan, durasi, harga;
        Button EditVoucher,DeleteVoucher;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView) itemView.findViewById(R.id.managementPaket_namaPaket);
            kecepatan=(TextView) itemView.findViewById(R.id.managementPaket_kecepatanPaket);
            durasi=(TextView) itemView.findViewById(R.id.managementPaket_durasiPaket);
            harga=(TextView) itemView.findViewById(R.id.managementPaket_hargaPaket);
            EditVoucher=(Button) itemView.findViewById(R.id.managementPaket_ButtonEdit);
            DeleteVoucher=(Button) itemView.findViewById(R.id.managementPaket_ButtonDelete);

        }
    }
}
