package com.example.alumne.changebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends Activity {
    static Context x;
    private EditText etemail;
    private EditText etpassword;
    private boolean loggedIn = false;
    //public static final String LOGIN_URL = "http://192.168.94.1/Android/LoginLogout/login.php";


    public static final String KEY_EMAIL="email";
    public static final String KEY_PASSWORD = "password";

    public static final String LOGIN_SUCCESS = "Logeado con exito";
    private static final String LOGIN = "http://changebook.esy.es/Login.php";



    //Keys for Sharedpreferences
    //This would be the name of our shared preference
    public static final String SHARED_PREF_NAME = "compartido";
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String NAME_SHARED_PREF = "name";
    public static final String USER_SHARED_PREF = "user";
    public static final String PASSWORD_SHARED_PREF = "password";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static boolean log =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView logear;
        logear = (TextView)findViewById(R.id.tvregister);
        x=this;

        etemail= (EditText)findViewById(R.id.etuser);
        etpassword = (EditText)findViewById(R.id.etpassword);


        Button entrar = (Button)findViewById(R.id.btlogin);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etemail.getText().toString().equals("") && etpassword.getText().toString().equals("")){
                    Toast toast = Toast.makeText(x, "Introduce el usuario y contraseña", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    LogearUsuario();


                }

            }
        });
        logear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(x, Registro.class);
                startActivity(i);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
    @Override
    protected void onResume(){
        super.onResume();
       SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,x.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(LOGGEDIN_SHARED_PREF,false);

       if (loggedIn){
            Intent intent = new Intent(Login.this, Perfil.class);
            startActivity(intent);
           finish();
        }


    }
    private void LogearUsuario(){
        //Cojo el email y el password que utiliza para logearse
        final String email = etemail.getText().toString().trim();
        final String password = etpassword.getText().toString().trim();
        //Utilizo la libreria volley que ya permite enviar peticiones directas al servidor
        //La variable String Login es la ruta de donde se encuntra el fichero php.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN, new Response.Listener<String>() {

            @Override
            //Cuando le envio la petición al servidor y es correcta
            public void onResponse(String response) {
                Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
                String[] parts = response.split("-");
                System.out.println(parts[0]);
                //Leo en json y tengo Shared preferences para pasarselo a la actividad del perfil
                try {

                    JSONObject jsonObject = new JSONObject(parts[0]);

                    String emails = jsonObject.getString("email");
                    String nombres = jsonObject.getString("name");
                    String passwords = jsonObject.getString("password");
                    String users = jsonObject.getString("user");
                    System.out.println(emails+nombres+passwords+users);

                    SharedPreferences sharedPreferences = Login.this.getSharedPreferences(SHARED_PREF_NAME,x.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean(LOGGEDIN_SHARED_PREF,true);
                    editor.putString(EMAIL_SHARED_PREF, emails);
                    editor.putString(NAME_SHARED_PREF,nombres);
                    editor.putString(PASSWORD_SHARED_PREF,passwords);
                    editor.putString(USER_SHARED_PREF,users);
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (parts[1].equalsIgnoreCase(LOGIN_SUCCESS)){

                    Intent i = new Intent(x,Perfil.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                }



            }
            //Cuando le envio la petición al servidor y es incorrecta
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(Login.this,error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            //Le paso los campos de las respectivas tablas
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put(KEY_EMAIL,email);
                params.put(KEY_PASSWORD,password);

                return params;
            }

        };
        //Envio la petición
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
