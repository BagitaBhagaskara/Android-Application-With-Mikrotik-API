package com.project.kresnahotspot;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.adapter.PenjualanAdapter;
import com.project.kresnahotspot.adapter.PermintaanPembelianPointAdapter_Admin;
import com.project.kresnahotspot.model.PembelianPoint;
import com.project.kresnahotspot.model.Penjualan;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminFragmentPointUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminFragmentPointUser extends Fragment {

    private RecyclerView permintaanPoint;
    private List<PembelianPoint> list=new ArrayList<>();
    private PermintaanPembelianPointAdapter_Admin permintaanPembelianAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminFragmentPointUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminFragmentPointUser.
     */
    // TODO: Rename and change types and number of parameters
    public static adminFragmentPointUser newInstance(String param1, String param2) {
        adminFragmentPointUser fragment = new adminFragmentPointUser();
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
        View view=inflater.inflate(R.layout.fragment_admin_point_user, container, false);
        permintaanPoint=view.findViewById(R.id.itemPermintaanPointUser);
        permintaanPembelianAdapter=new PermintaanPembelianPointAdapter_Admin(getContext(),list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        permintaanPoint.setLayoutManager(layoutManager);
        permintaanPoint.setAdapter(permintaanPembelianAdapter);

        ambilDataPembelianPoint();

        return view;

    }

    private void ambilDataPembelianPoint() {
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        db.collection("pembelianPoint")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                PembelianPoint pembelianPoint=new PembelianPoint(queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("jumlahPembelianPoint"),
                                        queryDocumentSnapshot.getString("metodePembayaran"),
                                        queryDocumentSnapshot.getString("tanggal"),
                                        queryDocumentSnapshot.getString("status"));
                                pembelianPoint.setIdUser(queryDocumentSnapshot.getString("idUser"));
                                list.add(pembelianPoint);
                            }
                            permintaanPembelianAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}