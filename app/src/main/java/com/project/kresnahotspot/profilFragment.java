package com.project.kresnahotspot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class profilFragment extends Fragment {
    ImageView fotoUser;
    TextView nama,point,email,handphone;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profilFragment newInstance(String param1, String param2) {
        profilFragment fragment = new profilFragment();
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
        View view=inflater.inflate(R.layout.fragment_profil, container, false);
        fotoUser=(ImageView) view.findViewById(R.id.profil_fotoUser);
        nama=(TextView) view.findViewById(R.id.profil_namaUser);
        handphone=(TextView) view.findViewById(R.id.profil_handphoneUser);
        email=(TextView) view.findViewById(R.id.profil_emailUser);
        point=(TextView) view.findViewById(R.id.profil_pointUser) ;
        FirebaseAuth auth= FirebaseAuth.getInstance();


        ambilDataProfilUser(auth.getCurrentUser().getUid());


        return view;


    }

    private void ambilDataProfilUser(String uid) {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        DocumentReference documentReference=db.collection("dataUser").document(uid);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            String setNama=documentSnapshot.getString("namaUser");
                            nama.setText(setNama);
                            String setPoint=documentSnapshot.getString("coin");
                            point.setText(setPoint);
                            String setEmail=documentSnapshot.getString("emailUser");
                            email.setText(setEmail);
                            String setHandphone=documentSnapshot.getString("phoneUser");
                            handphone.setText(setHandphone);
                        }else{
                            Toast.makeText(getContext(), "Gagal Mengambil Data User", Toast.LENGTH_SHORT).show();
                        }
                       
                    }
                });

    }

}