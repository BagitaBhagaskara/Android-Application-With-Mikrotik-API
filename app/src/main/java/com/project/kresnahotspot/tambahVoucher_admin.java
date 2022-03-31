package com.project.kresnahotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tambahVoucher_admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_voucher_admin);

        Button tambahProfile;
        tambahProfile=(Button) findViewById(R.id.tambahVoucher_buttonTambahProfile);
        tambahProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(tambahVoucher_admin.this,tambahProfileHotspot_admin.class));
            }
        });


    }
}