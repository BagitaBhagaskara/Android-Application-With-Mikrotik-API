package com.project.kresnahotspot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.HotspotActiveMikrotik;
import com.project.kresnahotspot.model.HotspotUserMikrotik;

import java.util.List;

public class ActiveHotspotAdapter extends RecyclerView.Adapter<ActiveHotspotAdapter.MyViewHolder> {
    Context context;
    List<HotspotActiveMikrotik> list;

    public ActiveHotspotAdapter(Context context,List<HotspotActiveMikrotik>list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ActiveHotspotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_hotspot_active_admin,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveHotspotAdapter.MyViewHolder holder, int position) {
        HotspotActiveMikrotik hotspotActiveMikrotik=list.get(position);
        String server=hotspotActiveMikrotik.getServer();
        String user=hotspotActiveMikrotik.getUser();
        String uptime=hotspotActiveMikrotik.getUptime();
        String idleTime=hotspotActiveMikrotik.getIdleTime();
        String session=hotspotActiveMikrotik.getSessionTime();
        String mac=hotspotActiveMikrotik.getMacAddress();

        holder.server.setText(server);
        holder.user.setText(user);
        holder.uptime.setText(uptime);
        holder.idleTime.setText(idleTime);
        holder.sessionTime.setText(session);
        holder.macAddress.setText(mac);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView server,user, uptime, idleTime,sessionTime,macAddress;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            server=(TextView) itemView.findViewById(R.id.rowHotspotActive_server);
            user=(TextView) itemView.findViewById(R.id.rowHotspotActive_user);
            uptime=(TextView) itemView.findViewById(R.id.rowHotspotActive_uptime);
            idleTime=(TextView) itemView.findViewById(R.id.rowHotspotActive_idleTime);
            sessionTime=(TextView) itemView.findViewById(R.id.rowHotspotActive_sessionTime);
            macAddress=(TextView) itemView.findViewById(R.id.rowHotspotActive_macAddress);
        }
    }
}
