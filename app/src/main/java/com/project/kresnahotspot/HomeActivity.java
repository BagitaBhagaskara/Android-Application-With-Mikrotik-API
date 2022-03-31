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

//        fotoUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updateFotoUser();
//
//            }
//        });

//        simpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                uploadFoto();
//            }
//        });
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

//    private void updateFotoUser(){
//        final CharSequence[] items={"Ambil Foto","Pilih Dari Galeri","Batal"};
//        AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
//        builder.setTitle("Ubah Foto Profil ?");
//        builder.setItems(items,(dialog,item)->{
//            if (items[item].equals("Ambil Foto")){
//                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(i,10);
//            }
//           else if (items[item].equals("Pilih Dari Galeri")){
//                Intent i = new Intent(Intent.ACTION_PICK);
//                i.setType("image/*");
//                startActivityForResult(Intent.createChooser(i,"Pilih Foto"),20);
//
//            }
//           else if (items[item].equals("Batal")){
//               dialog.dismiss();
//            }
//        });
//        builder.show();
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==10&&resultCode==RESULT_OK){
//            final Bundle extras= data.getExtras();
//            Thread thread = new Thread(()->{
//                Bitmap bitmap =(Bitmap) extras.get("data");
//                fotoUser.post(()->{
//                    fotoUser.setImageBitmap(bitmap);
//
//                });
//            });
//            thread.start();
//        }
//        if(requestCode==20&&resultCode==RESULT_OK&&data!=null){
//            final Uri path= data.getData();
//            Thread thread = new Thread(()->{
//                try{
//                    InputStream inputStream=getContentResolver().openInputStream(path);
//                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                    fotoUser.post(()->{
//                        fotoUser.setImageBitmap(bitmap);
//
//                    });
//                }
//                catch (IOException e){
//                    e.printStackTrace();
//                }
//            });
//            thread.start();
//        }
//    }
////
//    private void uploadFoto (){
//        fotoUser.setDrawingCacheEnabled(true);
//        fotoUser.buildDrawingCache();
//        Bitmap bitmap=((BitmapDrawable)fotoUser.getDrawable()).getBitmap();
//        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
//        byte[]data=byteArrayOutputStream.toByteArray();
//
//        FirebaseStorage storage=FirebaseStorage.getInstance();
//        StorageReference storageReference=storage.getReference("fotoUser").child("USR"+new Date().getTime()+".jpeg");
//
//        UploadTask uploadTask =storageReference.putBytes(data);
//        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                Toast.makeText(HomeActivity.this, "Foto Gagal Diuplaod", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                if(taskSnapshot.getMetadata()!=null){
//                    if(taskSnapshot.getMetadata().getReference()!=null){
//                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
//                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Uri> task) {
//                                        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
//                                        uploadFotoFireStore(task.getResult().toString(),firebaseAuth.getCurrentUser().getUid());
//                                    }
//                                });
//                    }
//                }
//            }
//        });
//
//    }
//    private void uploadFotoFireStore(String urlFoto, String id) {
//        Map<String,Object>dataUser=new HashMap<>();
//        dataUser.put("fotoUser",urlFoto);
//        db.collection("dataUser").document(id)
//                .set(dataUser)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(HomeActivity.this, "Foto Berhasil Diuplaod", Toast.LENGTH_SHORT).show();
//                    }
//                });
    //}


}