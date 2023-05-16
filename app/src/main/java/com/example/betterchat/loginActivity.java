package com.example.betterchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class  loginActivity extends AppCompatActivity{


    EditText EmailLogin, PasswordLogin;
    Button button_InicioSesion;
    TextView textView_OlvidastePassword, textView_RegistraseLogin;
    FirebaseAuth mAuth;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        EmailLogin = findViewById(R.id.EditText_EmailLogin);
        PasswordLogin = findViewById(R.id.EditText_PasswordLogin);
        button_InicioSesion = findViewById(R.id.button_InicioSesion);
        textView_RegistraseLogin = findViewById(R.id.textView_RegistraseLogin);
        textView_OlvidastePassword = findViewById(R.id.textView_OlvidastePassword);


        textView_OlvidastePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(loginActivity.this,recuperarPassword.class);
                startActivity(intent1);
                finish();
            }
        });

        textView_RegistraseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(loginActivity.this, Activity_RegistroUsuario.class);
                startActivity(intent);
            }
        });

        button_InicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String EmailUser = EmailLogin.getText().toString().trim();
                String PasswordUser = PasswordLogin.getText().toString().trim();

                if(EmailUser.isEmpty() && PasswordUser.isEmpty()){

                    Toast.makeText(loginActivity.this, "Ingresa tus datos.", Toast.LENGTH_SHORT).show();

                }else{

                    loginUser(EmailUser,PasswordUser);

                }
            }

            private void loginUser(String emailUser, String passwordUser) {

                mAuth.signInWithEmailAndPassword(emailUser,passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(loginActivity.this,ChatsActivity.class));
                            Toast.makeText(loginActivity.this, "Iniciaste Sesion Correctamente.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(loginActivity.this, "Error al Iniciar Sesion unu.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(loginActivity.this, "Error al Iniciar Sesion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}