package com.example.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.databinding.ActivityWorkersBinding;

public class Workers extends AppCompatActivity {
    private int MY_PERMISSIONS_REQUEST;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityWorkersBinding binding;

    Button boton_cerrar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWorkersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //shared
        preferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
        boton_cerrar = findViewById(R.id.button2);
        boton_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("sesion", false);
                editor.apply();
                startActivity(new Intent(Workers.this, MainActivity.class));
            }
        });
        setSupportActionBar(binding.appBarWorkers.toolbar);
        /*
        binding.appBarWorkers.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        String niv = preferences.getString("nivel","normi");
        Menu nav_Menu = navigationView.getMenu();

        if(niv.equals("admin")){
            nav_Menu.findItem(R.id.nav_slideshow).setVisible(false);
        }else{
            nav_Menu.findItem(R.id.nav_slideshow).setVisible(false);
            nav_Menu.findItem(R.id.nav_admin).setVisible(false);
            nav_Menu.findItem(R.id.nav_eliminar).setVisible(false);
            nav_Menu.findItem(R.id.nav_editar).setVisible(false);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_perfil, R.id.nav_admin, R.id.nav_editar, R.id.nav_eliminar)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_workers);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.workers, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_workers);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}