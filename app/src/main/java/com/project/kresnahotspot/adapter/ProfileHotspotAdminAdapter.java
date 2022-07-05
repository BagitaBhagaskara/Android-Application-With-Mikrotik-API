package com.project.kresnahotspot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kresnahotspot.R;
import com.project.kresnahotspot.model.ProfileHotspotAdmin_model;


import java.util.List;

public class ProfileHotspotAdminAdapter extends RecyclerView.Adapter<ProfileHotspotAdminAdapter.MyViewHolder> {
    private Context context;
    private List<ProfileHotspotAdmin_model> list;

    public ProfileHotspotAdminAdapter(Context context, List<ProfileHotspotAdmin_model> list){
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ProfileHotspotAdminAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_profile_hotspot_admin,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHotspotAdminAdapter.MyViewHolder holder, int position) {
        ProfileHotspotAdmin_model profileHotspotAdmin =list.get(position);
        String nama=profileHotspotAdmin.getNamaProfile();
        String rateLimit=profileHotspotAdmin.getRateLimit();
        String server=profileHotspotAdmin.getServer();
        String shareUser=profileHotspotAdmin.getShareUser();
        String uptime=profileHotspotAdmin.getUptime();

        holder.namaProfile.setText(nama);
        holder.rateLimit.setText(rateLimit);
        holder.server.setText(server);
        holder.shareUser.setText(shareUser);
        holder.uptime.setText(uptime);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView namaProfile,rateLimit,server,shareUser,uptime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namaProfile=(TextView) itemView.findViewById(R.id.row_profileHotspot_namaProfile);
            rateLimit=(TextView) itemView.findViewById(R.id.row_profileHotspot_rateLimit);
            server=(TextView) itemView.findViewById(R.id.row_profileHotspot_server);
            shareUser=(TextView) itemView.findViewById(R.id.row_profileHotspot_shareUser);
            uptime=(TextView) itemView.findViewById(R.id.row_profileHotspot_uptime);
        }
    }
}
