package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    TextInputEditText editext_usuario;
    TextInputEditText editext_correo;
    TextInputEditText editext_contraseña;
    TextInputEditText editext_conf_contraseña;
    TextInputEditText editext_fecha;
    AutoCompleteTextView editext_paises;

    TextInputLayout textinput_usuario;
    TextInputLayout textinput_correo;
    TextInputLayout textinput_contraseña;
    TextInputLayout textinput_conf_contraseña;
    TextInputLayout textinput_fecha;
    TextInputLayout textinput_paises;

    TextView inicio;
    Button registrarse;
    int campos_correctos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editext_usuario = findViewById(R.id.textInputEditText01);
        textinput_usuario = findViewById(R.id.textInputLayout01);

        editext_correo = findViewById(R.id.textInputEditText02);
        textinput_correo = findViewById(R.id.textInputLayout02);

        editext_contraseña = findViewById(R.id.textInputEditText03);
        textinput_contraseña = findViewById(R.id.textInputLayout03);

        editext_conf_contraseña = findViewById(R.id.textInputEditText04);
        textinput_conf_contraseña = findViewById(R.id.textInputLayout04);

        editext_fecha = findViewById(R.id.textInputEditText05);
        textinput_fecha = findViewById(R.id.textInputLayout05);

        editext_paises = findViewById(R.id.autoCompleteTextView);
        textinput_paises = findViewById(R.id.textInputLayout06);

        inicio = findViewById(R.id.textView04);
        registrarse = findViewById(R.id.button);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(Registro.this,R.array.paises,android.R.layout.simple_spinner_dropdown_item);
        editext_paises.setAdapter(adapter);

        //Mostrar calendario
        editext_fecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerDialog datePickerDialog = new DatePickerDialog(Registro.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = year+"-"+month+"-"+day;
                        editext_fecha.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        //Ir al inicio
        inicio.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(Registro.this, Inicio.class));
            }
        });

        //Registrar usuario
        registrarse.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                //Obtenemos los datos del usuario
                String usuario = editext_usuario.getText().toString();
                String correo = editext_correo.getText().toString();
                String contraseña = editext_contraseña.getText().toString();
                String conf_contraseña = editext_conf_contraseña.getText().toString();
                String fecha = editext_fecha.getText().toString();
                String paises = editext_paises.getText().toString();

                sedimentación(usuario,correo,contraseña,conf_contraseña,fecha,paises);
                if (campos_correctos == 6) {
                    crear(usuario,correo,contraseña,fecha,paises);
                }
            }
        });
    }

    //Sedimentación
    private void sedimentación(String usuario, String correo, String contraseña,String conf_contraseña, String fecha, String paises){
        campos_correctos = 0;
        //Validar campos no vacios

        if (!usuario.isEmpty()){
            usuario = usuario.replace(" ", "").toLowerCase();
            campos_correctos = campos_correctos+1;
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Ingrese un nombre de usuario", Toast.LENGTH_SHORT);
            toast.show();
            //toggleTextInputLayoutError(textinput_usuario, "Ingrese un nombre de usuario");
        }

        if (!correo.isEmpty()){
            correo = correo.replace(" ", "").toLowerCase();
            Pattern pattern_correo = Patterns.EMAIL_ADDRESS;
            if (pattern_correo.matcher(correo).matches()){
                campos_correctos = campos_correctos+1;
            }else{
                campos_correctos = campos_correctos-1;
                Toast toast = Toast.makeText(Registro.this, "Formato no valido", Toast.LENGTH_SHORT);
                toast.show();
                //toggleTextInputLayoutError(textinput_correo, "Formato no valido");
            }
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Ingrese un correo", Toast.LENGTH_SHORT);
            toast.show();
            //toggleTextInputLayoutError(textinput_correo, "Ingrese un correo");
        }

        if (!contraseña.isEmpty()){
            contraseña = contraseña.replace(" ", "");
            campos_correctos = campos_correctos+1;
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Ingrese una contraseña", Toast.LENGTH_SHORT);
            toast.show();
            //toggleTextInputLayoutError(textinput_contraseña, "Ingrese una contraseña");
            campos_correctos = campos_correctos-1;
        }

        if (!conf_contraseña.isEmpty()){
            conf_contraseña = conf_contraseña.replace(" ", "");
            if (contraseña.equals(conf_contraseña)) {
                campos_correctos = campos_correctos+1;
            }else{
                campos_correctos = campos_correctos-1;
                Toast toast = Toast.makeText(Registro.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
                toast.show();
                //toggleTextInputLayoutError(textinput_conf_contraseña, "Las contraseñas no coinciden");
                campos_correctos = campos_correctos-1;
            }
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Confirme la contraseña", Toast.LENGTH_SHORT);
            toast.show();
            //toggleTextInputLayoutError(textinput_conf_contraseña, "Confirme la contraseña");
            campos_correctos = campos_correctos-1;
        }

        if (!fecha.isEmpty()){
            campos_correctos = campos_correctos+1;
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Seleccione una fecha", Toast.LENGTH_SHORT);
            toast.show();
            //toggleTextInputLayoutError(textinput_fecha, "Seleccione una fecha");
            campos_correctos = campos_correctos-1;
        }

        if (!paises.isEmpty()){
            campos_correctos = campos_correctos+1;
        }else{
            campos_correctos = campos_correctos-1;
            Toast toast = Toast.makeText(Registro.this, "Seleccione un país", Toast.LENGTH_SHORT);
            toast.show();
            //toggleTextInputLayoutError(textinput_paises, "Seleccione un país");
            campos_correctos = campos_correctos-1;
        }
    }
    private void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,String msg) {
        textInputLayout.setError(msg);
        textInputLayout.setErrorEnabled(msg != null);
    }

    private void  crear(String usuario, String correo, String contraseña, String fecha, String paises){
        String url = "https://www.carolinabr.tk/app/index.php";
        StringRequest postResquest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String value = jsonObject.getString("value");
                    String mensaje = jsonObject.getString("mensaje");
                    if (value.equals("TRUE")){
                        Toast toast = Toast.makeText(Registro.this, mensaje, Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        Toast toast = Toast.makeText(Registro.this, mensaje, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("error",error.getMessage());
            }
        })
        {
            protected Map<String, String > getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("contraseña", contraseña);
                params.put("usuario", usuario);
                params.put("correo", correo);
                params.put("fecha", fecha);
                params.put("pais", paises);
                params.put("request", "registrar");
                params.put("nivel", "normi");
                return params;
            }
        };
        Volley.newRequestQueue(Registro.this).add(postResquest);
    }
}