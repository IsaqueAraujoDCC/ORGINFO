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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cadastro extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;
    private EditText campoUsername, campoEmail, campoSenha;
    private Button btCadastrar;
    private TextView queroLogar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        campoUsername = (EditText) findViewById(R.id.username);
        campoEmail = (EditText) findViewById(R.id.email);
        campoSenha = (EditText) findViewById(R.id.senha);
        btCadastrar = (Button)findViewById(R.id.btCadastrar);
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CadastrarUsuario();
            }
        });
        queroLogar = (TextView)findViewById(R.id.queroLogar);
        queroLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cadastro.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_open_scale, R.anim.slide_close_scale);
            }
        });
    }
    public void CadastrarUsuario(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        usuario = new Usuario(campoUsername.getText().toString(), campoEmail.getText().toString(), campoSenha.getText().toString());
        firebaseAuth.createUserWithEmailAndPassword(usuario.getE_mail(), usuario.getSenha()).addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    usuario.setId(task.getResult().getUser().getUid().toString());
                    usuario.salvarDados();
                }else{
                    Toast.makeText(Cadastro.this, "Não foi possivel se cadastrar 8(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Cadastro.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_open_scale, R.anim.slide_close_scale);
    }
}
