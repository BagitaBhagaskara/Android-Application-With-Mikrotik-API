package com.project.kresnahotspot.adapter;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
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
        }
        holder.status.setText(status);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tampilDialogPembayaranPoint(pembelianPoint, holder.metodePembayaran);
            }
        });
    }

    private void tampilDialogPembayaranPoint(PembelianPoint pembelianPoint, TextView itemView) {
        View view=LayoutInflater.from(context).inflate(R.layout.dialog_pembayaran_point,null);
        TextView metodeBayar, totalBayar, hilang1, hilang2, hilang3,hilang4,bayarBank, bayarDana,tampil ;
        ImageView gambar;
        Button upload;
        String metode1=pembelianPoint.getMetodePembayaran();
        String total=pembelianPoint.getJumlahPoint();
        metodeBayar=(TextView) view.findViewById(R.id.dialog_pembayaranMetode);
        totalBayar=(TextView) view.findViewById(R.id.dialog_pembayaranTotalPembayaran);
        gambar=(ImageView) view.findViewById(R.id.dialog_pembayaranImage);
        upload=(Button) view.findViewById(R.id.dialog_pembayaranButtonUpload);
        hilang1=(TextView) view.findViewById(R.id.hilang1);
        hilang2=(TextView) view.findViewById(R.id.hilang2);
        hilang3=(TextView) view.findViewById(R.id.hilang3);
        hilang4=(TextView) view.findViewById(R.id.hilang4);
        bayarBank=(TextView) view.findViewById(R.id.dialog_pembayaranPointBank);
        bayarDana=(TextView) view.findViewById(R.id.dialog_pembayaranDana);
        tampil=(TextView) view.findViewById(R.id.tampil);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);

        metodeBayar.setText(metode1);
        if(metode1.equals("Langsung")){
            tampil.setVisibility(View.VISIBLE);
            gambar.setVisibility(View.GONE);
            upload.setVisibility(View.GONE);
            hilang1.setVisibility(View.GONE);
            hilang2.setVisibility(View.GONE);
            hilang3.setVisibility(View.GONE);
            hilang4.setVisibility(View.GONE);
            bayarBank.setVisibility(View.GONE);
            bayarDana.setVisibility(View.GONE);
        }
        totalBayar.setText("Rp."+total);
        AlertDialog dialog= builder.create();
        dialog.show();

        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            updateFotoBuktiPembayaran(context);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadFoto();
            }
        });
    }


    private void updateFotoBuktiPembayaran(Context context) {
        final CharSequence[] items={"Ambil Foto","Pilih Dari Galeri","Batal"};
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Ubah Foto Profil ?");
        builder.setItems(items,(dialog,item)->{
            if (items[item].equals("Ambil Foto")){
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ((Activity)context).startActivityForResult(i,10);
            }
            else if (items[item].equals("Pilih Dari Galeri")){
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                ((Activity)context).startActivityForResult(Intent.createChooser(i,"Pilih Foto"),20);
            }
            else if (items[item].equals("Batal")){
                dialog.dismiss();
            }
        });

        builder.show();
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

        }
    }
}
