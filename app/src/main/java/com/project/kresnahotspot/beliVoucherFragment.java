package com.project.kresnahotspot;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.adapter.VoucherAdapter;
import com.project.kresnahotspot.model.Voucher;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link beliVoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class beliVoucherFragment extends Fragment {
private RecyclerView recyclerViewVoucher;
private VoucherAdapter voucherAdapter;
private List<Voucher>list=new ArrayList<>();




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public beliVoucherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment beliVoucherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static beliVoucherFragment newInstance(String param1, String param2) {
        beliVoucherFragment fragment = new beliVoucherFragment();
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
        View view= inflater.inflate(R.layout.fragment_beli_voucher, container, false);
        recyclerViewVoucher= view.findViewById(R.id.itemVoucher);
        voucherAdapter=new VoucherAdapter(getContext(),list);
        ButterKnife.bind(this,view);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        recyclerViewVoucher.setLayoutManager(gridLayoutManager);
        recyclerViewVoucher.setAdapter(voucherAdapter);

        ambilDataVoucher();

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
                            voucherAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getContext(), "Gagal mengambil data voucher", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}