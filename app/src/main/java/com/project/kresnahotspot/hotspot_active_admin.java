package com.project.kresnahotspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.project.kresnahotspot.adapter.ActiveHotspotAdapter;
import com.project.kresnahotspot.adapter.UserHotspotAdapter;
import com.project.kresnahotspot.model.HotspotActiveMikrotik;
import com.project.kresnahotspot.model.HotspotUserMikrotik;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.SocketFactory;

import examples.Config;
import me.legrange.mikrotik.ApiConnection;

public class hotspot_active_admin extends AppCompatActivity {
    ApiConnection con;
    public static boolean cekLoginMikrotik=true;
    private static final String LOG_TAG = "HotspotActive";
    private List<HotspotActiveMikrotik>list=new ArrayList<>();
    private RecyclerView activeHotspot;
    private Handler timerHandler = new Handler();
    private boolean shouldRun = true;
    private ActiveHotspotAdapter activeHotspotAdapter;
    SharedPreferences getDataLoginMikrotik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspot_active_admin);
        LinearLayout warning;
        ImageView refresh,kembali;

        warning=(LinearLayout) findViewById(R.id.hotspot_active_admin_warning);
        refresh=findViewById(R.id.hotspot_user_active_refresh);
        kembali=findViewById(R.id.hotspot_active_admin_kembali);

        getDataLoginMikrotik= hotspot_active_admin.this.getSharedPreferences("data_login_mikrotik", Context.MODE_PRIVATE);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cekloginMikrotik(warning);

        activeHotspot=findViewById(R.id.itemHotspotActive);
        activeHotspotAdapter=new ActiveHotspotAdapter(hotspot_active_admin.this,list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(hotspot_active_admin.this,LinearLayoutManager.VERTICAL,false);

        activeHotspot.setLayoutManager(layoutManager);
        activeHotspot.setAdapter(activeHotspotAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loginMikrotik loginMikrotik=new loginMikrotik();
        loginMikrotik.execute();
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
                ambilDataUserActiveMikrotik ambilDataUserActiveMikrotik=new ambilDataUserActiveMikrotik();
                ambilDataUserActiveMikrotik.execute();
                timerHandler.postDelayed(this,1000);
            }
        }
    };

    private void cekloginMikrotik(LinearLayout warning) {
        Log.d(LOG_TAG, "status cek login: "+cekLoginMikrotik);
        if(cekLoginMikrotik==false){
            warning.setVisibility(View.VISIBLE);
        }else if(cekLoginMikrotik==true){
            warning.setVisibility(View.GONE);
        }
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
                Log.d(LOG_TAG, "login mikrotik gagal-hotspotactive");
            }
            return null;
        }
    }
    class ambilDataUserActiveMikrotik extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.clear();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                List<Map<String, String>> rs = con.execute("/ip/hotspot/active/print");
                for (Map<String, String> map : rs) {
                    Log.d(LOG_TAG, "Data Active Hotspot map "+map);
                    Log.d(LOG_TAG, "Data Active Hotspot user "+map.get("user"));
                    Log.d(LOG_TAG, "Data Active Hotspot server "+map.get("server"));
                    Log.d(LOG_TAG, "Data Active Hotspot uptime "+map.get("uptime"));
                    Log.d(LOG_TAG, "Data Active Hotspot idle time "+map.get("idle-time"));
                    Log.d(LOG_TAG, "Data Active Hotspot session time "+map.get("session-time-left"));
                    Log.d(LOG_TAG, "Data Active Hotspot mac address "+map.get("mac-address"));
                    HotspotActiveMikrotik hotspotActiveMikrotik=new HotspotActiveMikrotik(
                            map.get("server"),
                            map.get("user"),
                            map.get("uptime"),
                            map.get("idle-time"),
                            map.get("session-time-left"),
                            map.get("mac-address"));
                    list.add(hotspotActiveMikrotik);
                }
            }
            catch (Exception e)
            {
//                cekLoginMikrotik=false;
                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot");
            }
            return null;
        }
        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //tvResult.setText("End");
            activeHotspotAdapter.notifyDataSetChanged();
        }
    }
}