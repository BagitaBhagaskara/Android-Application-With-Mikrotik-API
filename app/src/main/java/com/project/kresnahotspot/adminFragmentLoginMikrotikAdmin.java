package com.project.kresnahotspot;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.kresnahotspot.model.ConfigLoginMikrotik;
import com.project.kresnahotspot.model.PembelianPoint;
import com.project.kresnahotspot.model.ProfileHotspotAdmin_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.SocketFactory;

import examples.Config;
import me.legrange.mikrotik.ApiConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminFragmentLoginMikrotikAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminFragmentLoginMikrotikAdmin extends Fragment {
    private List<ConfigLoginMikrotik> list=new ArrayList<>();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    SharedPreferences getDataLoginMikrotik;
    SharedPreferences.Editor setLoginMikrotik;
    ApiConnection con;
    TextView connect,login,password,port;
    Button cekKoneksi, editKoneksi;



    public static boolean cekLoginMikrotik=true;
    private static final String LOG_TAG = "Login Mikrotik";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminFragmentLoginMikrotikAdmin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminFragmentLoginMikrotikAdmin.
     */
    // TODO: Rename and change types and number of parameters
    public static adminFragmentLoginMikrotikAdmin newInstance(String param1, String param2) {
        adminFragmentLoginMikrotikAdmin fragment = new adminFragmentLoginMikrotikAdmin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_admin_login_mikrotik_admin, container, false);
        connect=(TextView) view.findViewById(R.id.loginMikrotikAdmin_connectTo);
        login=(TextView) view.findViewById(R.id.loginMikrotikAdmin_login);
        password=(TextView) view.findViewById(R.id.loginMikrotikAdmin_password);
        port=(TextView) view.findViewById(R.id.loginMikrotikAdmin_Port);
        cekKoneksi=(Button) view.findViewById(R.id.loginMikrotikAdmin_buttonCekKoneksi);
        editKoneksi=(Button) view.findViewById(R.id.loginMikrotikAdmin_buttonEditKoneksi);

        getDataLoginMikrotik= getContext().getSharedPreferences("data_login_mikrotik", MODE_PRIVATE);
        String portMikro=Integer.toString(getDataLoginMikrotik.getInt("portLoginMikrotikInt",0));
        String ipMikro=getDataLoginMikrotik.getString("ipLoginMikrotik",null);
        String userMikro=getDataLoginMikrotik.getString("usernameLoginMikrotik",null);
        String passMikro=getDataLoginMikrotik.getString("passwordLoginMikrotik",null);



        setDataLogin(connect,login,password,port,portMikro,ipMikro,userMikro,passMikro);

        editKoneksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditKoneksi(ipMikro,userMikro,passMikro,portMikro);
            }
        });
        cekKoneksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginMikrotik loginMikrotik=new LoginMikrotik();
                loginMikrotik.execute();

                dialogAlert();
            }
        });

        return view;
    }

    private void dialogAlert() {
       View dialogWarning=LayoutInflater.from(getContext()).inflate(R.layout.dialog_alert_login_mikrotik_gagal,null);
       View dialogBerhasil=LayoutInflater.from(getContext()).inflate(R.layout.dialog_alert_login_mikrotik_berhasil,null);

        if(cekLoginMikrotik==false){
           AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
           builder.setView(dialogWarning);
           AlertDialog dialog=builder.create();
           dialog.show();
       }else if(cekLoginMikrotik==true){
           AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
           builder.setView(dialogBerhasil);
           AlertDialog dialog=builder.create();
           dialog.show();
       }
    }

    class LoginMikrotik extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //tvResult.setText("Begin");

        }
        @Override
        protected Void doInBackground(Void... voids) {

            try
            {
                con = ApiConnection.connect(SocketFactory.getDefault(), getDataLoginMikrotik.getString("ipLoginMikrotik",null), getDataLoginMikrotik.getInt("portLoginMikrotikInt",0 ), 2000);
                con.login(getDataLoginMikrotik.getString("usernameLoginMikrotik",null), getDataLoginMikrotik.getString("passwordLoginMikrotik",null));
                if(con.isConnected()){
                    Log.d(LOG_TAG, "Login Berhasil");
                    cekLoginMikrotik=true;
                }
            }
            catch (Exception e)
            {
                cekLoginMikrotik=false;
                Log.d(LOG_TAG, "Login gagal: "+getDataLoginMikrotik.getInt("portLoginMikrotikInt",0 ));
                Log.d(LOG_TAG, ""+e);
            }
            return null;
        }
    }

    private void setDataLogin(TextView connect, TextView login, TextView password, TextView port, String portMikro, String ipMikro, String userMikro, String passMikro) {
        connect.setText(ipMikro);
        login.setText(userMikro);
        password.setText(passMikro);
        port.setText(portMikro);
//        portMikrotik =Integer.parseInt(getDataLoginMikrotik.getString("portLoginMikrotik",null));

    }

    private void dialogEditKoneksi(String ipMikro, String userMikro, String passMikro, String portMikro) {
        View view =LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_data_login_mikrotik,null);
        EditText connectDialog,portDialog,loginDialog,passwordDialog;
        Button simpan, batal;

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(view);

        AlertDialog dialog=builder.create();
        dialog.show();

        connectDialog=(EditText) view.findViewById(R.id.dialogEditLoginMikrotik_connectTo);
        portDialog=(EditText) view.findViewById(R.id.dialogEditLoginMikrotik_port);
        loginDialog=(EditText) view.findViewById(R.id.dialogEditLoginMikrotik_login);
        passwordDialog=(EditText) view.findViewById(R.id.dialogEditLoginMikrotik_password);
        simpan=(Button) view.findViewById(R.id.dialogEditLoginMikrotik_buttonSimpan);
        batal=(Button) view.findViewById(R.id.dialogEditLoginMikrotik_buttonBatal);
        connectDialog.setText(ipMikro);
        portDialog.setText(portMikro);
        loginDialog.setText(userMikro);
        passwordDialog.setText(passMikro);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String koneksi=connectDialog.getText().toString();
                String userLogin=loginDialog.getText().toString();
                String pass=passwordDialog.getText().toString();
                String portt=portDialog.getText().toString();


                if(koneksi.equals("")){
                    Toast.makeText(getContext(), "Data login mikrotik belum lengkap", Toast.LENGTH_SHORT).show();
                }else if(userLogin.equals("")){
                    Toast.makeText(getContext(), "Data login mikrotik belum lengkap", Toast.LENGTH_SHORT).show();
                }else if(pass.equals("")){
                    Toast.makeText(getContext(), "Data login mikrotik belum lengkap", Toast.LENGTH_SHORT).show();
                }else if(portt.equals("")){
                Toast.makeText(getContext(), "Data login mikrotik belum lengkap", Toast.LENGTH_SHORT).show();
                 }else{
                    simpanDataLoginMikrotik(koneksi,userLogin,pass,portt);
                    connect.setText(koneksi);
                    port.setText(portt);
                    login.setText(userLogin);
                    password.setText(pass);
                    dialog.dismiss();
                }
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void simpanDataLoginMikrotik(String koneksi, String userLogin, String pass, String portt) {
        setLoginMikrotik=getContext().getSharedPreferences("data_login_mikrotik",MODE_PRIVATE).edit();
        int p=Integer.parseInt(portt);
        setLoginMikrotik.putString("ipLoginMikrotik",koneksi);
        setLoginMikrotik.putString("usernameLoginMikrotik",userLogin);
        setLoginMikrotik.putString("passwordLoginMikrotik",pass);
        setLoginMikrotik.putInt("portLoginMikrotikInt",p);
        setLoginMikrotik.commit();

        HashMap<String,Object> DataLogin=new HashMap<>();
        DataLogin.put("Connect",koneksi);
        DataLogin.put("Username",userLogin);
        DataLogin.put("Password",pass);
        DataLogin.put("Port",portt);
        db.collection("loginMikrotik")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                String id = queryDocumentSnapshot.getId();
                                DocumentReference documentReference=db.collection("loginMikrotik").document(id);
                                documentReference.set(DataLogin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getContext(), "Data Login Mikrotik Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }


}