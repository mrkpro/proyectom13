package com.example.alumne.changebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Registro extends Activity {
    private EditText etuser;
    private EditText etpassword;
    private EditText etemail;
    private EditText etname;
    static Context x;

    private static final String REGISTRO = "http://changebook.esy.es/Registro.php";

    public static final String KEY_USERNAME="user";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_EMAIL="email";
    public static final String KEY_NAME="name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        TextView login = (TextView)findViewById(R.id.tvlogin);
        x=this;

        etuser = (EditText)findViewById(R.id.etuser);
        etpassword = (EditText)findViewById(R.id.etpassword);
        etemail = (EditText)findViewById(R.id.etemail);
        etname = (EditText)findViewById(R.id.etname);

        Button registro = (Button)findViewById(R.id.btregister);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(x, MainActivity.class);
                startActivity(i);
                registrarUsuario();


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(x, Login.class);
                startActivity(i);
            }
        });
    }
    private void registrarUsuario(){
        //Cojemos los valores que hay en los respectivos EditText donde escribimos.
        final String user = etuser.getText().toString().trim();
        final String password = etpassword.getText().toString().trim();
        final String email = etemail.getText().toString().trim();
        final String name = etname.getText().toString().trim();
        //Utilizo la libreria volley que ya permite enviar peticiones directas al servidor
        //El String Registro apunta a la url donde se encuentra el fichero PHP
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTRO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Registro.this, response, Toast.LENGTH_LONG).show();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(Registro.this,error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            //Le paso los parámetros el primer valor es tal cual el nombre que tiene en la base de datos la tabla
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put(KEY_USERNAME,user);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL,email);
                params.put(KEY_NAME,name);
                return params;
            }

        };
        //Envio la petición al servidor
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_registro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
