package com.project.kresnahotspot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import examples.Config;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore db;
    TextView namaUserLogin;
    public TextView coin;
    ImageView fotoUser;
    Button simpan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);



        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        NavigationView navigationView =findViewById(R.id.navigationView);
        View headerView =navigationView.getHeaderView(0);
        namaUserLogin=(TextView) headerView.findViewById(R.id.namaUser) ;
        coin=(TextView) headerView.findViewById(R.id.jumlahCoinUser);
        fotoUser=(ImageView) headerView.findViewById(R.id.imageProfile);

        navigationView.setItemIconTintList(null);

        NavController navController= Navigation.findNavController(this, R.id.naviHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        final TextView textTitle = findViewById(R.id.textTitle);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                textTitle.setText(destination.getLabel());
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
                    ambilDataNamaUser(firebaseAuth.getCurrentUser().getUid());
                }
            }
        });
    }
    private void ambilDataNamaUser(String id){
        db.collection("dataUser").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                           DocumentSnapshot documentSnapshot=task.getResult();
                           String nama=documentSnapshot.getString("namaUser");
                           namaUserLogin.setText(nama);
                           String poin=documentSnapshot.getString("coin");
                           coin.setText(poin);
                    }
                });
    }

}