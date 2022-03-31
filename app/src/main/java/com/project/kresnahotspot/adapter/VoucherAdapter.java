package com.project.kresnahotspot.adapter;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.DocumentKey;
import com.project.kresnahotspot.HomeActivity;
import com.project.kresnahotspot.LoginActivity;
import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.Penjualan;
import com.project.kresnahotspot.model.Voucher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.SocketFactory;

import examples.Config;
import examples.Example;
import examples.ScriptCommand;
import examples.SimpleCommand;
import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.MyViewHolder>{
    private Context context;
    private List<Voucher> list;
    private String username,password;
    public String uptime,profile;
    private static final String LOG_TAG = "VoucherAdapter";


    public VoucherAdapter(Context context, List<Voucher> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_paket_voucher,parent,false);
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



        holder.beliVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahVoucher(voucher);
            }
        });
    }

    private void tambahVoucher(Voucher voucher) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_beli_voucher,null);
        TextView point, nama,kecepatan,durasi,harga;
        Button beli;


        point=(TextView) view.findViewById(R.id.dialog_point);
        nama=(TextView) view.findViewById(R.id.dialog_namaPaket);
        kecepatan=(TextView)  view.findViewById(R.id.dialog_kecepatanPaket);
        durasi=(TextView)  view.findViewById(R.id.dialog_durasiPaket);
        harga=(TextView) view.findViewById(R.id.dialog_hargaPaket);
        beli=(Button) view.findViewById(R.id.dialog_buttonBeli);

        ambilPointUser(point);
        String dialog_nama=voucher.getNama();
        String dialog_kecepatan=voucher.getKecepatan();
        String dialog_durasi=voucher.getDurasi();
        String dialog_harga=voucher.getHarga();

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);

        nama.setText(dialog_nama);
        kecepatan.setText(dialog_kecepatan);
        durasi.setText(dialog_durasi);
        harga.setText(dialog_harga);
        AlertDialog dialog= builder.create();
        dialog.show();


        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(point.getText().toString())<Integer.parseInt(harga.getText().toString())){
                    Toast.makeText(context, "Point tidak cukup, silahkah melakukan pembelian poin", Toast.LENGTH_SHORT).show();
                }else{
                    String random=getRandomString(6);
                    username= random;
                    password=random;
                    ambildataConfig(voucher);
                    uptime=voucher.getConfigUptime();
                    profile=voucher.getConfigProfile();
                    if(uptime!=null&&!uptime.isEmpty()&&profile!=null&&!profile.isEmpty()){
                        simpanPembelianVoucher(voucher,username,password);
                        MyTask mt = new MyTask();
                        mt.execute();
                        dialog.dismiss();

                    }else{
                        Toast.makeText(context, "Masih proses, coba lagi.", Toast.LENGTH_SHORT).show();
                    }


                }
            }

        });

    }

    private void ambildataConfig(Voucher voucher) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        String idVoucher=voucher.getId();
        DocumentReference ambilIdConfig=db.collection("Voucher").document(idVoucher);
        ambilIdConfig
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            String idconfig=documentSnapshot.getString("idConfig");
                            DocumentReference ambilDataConfig=db.collection("configHotspot").document(idconfig);
                            ambilDataConfig
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot1=task.getResult();
                                                voucher.setConfigUptime(documentSnapshot1.getString("uptime"));
                                                voucher.setConfigProfile(documentSnapshot1.getString("namaProfile"));
                                            }else{
                                                Toast.makeText(context, "gagal mengambil data", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(context, "gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }




    class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //tvResult.setText("Begin");

        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                try
                {
                    Log.d(LOG_TAG, "start");
                    ApiConnection con = ApiConnection.connect(SocketFactory.getDefault(), Config.HOST, ApiConnection.DEFAULT_PORT, 2000);
                    con.login(Config.USERNAME, Config.PASSWORD);
                    Log.d(LOG_TAG, "start2");
                    if(con.isConnected())
                    {
                        Log.d(LOG_TAG, "isConnected");
                    }
                    con.execute("/ip/hotspot/user/add server=hotspot1 name="+username+" password="+password+" profile="+profile+" limit-uptime="+uptime);

                    con.close();

                }
                catch (Exception e)
                {
                    Log.d(LOG_TAG, "error"+uptime+"profile="+profile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //tvResult.setText("End");
        }
    }

    private void simpanPembelianVoucher(Voucher voucher, String username, String password) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        String idVoucher=voucher.getId();
//        idVoucherConfig=idVoucher;
        String tanggal=ambilTanggal();
        String usernameHotspot=username;
        String passwordHotspot=password;

        FirebaseAuth auth=FirebaseAuth.getInstance();
        String idUser=auth.getCurrentUser().getUid();





        HashMap<String,Object>penjualanVoucher=new HashMap<>();
        penjualanVoucher.put("Id Voucher",idVoucher);
        penjualanVoucher.put("Tanggal Pembelian",tanggal);
        penjualanVoucher.put("Username Login Hotspot",usernameHotspot);
        penjualanVoucher.put("Password Login Hotspot",passwordHotspot);
        penjualanVoucher.put("Id User",idUser);

        db.collection("penjualanVoucher")
                .add(penjualanVoucher)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Voucher Berhasil Dibeli", Toast.LENGTH_SHORT).show();
                            //DocumentReference ambilConfig =db.collection("penjualanVoucher").document(idVoucher);
                            DocumentReference documentReference=db.collection("dataUser").document(auth.getCurrentUser().getUid());
                            documentReference.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot= task.getResult();
                                                String point=documentSnapshot.getString("coin");
                                                int sisa = Integer.parseInt(point)-Integer.parseInt(voucher.getHarga());
                                                HashMap<String,Object>dataUser=new HashMap<>();
                                                dataUser.put("coin",Integer.toString(sisa));
                                                dataUser.put("emailUser",documentSnapshot.getString("emailUser"));
                                                dataUser.put("isUser",documentSnapshot.getString("isUser"));
                                                dataUser.put("namaUser",documentSnapshot.getString("namaUser"));
                                                dataUser.put("phoneUser",documentSnapshot.getString("phoneUser"));
                                                documentReference.set(dataUser);
                                                ((HomeActivity)context).coin.setText(""+sisa);

                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(context, "Voucher Gagal Dibeli", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private String ambilTanggal() {
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
        String tanggal = simpleDateFormat.format(calendar.getTime());
        return tanggal;
    }

    private void ambilPointUser(TextView view) {
        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference documentReference=db.collection("dataUser").document(auth.getCurrentUser().getUid());
        documentReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot= task.getResult();
                            view.setText(documentSnapshot.getString("coin"));
                        }else{
                            Toast.makeText(context, "Gagal mengambil data point", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nama, kecepatan, durasi, harga;
        Button beliVoucher;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView) itemView.findViewById(R.id.namaPaket);
            kecepatan=(TextView)  itemView.findViewById(R.id.kecepatanPaket);
            durasi=(TextView)  itemView.findViewById(R.id.durasiPaket);
            harga=(TextView) itemView.findViewById(R.id.hargaPaket);
            beliVoucher=(Button) itemView.findViewById(R.id.buttonBeliVoucher);


        }
    }
}
