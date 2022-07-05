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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;


public class AdminActivity extends AppCompatActivity {
    TextView namaAdminLogin;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        final DrawerLayout drawerLayoutAdmin = findViewById(R.id.drawerLayout_admin);
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        findViewById(R.id.menu_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutAdmin.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView =findViewById(R.id.navigationView_admin);
        View headerView =navigationView.getHeaderView(0);
        namaAdminLogin=(TextView) headerView.findViewById(R.id.namaAdmin);


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
    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                }
                else {
                    ambilDataNamaAdmin(firebaseAuth.getCurrentUser().getUid());
                }
            }
        });
    }

    private void ambilDataNamaAdmin(String uid) {
        db.collection("dataUser").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot=task.getResult();
                        String nama=documentSnapshot.getString("namaUser");
                        namaAdminLogin.setText(nama);
                    }
                });
    }

}