package com.project.kresnahotspot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kresnahotspot.model.HotspotMikrotik;

import java.util.List;
import java.util.Map;

import javax.net.SocketFactory;

import examples.Config;
import me.legrange.mikrotik.ApiConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminFragmentHotspot#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminFragmentHotspot extends Fragment {
    ApiConnection con;
    HotspotMikrotik hotspotMikrotik=new HotspotMikrotik();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    TextView itemHotspotActive,itemHotspotUser;
    LinearLayout klikHotspotActive,klikHotspotUser,klikGenerateUser,klikProfileHotspot,warning;
    public boolean cekLoginMikrotik=true;
    SharedPreferences getDataLoginMikrotik;
    private static final String LOG_TAG = "Hotspot Mikrotik";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminFragmentHotspot() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminFragmentHotspot.
     */
    // TODO: Rename and change types and number of parameters
    public static adminFragmentHotspot newInstance(String param1, String param2) {
        adminFragmentHotspot fragment = new adminFragmentHotspot();
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
        View view=inflater.inflate(R.layout.fragment_admin_hotspot, container, false);
        ImageView refresh;
        itemHotspotActive=(TextView) view.findViewById(R.id.hotspotAdmin_hotspotActive);
        itemHotspotUser=(TextView) view.findViewById(R.id.hotspotAdmin_hotspotUser);
        klikHotspotActive=(LinearLayout) view.findViewById(R.id.hotspotAdmin_klikHotspotActive);
        klikHotspotUser=(LinearLayout) view.findViewById(R.id.hotspotAdmin_klikHotspotUser);
        klikGenerateUser=(LinearLayout) view.findViewById(R.id.hotspotAdmin_klikGenerateHotspot);
        klikProfileHotspot=(LinearLayout)view.findViewById(R.id.hotspotAdmin_klikProfileHotspot);
        warning=(LinearLayout) view.findViewById(R.id.hotspotAdmin_warning);
//        refresh= (ImageView) view.findViewById(R.id.hotspotAdmin_refresh);
        getDataLoginMikrotik=getContext().getSharedPreferences("data_login_mikrotik", Context.MODE_PRIVATE);

        klikGenerateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),generateHotspotAdmin.class));
            }
        });
        klikHotspotActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),hotspot_active_admin.class));
            }
        });


        klikHotspotUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),hotspot_user_admin.class));
            }
        });

        klikProfileHotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),profileHotspotAdmin.class));
            }
        });

        loginMikrotik loginMikrotik=new loginMikrotik();
        loginMikrotik.execute();

        ambilDataMikrotik ambilDataMikrotik=new ambilDataMikrotik();
        ambilDataMikrotik.execute();

        setHotspotAktif(itemHotspotActive);
        setHotspotUser(itemHotspotUser);
        if(cekLoginMikrotik==false){
            warning.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void setHotspotUser(TextView itemHotspotUser) {
        DatabaseReference ref=database.getReference("jumlahHotspotUser");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                itemHotspotUser.setText(value+" User");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setHotspotAktif(TextView itemHotspotActive) {
        DatabaseReference ref=database.getReference("jumlahHotspotActive");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                itemHotspotActive.setText(value+" Active");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    class loginMikrotik extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try
            {
                con =ApiConnection.connect(SocketFactory.getDefault(), getDataLoginMikrotik.getString("ipLoginMikrotik",null), getDataLoginMikrotik.getInt("portLoginMikrotikInt",0 ), 2000);
                con.login(getDataLoginMikrotik.getString("usernameLoginMikrotik",null), getDataLoginMikrotik.getString("passwordLoginMikrotik",null));
                Log.d(LOG_TAG, "Login Mikrotik Berhasil");
            }
            catch (Exception e)
            {
                cekLoginMikrotik=false;
                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot login mikrotik");
            }
            return null;
        }
    }
    class ambilDataMikrotik extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                List<Map<String, String>> rs = con.execute("/ip/hotspot/user/print where server=hotspot1");
                    Log.d(LOG_TAG,"Data Resource: "+rs);
                    int banyakData=rs.size();
                    String Sbanyak=Integer.toString(banyakData);
                    hotspotMikrotik.setJumlahHotspotUsers(Sbanyak);
                    Log.d(LOG_TAG,"Banyak user: "+Sbanyak);
                    DatabaseReference user = database.getReference("jumlahHotspotUser");
                    user.setValue(Sbanyak);

                List<Map<String, String>> aktif = con.execute("/ip/hotspot/active/print");
                    Log.d(LOG_TAG,"Data Resource: "+aktif);
                    int banyakDataAktif=aktif.size();
                    String SbanyakAktif=Integer.toString(banyakDataAktif);
                    hotspotMikrotik.setJumlahUserActive(SbanyakAktif);
                    Log.d(LOG_TAG,"Banyak active: "+SbanyakAktif);
                    Log.d(LOG_TAG,"GetBanyak active: "+hotspotMikrotik.getJumlahUserActive());
                    DatabaseReference active = database.getReference("jumlahHotspotActive");
                    active.setValue(SbanyakAktif);
            }
            catch (Exception e)
            {
                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot login mikrotik");
            }
            return null;
        }
    }




}