package com.example.betterchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PerfilUser extends AppCompatActivity {

    ImageView ImgBackUser;

    ImageView ImgUser;

    TextView Descripcion_de_usuario;

    TextView Usuario;

    Button publicar;

    EditText publicacion;

    TextView publicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_user);

        ImgBackUser=findViewById(R.id.img_background);
        ImgUser=findViewById(R.id.img_user);
        Descripcion_de_usuario=findViewById(R.id.user_descrip);
        Usuario=findViewById(R.id.user_tag);
        publicar=findViewById(R.id.boton_publicar);
        publicacion=findViewById(R.id.text_publication);
        publicaciones=findViewById(R.id.user_publications);
    }

}