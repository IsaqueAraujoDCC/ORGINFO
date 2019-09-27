package br.android.apporginfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText campoEmail, campoSenha;
    private Button btEntrar;
    private TextView queroCadastrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        campoEmail = (EditText) findViewById(R.id.email);
        campoSenha = (EditText) findViewById(R.id.senha);
        btEntrar = (Button)findViewById(R.id.btEntrar);
        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entrar();
            }
        });
        queroCadastrar = (TextView)findViewById(R.id.queroCadastrar);
        queroCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.slide_open_scale, R.anim.slide_close_scale);
            }
        });
    }
    public void Entrar(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(campoEmail.getText().toString(), campoSenha.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(Login.this, TelaPrincipal.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.slide_open_scale, R.anim.slide_close_scale);
                    finish();
                }else{
                    Toast.makeText(Login.this, "Não foi possivle efetuar login 8(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
