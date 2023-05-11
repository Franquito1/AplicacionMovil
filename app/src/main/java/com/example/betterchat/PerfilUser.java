package com.example.betterchat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;

public class PerfilUser extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    ImageView ImgBackUser;
    ImageView ImgUser;
    TextView Descripcion_de_usuario;
    TextView Usuario;
    Button publicar;
    EditText publicacion;
    TextView publicaciones;

    FirebaseUser user;
    FirebaseAuth mAuth;
    ProgressDialog pd;
    Uri imageUri = null;

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

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        pd = new ProgressDialog(this);

        ImgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Seleccione una imagen"), PICK_IMAGE);
            }
        });

        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {
        if (imageUri != null) {
            pd.setMessage("Publicando");
            pd.show();

            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads/" + user.getUid() + "/" + System.currentTimeMillis() + ".jpg");

            UploadTask uploadTask = fileRef.putFile(imageUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String imageUrl = downloadUri.toString();
                        Toast.makeText(PerfilUser.this, "Publicación realizada correctamente", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Toast.makeText(PerfilUser.this, "Error al publicar", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mImageUri = data.getData();

            ImageView mImageView = null;
            Picasso.get().load(mImageUri).into(mImageView); // muestra la imagen seleccionada en el ImageView

            uploadFile(mImageUri);
        }
    }

    private void uploadFile(Uri mImageUri) {
        if (mImageUri != null) {
            StorageReference mStorageRef = null;
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show();
                    }
                }

                private String getFileExtension(Uri uri) {
                    ContentResolver contentResolver = getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    return mime.getExtensionFromMimeType(contentResolver.getType(uri));
                }
            }
