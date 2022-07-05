package examples;

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

/**
 * Config class for examples
 *
 * @author gideon
 */
public class Config {
    public  String HOST;
    public  String USERNAME;
    public  String PASSWORD;

    public void ambilDataLogin() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("loginMikrotik")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                                String id=queryDocumentSnapshot.getId();
                                DocumentReference documentReference=db.collection("loginMikrotik").document(id);
                                documentReference.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot documentSnapshot= task.getResult();
                                               HOST=documentSnapshot.getString("Connect");
                                                USERNAME=documentSnapshot.getString("Username");
                                                PASSWORD=documentSnapshot.getString("Password");
                                                //share prefere
                                            }
                                        });
                            }
                        }
                    }
                });
    }


//    public static final String HOST = "192.168.56.104";
//    public static final String USERNAME = "admin";
//    public static final String PASSWORD = "admin";



//    public static final String HOST = "20.20.20.1";
//    public static final String USERNAME = "admin@arsanawayan";
//    public static final String PASSWORD = "arsana160396";

}
