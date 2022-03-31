package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Map;

import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;


public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        final DrawerLayout drawerLayoutAdmin = findViewById(R.id.drawerLayout_admin);

        findViewById(R.id.menu_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutAdmin.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView =findViewById(R.id.navigationView_admin);
        navigationView.setItemIconTintList(null);

        NavController navController= Navigation.findNavController(this, R.id.naviHostFragment_admin);
        NavigationUI.setupWithNavController(navigationView, navController);

        final TextView textTitleAdmin=findViewById(R.id.textTitle_admin);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                textTitleAdmin.setText(destination.getLabel());
            }
        });


    }
    ApiConnection con;
    private void test() throws MikrotikApiException {
        List<Map<String, String>> results = con.execute("/interface/print");
        for (Map<String, String> result : results) {
            System.out.println(result);
        }
    }


}