package com.example.betterchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Activity_RegistroUsuario extends AppCompatActivity {


    Button RegistrarUsuario;
    EditText UsuarioRegistro, mailRegistro, PasswordRegistro;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        UsuarioRegistro = findViewById(R.id.EditText_UsuarioRegistro);
        mailRegistro = findViewById(R.id.EditText_EmailRegistro);
        PasswordRegistro = findViewById(R.id.EditText_PasswordRegistro);
        RegistrarUsuario = findViewById(R.id.button_RegistrarUsuario);

        RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreUsuario = UsuarioRegistro.getText().toString().trim();
                String EmailUsuario = mailRegistro.getText().toString().trim();
                String PasswordUsuario = PasswordRegistro.getText().toString().trim();

                // validacion para que
                if(nombreUsuario.isEmpty() && EmailUsuario.isEmpty() && PasswordUsuario.isEmpty()){

                    Toast.makeText(Activity_RegistroUsuario.this,"Complete todos los campos", Toast.LENGTH_SHORT).show();

                }else{

                    RegisterUser(nombreUsuario,EmailUsuario,PasswordUsuario);

                }
            }
        });
    }

        private void RegisterUser(String nombreUsuario, String EmailUsuario, String PasswordUsuario){

        mAuth.createUserWithEmailAndPassword(EmailUsuario,PasswordUsuario).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            String id = mAuth.getCurrentUser().getUid();
            Map<String,Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", nombreUsuario);
            map.put("email", EmailUsuario);
            map.put("password", PasswordUsuario);

            mFirestore.collection("Usuario").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    finish();
                    startActivity(new Intent(Activity_RegistroUsuario.this,MainActivity.class));
                    Toast.makeText(Activity_RegistroUsuario.this, "Te registraste con Exito! :)", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    e.printStackTrace();
                    Log.e("Error", "Firebase Error: " + e.getMessage());
                    Toast.makeText(Activity_RegistroUsuario.this, "Error al Registrar Unu", Toast.LENGTH_SHORT).show();

                }
            });

            }

        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Activity_RegistroUsuario.this, "Error al Registrar:(", Toast.LENGTH_SHORT).show();

            }
        });
    }
}