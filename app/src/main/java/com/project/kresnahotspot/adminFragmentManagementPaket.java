package com.project.kresnahotspot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.adapter.ManagementPaketAdapter;
import com.project.kresnahotspot.adapter.VoucherAdapter;
import com.project.kresnahotspot.model.Voucher;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminFragmentManagementPaket#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminFragmentManagementPaket extends Fragment {
    private RecyclerView recyclerView;
    private ManagementPaketAdapter managementPaketAdapter;
    private List<Voucher> list=new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminFragmentManagementPaket() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminFragmentManagementPaket.
     */
    // TODO: Rename and change types and number of parameters
    public static adminFragmentManagementPaket newInstance(String param1, String param2) {
        adminFragmentManagementPaket fragment = new adminFragmentManagementPaket();
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
        View view=inflater.inflate(R.layout.fragment_admin_management_paket, container, false);
        recyclerView= view.findViewById(R.id.itemManagementPaket);
        managementPaketAdapter=new ManagementPaketAdapter(getContext(),list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(managementPaketAdapter);
        
        ambilDataVoucher();

        ImageView buttonTambahVoucher;
        buttonTambahVoucher=(ImageView) view.findViewById(R.id.managementPaket_tambahPaket) ;
        buttonTambahVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),tambahVoucher_admin.class));
            }
        });
        return view;
        
    }

    private void ambilDataVoucher() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Voucher")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    @SuppressLint("NotifyDataSetChanged")
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot :task.getResult()){
                                Voucher voucher=new Voucher(
                                        documentSnapshot.getString("namaVoucher"),
                                        documentSnapshot.getString("kecepatanVoucher"),
                                        documentSnapshot.getString("durasiVoucher"),
                                        documentSnapshot.getString("hargaVoucher"),
                                        documentSnapshot.getString("idConfig"));
                                voucher.setId(documentSnapshot.getId());
                                list.add(voucher);
                            }
                            managementPaketAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getContext(), "gagal mengambil data voucher", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}