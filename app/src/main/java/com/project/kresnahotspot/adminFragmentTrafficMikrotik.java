package com.project.kresnahotspot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.project.kresnahotspot.model.ReadResourceMikrotik;
import com.project.kresnahotspot.model.Voucher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.SocketFactory;

import examples.Config;
import examples.Example;
import examples.SimpleCommand;
import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminFragmentTrafficMikrotik#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminFragmentTrafficMikrotik extends Fragment {
    private List<ReadResourceMikrotik> list=new ArrayList<>();
    private static final String LOG_TAG = "Resource Mikrotik";
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    private Handler timerHandler = new Handler();
    private boolean shouldRun = true;
    public static final int REQUEST_CODE = 1;
    SharedPreferences getDataLoginMikrotik;
    int portMikrotik;
    ApiConnection con;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminFragmentTrafficMikrotik() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminFragmentTrafficMikrotik.
     */
    // TODO: Rename and change types and number of parameters
    public static adminFragmentTrafficMikrotik newInstance(String param1, String param2) {
        adminFragmentTrafficMikrotik fragment = new adminFragmentTrafficMikrotik();
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
        View view=inflater.inflate(R.layout.fragment_admin_traffic_mikrotik, container, false);
        ImageView reboot;
        TextView cpu,uptime,memory,harddisk;
        reboot=(ImageView) view.findViewById(R.id.resource_reboot);
        cpu=(TextView) view.findViewById(R.id.resource_cpuLoad);
        uptime=(TextView) view.findViewById(R.id.resource_uptime);
        memory=(TextView) view.findViewById(R.id.resource_memory);
        harddisk=(TextView) view.findViewById(R.id.resource_harddisk);

        dataCpu(cpu);
        dataUptime(uptime);
        dataMemory(memory);
        dataHardiks(harddisk);

        getDataLoginMikrotik= getActivity().getSharedPreferences("data_login_mikrotik", Context.MODE_PRIVATE);
        Log.d(LOG_TAG, "data sharedPreference: "+getDataLoginMikrotik.getString("ipLoginMikrotik",null));

        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialogKonfirmasi();
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
//        portMikrotik =Integer.parseInt(getDataLoginMikrotik.getString("portLoginMikrotik",null));
        loginMikrotik login =new loginMikrotik();
        login.execute();
        timerHandler.postDelayed(timerRunnable, 1000);
    }
    @Override
    public void onPause() {
        super.onPause();
        /* ... */
        shouldRun = false;
        timerHandler.removeCallbacksAndMessages(timerRunnable);
    }

    private Runnable timerRunnable = new Runnable(){
        @Override
        public void run(){
            if(shouldRun){
                readDataResource resaurceMikrotik=new readDataResource();
                resaurceMikrotik.execute();
                timerHandler.postDelayed(this,1000);
            }
        }
    };

    private void dataHardiks(TextView harddisk) {
        DatabaseReference ref=database.getReference("hardisk");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                int ubahInt=Integer.parseInt(value);
                int jumlah=(ubahInt/1024)/1000;
                String hasil=Integer.toString(jumlah);
                harddisk.setText(hasil+" Mb");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void  dataMemory(TextView memory){
        DatabaseReference ref=database.getReference("memory");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                int ubahInt=Integer.parseInt(value);
                int jumlah=(ubahInt/1024)/1000;
                String hasil=Integer.toString(jumlah);
                memory.setText(hasil+" Mb");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void dataUptime(TextView uptime) {
        DatabaseReference ref=database.getReference("uptime");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                uptime.setText(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dataCpu(TextView cpu) {
        DatabaseReference ref=database.getReference("cpuLoad");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String value=snapshot.getValue(String.class);
               cpu.setText(value+" %");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void dialogKonfirmasi() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_konfirmasi_logout,null);
        Button ya, tidak;
        ya=(Button) view.findViewById(R.id.dialogLogout_buttonIya);
        tidak=(Button) view.findViewById(R.id.dialogLogout_buttonTidak);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(view);
        AlertDialog dialog= builder.create();
        dialog.show();

        ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reboot mt= new Reboot();
                mt.execute();
            }
        });

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    class Reboot extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... params) {
            try
                {
                    con.execute("/system/reboot");
                    con.close();
                }
            catch (Exception e)
                {
                    Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot");
                }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    class loginMikrotik extends AsyncTask<Void,Void,Void>{

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
                Log.d(LOG_TAG, "Exception Login:  "+e);
                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot login mikrotik");

            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //tvResult.setText("End");
            Log.d(LOG_TAG, "data config:   "+getDataLoginMikrotik.getString("ipLoginMikrotik",null)+" "+getDataLoginMikrotik.getString("usernameLoginMikrotik",null)+" "+getDataLoginMikrotik.getString("passwordLoginMikrotik",null));
        }
    }
    class readDataResource extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                    List<Map<String, String>> rs = con.execute("/system/resource/print");
                    for (Map<String, String> map : rs) {
                        Log.d(LOG_TAG,"Data Resource: "+map.get("uptime"));
                        DatabaseReference uptime = database.getReference("uptime");
                        DatabaseReference cpuUpload = database.getReference("cpuLoad");
                        DatabaseReference memory = database.getReference("memory");
                        DatabaseReference hdd = database.getReference("hardisk");
                        uptime.setValue(map.get("uptime"));
                        cpuUpload.setValue(map.get("cpu-load"));
                        memory.setValue(map.get("free-memory"));
                        hdd.setValue(map.get("free-hdd-space"));
                    }
            }
            catch (Exception e)
            {
                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}