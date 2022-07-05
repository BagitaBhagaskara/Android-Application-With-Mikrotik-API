package com.project.kresnahotspot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.HotspotUserMikrotik;
import com.project.kresnahotspot.model.PembelianPoint;

import java.util.List;

public class UserHotspotAdapter extends RecyclerView.Adapter<UserHotspotAdapter.MyViewHolder> {
    Context context;
    List<HotspotUserMikrotik> list;

    public UserHotspotAdapter(Context context,List<HotspotUserMikrotik>list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserHotspotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_hotspot_user_admin,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserHotspotAdapter.MyViewHolder holder, int position) {
        HotspotUserMikrotik hotspotUserMikrotik=list.get(position);
        String server=hotspotUserMikrotik.getServer();
        String name=hotspotUserMikrotik.getName();
        String profile=hotspotUserMikrotik.getProfile();
        String uptime=hotspotUserMikrotik.getUptime();
        String password=hotspotUserMikrotik.getPassword();

        holder.server.setText(server);
        holder.name.setText(name);
        holder.profile.setText(profile);
        holder.uptime.setText(uptime);
        holder.password.setText(password);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView server,name,profile,uptime,password;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            server=(TextView) itemView.findViewById(R.id.rowHotspotUser_server);
            name=(TextView) itemView.findViewById(R.id.rowHotspotUser_name);
            profile=(TextView) itemView.findViewById(R.id.rowHotspotUser_profile);
            uptime=(TextView) itemView.findViewById(R.id.rowHotspotUser_uptime);
            password=(TextView) itemView.findViewById(R.id.rowHotspotUser_password);
        }
    }
}
