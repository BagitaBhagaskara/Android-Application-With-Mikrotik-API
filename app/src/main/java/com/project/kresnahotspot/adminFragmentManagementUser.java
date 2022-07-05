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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.adapter.ManagementUserAdapter_Admin;
import com.project.kresnahotspot.adapter.statusPembelianPointUserAdapter;
import com.project.kresnahotspot.model.ManagementUser;
import com.project.kresnahotspot.model.PembelianPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminFragmentManagementUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminFragmentManagementUser extends Fragment {
    private RecyclerView recyclerView;
    private List<ManagementUser> list=new ArrayList<>();
    private ManagementUserAdapter_Admin managementUserAdapter_admin;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminFragmentManagementUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminFragmentManagementUser.
     */
    // TODO: Rename and change types and number of parameters
    public static adminFragmentManagementUser newInstance(String param1, String param2) {
        adminFragmentManagementUser fragment = new adminFragmentManagementUser();
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
        View view=inflater.inflate(R.layout.fragment_admin_management_user, container, false);
        recyclerView= view.findViewById(R.id.itemManagementUser);
        managementUserAdapter_admin =new ManagementUserAdapter_Admin(getContext(),list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(managementUserAdapter_admin);

        ambilDatauser();

        return view;
    }

    private void ambilDatauser() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("dataUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                ManagementUser managementUser = new ManagementUser(
                                        queryDocumentSnapshot.getString("coin"),
                                        queryDocumentSnapshot.getString("emailUser"),
                                        queryDocumentSnapshot.getString("namaUser"),
                                        queryDocumentSnapshot.getString("phoneUser"),
                                        queryDocumentSnapshot.getId()
                                );
                                list.add(managementUser);
                            }
                            managementUserAdapter_admin.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
