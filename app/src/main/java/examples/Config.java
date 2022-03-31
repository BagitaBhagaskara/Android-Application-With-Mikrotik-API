package examples;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Config class for examples
 *
 * @author gideon
 */
public class Config {
//
//    String connect, username,password;
//
//    public void ambilDataLogin() {
//        FirebaseFirestore db=FirebaseFirestore.getInstance();
//        DocumentReference documentReference=db.collection("loginMikrotik").document();
//        documentReference
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if(task.isSuccessful()){
//                            DocumentSnapshot documentSnapshot= task.getResult();
//                            connect=documentSnapshot.getString("Connect");
//                            username=documentSnapshot.getString("Username");
//                            password=documentSnapshot.getString("Password");
//                        }else{
//
//                        }
//                    }
//                });
//
//    }

    public static final String HOST = "192.168.56.104";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";

}
