package br.android.apporginfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Principal extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseAuth.signOut();
        if(firebaseAuth.getCurrentUser() !=null){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Principal.this, TelaPrincipal.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_open_scale, R.anim.slide_close_scale);
                    finish();

                }
            }, 2 * 1250);
            Log.i("usario", "logado");
        }else{
            Log.i("usario", "não logado");
            Intent intent = new Intent(Principal.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}
