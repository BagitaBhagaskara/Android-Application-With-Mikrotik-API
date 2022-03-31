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
import com.project.kresnahotspot.model.Penjualan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link statusVoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class statusVoucherFragment extends Fragment {
    private RecyclerView penjualan;
    private List<Penjualan>list=new ArrayList<>();
    private PenjualanAdapter penjualanAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public statusVoucherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment statusVoucherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static statusVoucherFragment newInstance(String param1, String param2) {
        statusVoucherFragment fragment = new statusVoucherFragment();
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
        View view=inflater.inflate(R.layout.fragment_status_voucher, container, false);
        penjualan=view.findViewById(R.id.itemPenjualanVoucher);
        penjualanAdapter=new PenjualanAdapter(getContext(),list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        penjualan.setLayoutManager(layoutManager);
        penjualan.setAdapter(penjualanAdapter);

        ambilDataPenjualan();

        return view;

    }

    private void ambilDataPenjualan() {
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        db.collection("penjualanVoucher")
                .whereEqualTo("Id User",auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                Penjualan penjualan = new Penjualan(queryDocumentSnapshot.getString("Id Voucher"),
                                        queryDocumentSnapshot.getString("Username Login Hotspot"),
                                        queryDocumentSnapshot.getString("Password Login Hotspot"),
                                        queryDocumentSnapshot.getString("Tanggal Pembelian"),
                                        queryDocumentSnapshot.getId());
                                list.add(penjualan);
                            }
                            penjualanAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}