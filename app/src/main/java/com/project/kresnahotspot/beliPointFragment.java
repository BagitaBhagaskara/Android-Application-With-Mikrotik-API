package com.project.kresnahotspot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link beliPointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class beliPointFragment extends Fragment {
    EditText masukanJumlahPoint;
    Button beli;
    TextView point;
    ImageView keranjang, history;
    RadioButton transfer,langsung,radioButton;
    RadioGroup radioGroup;
    String metode;
    private static final String LOG_TAG = "Beli poin: ";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public beliPointFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment beliPointFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static beliPointFragment newInstance(String param1, String param2) {
        beliPointFragment fragment = new beliPointFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_beli_point, container, false);
        masukanJumlahPoint=(EditText) view.findViewById(R.id.MasukanJumlahPoint);
        point=(TextView)  view.findViewById(R.id.banyakPoint);
        beli=(Button)  view.findViewById(R.id.btnBeliPoint);
        keranjang=(ImageView) view.findViewById(R.id.keranjang);
        transfer=(RadioButton) view.findViewById(R.id.radioButtonTransfer) ;
        langsung=(RadioButton)  view.findViewById(R.id.radioButtonLangsung);
        history=(ImageView) view.findViewById(R.id.beliPointuser_history) ;

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),statusPembelianPointUser.class));
            }
        });

        transfer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                transfer.setChecked(true);
                langsung.setChecked(false);
                metode=transfer.getText().toString().trim();
            }
        });

        langsung.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                transfer.setChecked(false);
                langsung.setChecked(true);
                metode=langsung.getText().toString().trim();
            }
        });

        tampilkanPoint();


        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!masukanJumlahPoint.getText().toString().trim().equals("")){
                    View itemView=LayoutInflater.from(getContext()).inflate(R.layout.dialog_pembelian_point,null);
                    TextView jumlahPoint,totalPembayaran,metodePembayaran;
                    Button buttonKonfirmasi;


                    jumlahPoint=(TextView) itemView.findViewById(R.id.dialog_jumlahPoint);
                    totalPembayaran=(TextView) itemView.findViewById(R.id.dialog_totalPembayaran);
                    metodePembayaran=(TextView) itemView.findViewById(R.id.dialog_metodeDetailPembayaran);
                    buttonKonfirmasi=(Button) itemView.findViewById(R.id.dialog_buttonKonfirmasi);
                    radioGroup=(RadioGroup) itemView.findViewById(R.id.radioButtonGrup);

                    FirebaseAuth auth=FirebaseAuth.getInstance();
                    String jumlah=masukanJumlahPoint.getText().toString().trim();
                    String total=masukanJumlahPoint.getText().toString().trim();

                    String tanggal=ambilTanggal();
                    String idUser=auth.getCurrentUser().getUid();

                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setView(itemView);

                    jumlahPoint.setText(jumlah+"\tpoint");
                    totalPembayaran.setText("Rp."+total);
                    metodePembayaran.setText(metode);


                    AlertDialog dialog=builder.create();
                    dialog.show();

                    buttonKonfirmasi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String metodeSet=metodePembayaran.getText().toString();
                                simpanPembelianPoint(idUser, jumlah,total,metodeSet,tanggal,dialog);
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Jumlah Pembelian Point Belum Terisi", Toast.LENGTH_SHORT).show();
                }

            }
        });

        keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),TransaksiPembelianPoint.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });


        return view;
    }


    private void tampilkanPoint() {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        DocumentReference documentReference=db.collection("dataUser").document(auth.getCurrentUser().getUid());
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot= task.getResult();
                            String dataPoint=documentSnapshot.getString("coin");
                            point.setText(dataPoint+"\tpoint");
                        }else{
                            Toast.makeText(getContext(), "Gagal Menampilkan Point", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void simpanPembelianPoint(String idUser, String jumlah, String total, String metodeSet, String tanggal, AlertDialog dialog) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d(LOG_TAG,"metode set: "+metodeSet);
        if(metodeSet.equals("Transfer")){
            HashMap<String,Object>pembelianPoint=new HashMap<>();
            pembelianPoint.put("idUser",idUser);
            pembelianPoint.put("jumlahPembelianPoint",jumlah);
            pembelianPoint.put("totalPembayaran",total);
            pembelianPoint.put("metodePembayaran",metodeSet);
            pembelianPoint.put("status","Menunggu Pembayaran");
            pembelianPoint.put("tanggal",tanggal);
            pembelianPoint.put("UrlBuktiPembelian",null);
            pembelianPoint.put("alasanDitolak",null);
            db.collection("pembelianPoint")
                    .add(pembelianPoint)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getContext(), "Konfirmasi pembelian berhasil, silahkan melakukan pembayaran!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(getContext(), "Konfirmasi Pembelian Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else if (metodeSet.equals("Langsung")){
            HashMap<String,Object>pembelianPoint=new HashMap<>();
            pembelianPoint.put("idUser",idUser);
            pembelianPoint.put("jumlahPembelianPoint",jumlah);
            pembelianPoint.put("totalPembayaran",total);
            pembelianPoint.put("metodePembayaran",metodeSet);
            pembelianPoint.put("status","Diproses");
            pembelianPoint.put("tanggal",tanggal);
            pembelianPoint.put("UrlBuktiPembelian",null);
            pembelianPoint.put("alasanDitolak",null);
            db.collection("pembelianPoint")
                    .add(pembelianPoint)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getContext(), "Konfirmasi pembelian berhasil, silahkan melakukan pembayaran!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(getContext(), "Konfirmasi Pembelian Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Log.d(LOG_TAG,"galgall beli "+metodeSet);
        }

    }

    private String ambilTanggal() {
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
        String tanggal = simpleDateFormat.format(calendar.getTime());
        return tanggal;
    }
}