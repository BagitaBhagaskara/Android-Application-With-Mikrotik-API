package com.project.kresnahotspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.project.kresnahotspot.adapter.PembelianPointAdapter;
import com.project.kresnahotspot.adapter.UserHotspotAdapter;
import com.project.kresnahotspot.model.HotspotUserMikrotik;
import com.project.kresnahotspot.model.PembelianPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.SocketFactory;

import examples.Config;
import me.legrange.mikrotik.ApiConnection;

public class hotspot_user_admin extends AppCompatActivity {
    ApiConnection con;
    public static boolean cekLoginMikrotik=true;
    private static final String LOG_TAG = "UserHotspot";
    private List<HotspotUserMikrotik>list=new ArrayList<>();
    private RecyclerView userHotspot;
    private UserHotspotAdapter userHotspotAdapter;
    SharedPreferences getDataLoginMikrotik;
//    Config config =new Config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspot_user_admin);
        LinearLayout warning;
        ImageView refresh,kembali,history;
        warning=findViewById(R.id.hotspot_user_admin_warning);
        refresh=findViewById(R.id.hotspot_user_admin_refresh);
        kembali=findViewById(R.id.hotspot_user_admin_kembali);

        getDataLoginMikrotik= hotspot_user_admin.this.getSharedPreferences("data_login_mikrotik", Context.MODE_PRIVATE);

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
                finish();
                onBackPressed();
            }
        });

        loginMikrotik loginMikrotik =new loginMikrotik();
        loginMikrotik.execute();

        cekloginMikrotik(warning);

        ambilDataUserMikrotik ambilDataUserMikrotik=new ambilDataUserMikrotik();
        ambilDataUserMikrotik.execute();


        userHotspot=findViewById(R.id.itemHotspotUser);
        userHotspotAdapter=new UserHotspotAdapter(hotspot_user_admin.this,list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(hotspot_user_admin.this,LinearLayoutManager.VERTICAL,false);

        userHotspot.setLayoutManager(layoutManager);
        userHotspot.setAdapter(userHotspotAdapter);
    }



    private void cekloginMikrotik(LinearLayout warning) {
        Log.d(LOG_TAG, "status cek login: "+cekLoginMikrotik);
        if(cekLoginMikrotik==false){
            warning.setVisibility(View.VISIBLE);
        }else if(cekLoginMikrotik==true){
            warning.setVisibility(View.GONE);
        }
    }

    class loginMikrotik extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                con =ApiConnection.connect(SocketFactory.getDefault(), getDataLoginMikrotik.getString("ipLoginMikrotik",null), getDataLoginMikrotik.getInt("portLoginMikrotikInt",0), 2000);
                con.login(getDataLoginMikrotik.getString("usernameLoginMikrotik",null), getDataLoginMikrotik.getString("passwordLoginMikrotik",null));
                Log.d(LOG_TAG, "Login Mikrotik Berhasil");
            }
            catch (Exception e)
            {
                cekLoginMikrotik=false;
                Log.d(LOG_TAG, "exception"+e);

                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot login mikrotik");

            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //tvResult.setText("End");
        }
    }
    class ambilDataUserMikrotik extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.clear();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                List<Map<String, String>> rs = con.execute("/ip/hotspot/user/print where server=hotspot1");
                for (Map<String, String> map : rs) {
//                    Log.d(LOG_TAG, "Data User Hotspot map: "+map);
//                    Log.d(LOG_TAG, "Data User Hotspot password: "+map.get("password"));
//                    Log.d(LOG_TAG, "Data User Hotspot Name"+map.get("profile"));
//                    Log.d(LOG_TAG, "Data User Hotspot Name"+map.get("uptime"));
                    HotspotUserMikrotik hotspotUserMikrotik=new HotspotUserMikrotik(
                            map.get("server"),
                            map.get("name"),
                            map.get("profile"),
                            map.get("uptime"),
                            map.get("password"));
                    list.add(hotspotUserMikrotik);
                }

            }
            catch (Exception e)
            {
                Log.d(LOG_TAG, "Anda belum terhubung pada jaringan wifi Kresna-Hotspot");
            }
            return null;
        }
        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //tvResult.setText("End");
            userHotspotAdapter.notifyDataSetChanged();
        }
    }

}