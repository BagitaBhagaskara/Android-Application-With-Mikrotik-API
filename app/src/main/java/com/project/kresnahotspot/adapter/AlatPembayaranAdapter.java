package com.project.kresnahotspot.adapter;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.PembelianPoint;
import com.project.kresnahotspot.model.alatPembayaranModel;

import java.util.List;

public class AlatPembayaranAdapter extends RecyclerView.Adapter<AlatPembayaranAdapter.MyViewHolder>{
    Context context;
    List<alatPembayaranModel> list;
    public AlatPembayaranAdapter(Context context, List<alatPembayaranModel>list){
        this.context = context;
        this.list = list;

    }
    @NonNull
    @Override
    public AlatPembayaranAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_alat_pembayaran,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlatPembayaranAdapter.MyViewHolder holder, int position) {
    alatPembayaranModel alatPembayaranModel=list.get(position);
    String mediaPembayaran=alatPembayaranModel.getMediaPembayaran();
    String atasNama=alatPembayaranModel.getAtasNama();
    String nomorPembayaran=alatPembayaranModel.getNomorPembayaran();
    String idPembayaran=alatPembayaranModel.getIdAlatPembayaran();

    holder.media.setText(mediaPembayaran);
    holder.atasNama.setText(atasNama);
    holder.nomor.setText(nomorPembayaran);

    holder.delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogKonfirmasiHapusAlat(idPembayaran);
        }
    });

    }

    private void dialogKonfirmasiHapusAlat(String idPembayaran) {
        View tampilDialog=LayoutInflater.from(context).inflate(R.layout.dialog_delete_alat_pembayaran,null);
        Button ya,tidak;
        ya=(Button) tampilDialog.findViewById(R.id.dialogDeleteAlatPembayaran_buttonIya);
        tidak=(Button) tampilDialog.findViewById(R.id.dialogDeleteAlatPembayaran_buttonTidak);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(tampilDialog);
        AlertDialog dialog= builder.create();
        dialog.show();

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusAlatPembayaran(idPembayaran);
                dialog.dismiss();

            }
        });

    }

    private void hapusAlatPembayaran(String idPembayaran) {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("dataAlatPembayaran").document(idPembayaran)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Media pembayaran berhasil dihapus", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void ambilDataAlatPembayaran() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("dataAlatPembayaran")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                alatPembayaranModel alat=new alatPembayaranModel(
                                        queryDocumentSnapshot.getString("mediaPembayaran"),
                                        queryDocumentSnapshot.getString("nomorPembayaran"),
                                        queryDocumentSnapshot.getString("atasNama"),
                                        queryDocumentSnapshot.getId()
                                );
                                list.add(alat);
                            }
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView media, atasNama, nomor;
        Button tambah, delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            media=(TextView) itemView.findViewById(R.id.row_alatPembayaran_mediaPembayaran);
            atasNama=(TextView) itemView.findViewById(R.id.row_alatPembayaran_atasNama);
            nomor=(TextView) itemView.findViewById(R.id.row_alatPembayaran_nomorPembayaran);
            delete=(Button) itemView.findViewById(R.id.row_alatPembayaran_buttonDelete);
        }
    }
}
