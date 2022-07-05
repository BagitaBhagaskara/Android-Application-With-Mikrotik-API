package com.project.kresnahotspot.adapter;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.kresnahotspot.HomeActivity;
import com.project.kresnahotspot.R;
import com.project.kresnahotspot.TransaksiPembelianPoint;
import com.project.kresnahotspot.model.PembelianPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PembelianPointAdapter extends RecyclerView.Adapter<PembelianPointAdapter.MyViewHolder> {
    Context context;
    List<PembelianPoint>list;
    Dialog dialog;


    public PembelianPointAdapter(Context context, List<PembelianPoint> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_transaksi_pembelian_point,parent,false);
        return new MyViewHolder(view);
    }

    public interface Dialog{
        void onClick(int pos);
    }
  public void setDialog(Dialog dialog){
        this.dialog=dialog;
  }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PembelianPoint pembelianPoint = list.get(position);
        String idPembalianPoint=pembelianPoint.getIdPembelianPoint();
        String jumlahPoint=pembelianPoint.getJumlahPoint();
        String metodePembayaran=pembelianPoint.getMetodePembayaran();
        String tanggal=pembelianPoint.getTanggal();
        String status=pembelianPoint.getStatus();

        holder.idPembelianPoint.setText(idPembalianPoint);
        holder.jumlahPoint.setText(jumlahPoint);
        holder.metodePembayaran.setText(metodePembayaran);
        holder.tanggal.setText(tanggal);

        if(status.equals("Menunggu Pembayaran")){
            holder.status.setTextColor(context.getResources().getColor(R.color.orange));
        }else if(status.equals("Diproses")){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            holder.status.setTextColor(context.getResources().getColor(R.color.biruHotspotActive));
        }else if(status.equals("Sukses")) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }else if(status.equals("Ditolak")) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            holder.status.setTextColor(context.getResources().getColor(R.color.merah));
        }

        holder.status.setText(status);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
    TextView idPembelianPoint, jumlahPoint, metodePembayaran,tanggal,status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idPembelianPoint = (TextView) itemView.findViewById(R.id.idPembelianPoint);
            jumlahPoint=(TextView)  itemView.findViewById(R.id.pembelianPoint_Point);
            metodePembayaran=(TextView)  itemView.findViewById(R.id.pembelianPoint_Metode);
            tanggal=(TextView)  itemView.findViewById(R.id.pembelianPoint_tanggal);
            status=(TextView) itemView.findViewById(R.id.pembelianPoint_Status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        dialog.onClick(getLayoutPosition());
                }
            });

        }
    }
}
