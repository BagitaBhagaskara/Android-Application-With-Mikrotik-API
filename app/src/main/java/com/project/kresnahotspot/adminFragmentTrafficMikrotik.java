package com.project.kresnahotspot;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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



        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialogKonfirmasi();
            }
        });
        return view;
    }

    private void dataCpu(TextView cpu) {

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
                    ApiConnection con = ApiConnection.connect(SocketFactory.getDefault(), Config.HOST, ApiConnection.DEFAULT_PORT, 2000);
                    con.login(Config.USERNAME, Config.PASSWORD);
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

    class Read extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                ApiConnection con = ApiConnection.connect(SocketFactory.getDefault(), Config.HOST, ApiConnection.DEFAULT_PORT, 2000);
                con.login(Config.USERNAME, Config.PASSWORD);
                list<Map<String, String>> rs = con.execute("/interface/print");
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
}