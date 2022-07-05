package com.project.kresnahotspot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.ManagementUser;


import java.util.HashMap;
import java.util.List;

public class ManagementUserAdapter_Admin extends RecyclerView.Adapter<ManagementUserAdapter_Admin.MyViewHolder>{
    Context context;
    List<ManagementUser> list;
    FirebaseFirestore db=FirebaseFirestore.getInstance();


    public ManagementUserAdapter_Admin(Context context, List<ManagementUser>list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ManagementUserAdapter_Admin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_management_user,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagementUserAdapter_Admin.MyViewHolder holder, int position) {
        ManagementUser managementUser=list.get(position);
        String namaUser=managementUser.getNamaUser();
        String phoneUser=managementUser.getPhone();
        String emailUser=managementUser.getEmail();
        String poinUser=managementUser.getPoin();
        String idUser=managementUser.getId();

        holder.nama.setText(namaUser);
        holder.phone.setText(phoneUser);
        holder.email.setText(emailUser);
        holder.poin.setText(poinUser);

        holder.tambahPoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTambahPoinUser(poinUser,idUser);

            }
        });

        holder.hapusUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHapusAkunUser(idUser);
            }
        });

    }

    private void dialogHapusAkunUser(String idUser) {
        View hapusAkun=LayoutInflater.from(context).inflate(R.layout.dialog__konfirmasi_delete_akun_user,null);
        Button iya, tidak;
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(hapusAkun);
        AlertDialog dialog= builder.create();
        dialog.show();
        iya=(Button) hapusAkun.findViewById(R.id.dialogDeleteAkunUser_buttonIya);
        tidak=(Button) hapusAkun.findViewById(R.id.dialogDeleteAkunUser_buttonTidak);

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        iya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                hapusAkunUser(idUser);
            }
        });



    }

//    private void hapusAkunUser(String idUser) {
//        FirebaseAuth auth=FirebaseAuth.getInstance();
//
//       auth.de
//
//        db.collection("dataUser").document(idUser)
//                .delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(context, "Akun user berhasil dihapus", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//
//    }

    private void dialogTambahPoinUser(String poinUser, String idUser) {
        View tampilDialog=LayoutInflater.from(context).inflate(R.layout.dialog_managemen_user_tambah_poin,null);
        TextView poinSaatIni;
        Button tambah, batal;
        EditText masukanPoin;
        masukanPoin=(EditText) tampilDialog.findViewById(R.id.dialogManagementUserTambah_masukanPoin);
        tambah=(Button) tampilDialog.findViewById(R.id.dialogManagementUserTambah_buttonYa);
        batal=(Button) tampilDialog.findViewById(R.id.dialogManagementUserTambah_buttonBatal);
        poinSaatIni=(TextView) tampilDialog.findViewById(R.id.dialogManagementUserTambah_poinSaatIni);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(tampilDialog);
        AlertDialog dialog= builder.create();
        dialog.show();

        poinSaatIni.setText(poinUser);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tambahPoin=masukanPoin.getText().toString();
                if (tambahPoin==""){
                    Toast.makeText(context, "Masukan jumlah poin", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Poin berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    tambahPoinUser(tambahPoin,idUser);
                    dialog.dismiss();
                }
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void tambahPoinUser(String tambahPoin, String idUser) {

        DocumentReference documentReference=db.collection("dataUser").document(idUser);
        documentReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot= task.getResult();
                            String point=documentSnapshot.getString("coin");
                            int jumlah = Integer.parseInt(point)+Integer.parseInt(tambahPoin);
                            HashMap<String,Object> dataUser=new HashMap<>();
                            dataUser.put("coin",Integer.toString(jumlah));
                            dataUser.put("emailUser",documentSnapshot.getString("emailUser"));
                            dataUser.put("statusLogin",documentSnapshot.getString("statusLogin"));
                            dataUser.put("namaUser",documentSnapshot.getString("namaUser"));
                            dataUser.put("phoneUser",documentSnapshot.getString("phoneUser"));
                            documentReference.set(dataUser);
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nama,poin,email,phone;
        Button tambahPoin, hapusUser;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView) itemView.findViewById(R.id.row_managementUser_nama);
            poin=(TextView) itemView.findViewById(R.id.row_managementUser_poin);
            email=(TextView) itemView.findViewById(R.id.row_managementUser_email);
            phone=(TextView) itemView.findViewById(R.id.row_managementUser_phone);
            tambahPoin=(Button) itemView.findViewById(R.id.row_managementUser_buttonTambah);
            hapusUser=(Button) itemView.findViewById(R.id.row_managementUser_buttonDelete);
        }
    }
}
