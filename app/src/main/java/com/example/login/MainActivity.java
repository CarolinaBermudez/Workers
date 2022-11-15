package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button iniciar_sesion;
    private Button registrarse;

    //shared
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciar_sesion = findViewById(R.id.iniciar_sesion);
        registrarse = findViewById(R.id.registrarse);

        //shared
        preferences = this.getSharedPreferences("sesiones",Context.MODE_PRIVATE);
        editor = preferences.edit();

        if (revisarSesion()){
            startActivity(new Intent(MainActivity.this, Workers.class));
        }else{
            /*
            String mensaje = "Sesión no iniciada prueba";
            Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show();
            */
        }

        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Inicio.class));
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, Registro.class));
            }
        });
    }
    private boolean revisarSesion(){
        return this.preferences.getBoolean("sesion",false);
    }

}