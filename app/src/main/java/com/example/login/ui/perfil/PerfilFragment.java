package com.example.login.ui.perfil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.R;
import com.example.login.databinding.FragmentPerfilBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

import java.sql.SQLOutput;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment{

    private FragmentPerfilBinding binding;

    private ImageButton imagenButton;
    private Button button;
    private CircleImageView porfileImage;
    boolean conImagen = false;
    String nombreImagenCamara;
    Uri imgUri;
    String linkImagen;
    final int código_galería = 10;
    final int código_cámara = 20;
    StorageReference nombreFolder;
    StorageReference nombreImagen;
    String usuario;
    DatabaseReference databaseReference;
    int num =1;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PerfilViewModel perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView_usuario = binding.textView1;
        final TextView textView_correo = binding.textView2;
        final TextView textView_fecha = binding.textView3;
        final TextView textView_pais = binding.textView4;

        imagenButton = binding.imageButton;
        porfileImage = binding.imageView1;

        button = binding.button;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //perfilViewModel.getText().observe(getViewLifecycleOwner(), textView_usuario::setText);



        SharedPreferences sharedPref = getActivity().getSharedPreferences("sesiones", 0);
        usuario = sharedPref.getString("usuario", "");
        String correo = sharedPref.getString("correo", "");
        String fecha = sharedPref.getString("fecha", "");
        String pais = sharedPref.getString("pais", "");


        if (num==1){
            cargarFoto();
        }
        num=2;
        textView_usuario.setText(usuario);
        textView_correo.setText(correo);
        textView_fecha.setText(fecha);
        textView_pais.setText(pais);


        imagenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Solicita los permisos para las imagenes
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
                }
                alertDialog();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                System.out.println("----------------------------------------------------------------");
                System.out.println(conImagen);
                System.out.println("----------------------------------------------------------------");
                if (conImagen){
                    /*
                    Map<String, Object> update = new HashMap<>();
                    update.put("linkImagen",linkImagen);
                    update.put("conImagen",conImagen);


                    databaseReference1.child(usuario).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });

                     */
                    databaseReference1.child(usuario).child("linkImagen").setValue(linkImagen);
                    databaseReference1.child(usuario).child("conImagen").setValue(conImagen);
                    cargarFoto();

                }else{
                    /*
                    Map<String, Object> update = new HashMap<>();
                    update.put("conImagen",conImagen);

                    databaseReference1.child(usuario).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });

                     */
                    databaseReference1.child(usuario).child("conImagen").setValue(conImagen);
                    cargarFoto();
                }
            }

        });



        return root;
    }

    private void alertDialog(){
        final CharSequence[] opciones = {"Cámara", "Galería", "Quitar", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getActivity());
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        if (conImagen){
                            quitarImagen();
                        }
                        //Abre la cámara
                        Intent intentCámara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //Crear nombre de la imagen con informacion del tiempo
                        nombreImagenCamara = nombreImagen();
                        //Guardar archivo temporal

                        File fotoArchivo = new File(getActivity().getExternalFilesDir(null),"imagen_"+nombreImagenCamara);
                        imgUri = FileProvider.getUriForFile(getActivity(),"com.example.login.fileprovider", fotoArchivo);
                        intentCámara.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
                        startActivityForResult(intentCámara,código_cámara);
                        break;
                    case 1:
                        if(conImagen){
                            quitarImagen();
                        }
                        //Abre la galería
                        Intent intentGalería = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intentGalería.setType("image/*");
                        startActivityForResult(intentGalería.createChooser(intentGalería,"Seleccione la Aplicación"),código_galería);
                        break;
                    case 2:
                        //if(conImagen){
                            quitarImagen();
                       // }
                        conImagen = false;
                        break;
                    case 3:
                        dialog.dismiss();
                        break;
                }
            }
        });
        alertOpciones.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case código_cámara:
                    Bitmap imgBitmap = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/imagen_"+nombreImagenCamara);

                    nombreFolder = FirebaseStorage.getInstance().getReference().child("ImagenesPerfil");
                    nombreImagen = nombreFolder.child(imgUri.getLastPathSegment());
                    nombreImagen.putFile(imgUri).addOnSuccessListener(taskSnapshot -> nombreImagen.getDownloadUrl().addOnSuccessListener(imgUri -> {
                        linkImagen = String.valueOf(imgUri);
                    }));

                    porfileImage.setImageBitmap(imgBitmap);
                    conImagen = true;

                    break;
                case código_galería:
                    //Se obtiene la imagen seleccionada
                    imgUri = data.getData();
                    //Indicamos que las imagenes se guadaran en la carpeta ImagenesPerfil
                    nombreFolder = FirebaseStorage.getInstance().getReference().child("ImagenesPerfil");
                    //Se establece el nombre de la imagen
                    nombreImagen = nombreFolder.child("imagen"+imgUri.getLastPathSegment() + nombreImagen());
                    //obtiene el link de la imagen
                    nombreImagen.putFile(imgUri).addOnSuccessListener(taskSnapshot -> nombreImagen.getDownloadUrl().addOnSuccessListener(imgUri -> {
                        linkImagen = String.valueOf(imgUri);
                    }));
                    //Se muestra la imagen en el imageview
                    porfileImage.setImageURI(imgUri);
                    conImagen = true;

                    break;
            }
        }
    }

    private void cargarFoto(){
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.child(usuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String conImagen2 = snapshot.child("conImagen").getValue().toString();

                    if (conImagen2.equals("true")){
                        if (snapshot.child("linkImagen").exists()) {
                            String linkImagen2 = snapshot.child("linkImagen").getValue().toString();
                            CircleImageView imageView;
                            imageView = binding.imageView1;
                            Picasso.get().load(linkImagen2).into(imageView);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void quitarImagen(){
        porfileImage = binding.imageView1;
        porfileImage.setImageResource(R.drawable.perfil);
        conImagen = false;
    }

    private  String nombreImagen(){
        Calendar fecha = Calendar.getInstance();
        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);
        String nombreImagen = año+mes+dia+hora+minuto+segundo+System.currentTimeMillis()/1000+".jpg";
        return nombreImagen;
    }
}
