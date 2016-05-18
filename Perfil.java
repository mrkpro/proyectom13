package com.example.alumne.changebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class Perfil extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {
    public static Context x;
    EditText text,text1,text2,text3;

    public static final String KEY_USERNAME="user";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_EMAIL="email";
    public static final String KEY_NAME="name";

    private boolean loggedIn = false;
    public static final String SHARED_PREF_NAME = "compartido";
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String NAME_SHARED_PREF = "name";
    public static final String USER_SHARED_PREF = "user";
    public static final String PASSWORD_SHARED_PREF = "password";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    private static final String MODIFICAR = "http://changebook.esy.es/modificarUsuario.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        x=this;

        text = (EditText) findViewById(R.id.tvemail);
        text1 = (EditText) findViewById(R.id.tvpaswords);
        text2 = (EditText) findViewById(R.id.tvusers);
        text3 = (EditText) findViewById(R.id.tvnames);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(EMAIL_SHARED_PREF, "Not Available");
        String password = sharedPreferences.getString(PASSWORD_SHARED_PREF, "Not Available");
        String user = sharedPreferences.getString(USER_SHARED_PREF, "Not Available");
        String name = sharedPreferences.getString(NAME_SHARED_PREF, "Not Available");
        System.out.println(email + password);

        text.setText(email);
        text1.setText(password);
        text2.setText(user);
        text3.setText(name);
        Button save = (Button)findViewById(R.id.btsave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarUsuario();
            }
        });

    }
    private void modificarUsuario(){
        //Cojemos los valores que hay en los respectivos EditText donde escribimos.
        final String email = text.getText().toString().trim();
        final String password = text1.getText().toString().trim();
        final String user = text2.getText().toString().trim();
        final String name = text3.getText().toString().trim();
        //Utilizo la libreria volley que ya permite enviar peticiones directas al servidor
        //El String Registro apunta a la url donde se encuentra el fichero PHP
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MODIFICAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("OK")){
                    Toast.makeText(Perfil.this,"Se ha modificado con exito", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(Perfil.this,error.toString(), Toast.LENGTH_LONG).show();
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
    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Estás seguro que quieres salir?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                //Getting out sharedpreferences
                SharedPreferences preferences = getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
                //Getting editor
                SharedPreferences.Editor editor = preferences.edit();

                //Puting the value false for loggedin
                editor.putBoolean(LOGGEDIN_SHARED_PREF, false);
                //Putting blank value to email

                //Saving the sharedpreferences
                editor.commit();

                //Starting login activity
                Intent intent = new Intent(Perfil.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,x.MODE_PRIVATE);
            loggedIn = sharedPreferences.getBoolean(LOGGEDIN_SHARED_PREF,false);
            if(!loggedIn) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(x,AddLibro.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {
            logout();

        } else if (id == R.id.consulta) {
            Intent i = new Intent(x,ListaLibros.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(x,ListaMisLibros.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
