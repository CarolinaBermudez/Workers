package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inicio extends AppCompatActivity {
    private TextView registrarse;
    Button inicio;
    TextInputEditText editext_correo;
    TextInputEditText editext_contraseña;

    //shared
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //DatabaseReference databaseReference;
    String conImagen;
    String linkImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editext_correo = findViewById(R.id.textInputEditText001);
        editext_contraseña = findViewById(R.id.textInputEditText002);
        registrarse = findViewById(R.id.textView004);
        inicio = findViewById(R.id.button001);

        //shared
        preferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //databaseReference = FirebaseDatabase.getInstance().getReference();

        registrarse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Inicio.this, Registro.class));
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String correo = editext_correo.getText().toString();
                String contraseña = editext_contraseña.getText().toString();

                if (!correo.isEmpty()) {
                    if (!contraseña.isEmpty()) {
                        login(correo, contraseña);
                    } else {
                        Toast toast = Toast.makeText(Inicio.this, "Ingrese una contraseña", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(Inicio.this, "Ingrese un correo", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void guardarSesion(String usuario, String correo, String fecha, String pais, String nivel) {
        editor.putBoolean("sesion", true);
        editor.putString("usuario",usuario);
        editor.putString("correo",correo);
        editor.putString("fecha",fecha);
        editor.putString("pais",pais);
        editor.putString("nivel",nivel);
       // editor.putString("conImagen",conImagen);
        //editor.putString("linkImagen",linkImagen);

        editor.apply();
    }


    private void login(String correo, String contraseña) {

        String url = "https://www.carolinabr.tk/app/index.php";
        StringRequest postResquest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String value = jsonObject.getString("value");
                    if (value.equals("TRUE")) {

                        String mensaje = jsonObject.getString("mensaje");
                        String id_usuario = jsonObject.getString("id_usuario");
                        String usuario = jsonObject.getString("usuario");
                        String correo = jsonObject.getString("correo");
                        String contraseña = jsonObject.getString("contraseña");
                        String fecha = jsonObject.getString("fecha");
                        String pais = jsonObject.getString("pais");
                        String nivel = jsonObject.getString("nivel");
/*
                        databaseReference.child(usuario).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    //Se obtienen los datos de firebase
                                    //ArrayList clasificación = new ArrayList();
                                    //long clasificaciónTamaño = snapshot.child("clasificación").getChildrenCount();

                                    if (snapshot.child("conImagen").exists()){
                                        conImagen = snapshot.child("conImagen").getValue().toString();

                                    }else{
                                        conImagen = "false";
                                    }

                                    //linkImagen = snapshot.child("linkImagen").getValue().toString();

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
*/
                        //shared
                        guardarSesion(usuario,correo, fecha, pais, nivel);

                        startActivity(new Intent(Inicio.this, Workers.class));

                    } else {
                        String mensaje = jsonObject.getString("mensaje");
                        Toast toast = Toast.makeText(Inicio.this, mensaje, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("correo", correo);
                params.put("contraseña", contraseña);
                params.put("request", "login");
                return params;
            }
        };
        Volley.newRequestQueue(Inicio.this).add(postResquest);
    }
}