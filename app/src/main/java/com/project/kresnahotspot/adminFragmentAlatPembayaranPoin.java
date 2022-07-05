package com.project.kresnahotspot;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.adapter.AlatPembayaranAdapter;
import com.project.kresnahotspot.adapter.ManagementUserAdapter_Admin;
import com.project.kresnahotspot.model.alatPembayaranModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminFragmentAlatPembayaranPoin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminFragmentAlatPembayaranPoin extends Fragment {
    private RecyclerView recyclerView;
    private List<alatPembayaranModel> list=new ArrayList<>();
    private AlatPembayaranAdapter alatPembayaranAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminFragmentAlatPembayaranPoin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminFragmentAlatPembayaranPoin.
     */
    // TODO: Rename and change types and number of parameters
    public static adminFragmentAlatPembayaranPoin newInstance(String param1, String param2) {
        adminFragmentAlatPembayaranPoin fragment = new adminFragmentAlatPembayaranPoin();
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
        View view= inflater.inflate(R.layout.fragment_admin_alat_pembayaran_poin, container, false);
        recyclerView= view.findViewById(R.id.itemPembayaranPoin);
        alatPembayaranAdapter =new AlatPembayaranAdapter(getContext(),list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(alatPembayaranAdapter);

        ImageView tambahPembayaran;
        tambahPembayaran=(ImageView) view.findViewById(R.id.tambahAlatPembayaran);

        tambahPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTambahPembayaran();
            }
        });

        ambilDataAlatPembayaran();
        return  view;
    }

    private void dialogTambahPembayaran() {
        View tampilDialog=LayoutInflater.from(getContext()).inflate(R.layout.dialog_tambah_alat_pembayaran,null);
        EditText mediaPembayaran, nomorPembayaran, atasNama;
        Button ya, tidak;

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(tampilDialog);
        AlertDialog dialog= builder.create();
        dialog.show();

        mediaPembayaran=(EditText) tampilDialog.findViewById(R.id.dialogTambahPembayaran_Media);
        nomorPembayaran=(EditText) tampilDialog.findViewById(R.id.dialogTambahPembayaran_NoPembayaran);
        atasNama=(EditText) tampilDialog.findViewById(R.id.dialogTambahPembayaran_AtasNama);
        ya=(Button) tampilDialog.findViewById(R.id.dialogTambahPembayaran_buttonSimpan);
        tidak=(Button) tampilDialog.findViewById(R.id.dialogTambahPembayaran_buttonBatal);

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String media=mediaPembayaran.getText().toString();
                String nomor=nomorPembayaran.getText().toString();
                String nama=atasNama.getText().toString();

                if(media=="" && nomor=="" &&nama==""){
                    Toast.makeText(getContext(), "Data Alat Pembayaran Belum Lengkap!", Toast.LENGTH_SHORT).show();
                }
                else{
                    simpanAlatPembayaran(media,nomor,nama);
                    dialog.dismiss();
                }
            }
        });
    }

    private void simpanAlatPembayaran(String media, String nomor, String nama) {
        FirebaseFirestore db= FirebaseFirestore.getInstance();

        HashMap<String,Object> dataMediaPembayaran=new HashMap<>();
        dataMediaPembayaran.put("atasNama",nama);
        dataMediaPembayaran.put("mediaPembayaran",media);
        dataMediaPembayaran.put("nomorPembayaran",nomor);

        db.collection("dataAlatPembayaran")
                .add(dataMediaPembayaran)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Alat Pembayaran Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                            ambilDataAlatPembayaran();
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
                           alatPembayaranAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}