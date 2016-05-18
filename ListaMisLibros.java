package com.example.alumne.changebook;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaMisLibros extends AppCompatActivity {
    ListView listalibros;
    ArrayAdapter<String> adapter;
    ArrayList<Libros> items = new ArrayList<>();
    String selectlibros = "http://changebook.esy.es/mislibros.php";
    String borrarlibros = "http://changebook.esy.es/borrarlibros.php";
    Context context;
    public static final String SHARED_PREF_NAME = "compartido";
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String KEY_EMAIL = "email";
    String email;
    RequestQueue requestQueue;
    public static final String ID_SHARED_PREF = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_libros);

        context = this;

        requestQueue = Volley.newRequestQueue(context);

        listalibros = (ListView) findViewById(R.id.lvlibros);
        listalibros.setAdapter(new ArrayAdapterLibros(context, items));

        listalibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String ides = items.get(position).getId();
                try {
                    final CharSequence[] cq = {"Borrar libro"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("List");
                    builder.setItems(cq, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int item) {
                            switch (item) {
                                case 0:
                                    borrarLibro(ides);
                                    break;

                            }


                        }


                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                } catch (Exception e) {

                }
            }
        });

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST,selectlibros, new Response.Listener<String>() {
            @Override
            public void onResponse(String respon) {
                try {
                JSONArray json= new JSONArray(respon);

                //Leo los libros en json y los convierto y los añado a una listview
                for (int i = 0; i < json.length(); i++) {


                        JSONObject jsonObject = json.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String nombres = jsonObject.getString("name");
                        String editoriales = jsonObject.getString("editorial");
                        String generos = jsonObject.getString("genero");
                        String precios = jsonObject.getString("precio");
                        String imagenes = jsonObject.getString("image");
                        Libros l = new Libros(id,nombres, editoriales, generos, precios, imagenes);
                        items.add(l);




                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((ArrayAdapterLibros) listalibros.getAdapter()).notifyDataSetChanged();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array" + volleyError.getMessage());
            }
        }){
            @Override

            protected Map<String,String> getParams(){
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String email = sharedPreferences.getString(EMAIL_SHARED_PREF, "Not Available");
                Map<String,String> params = new HashMap<String,String>();
                params.put(KEY_EMAIL,email);
                System.out.println(email);

                return params;
            }

        };
        // add json array request to the request queue
        requestQueue.add(jsonArrayRequest);

    }

    private void borrarLibro (final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, borrarlibros, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("La id es: " + id);
                if (response.equalsIgnoreCase("OK")){
                    Toast.makeText(ListaMisLibros.this,"Se ha eliminado correctamente", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context,Perfil.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(ListaMisLibros.this,"No se ha podido eliminar", Toast.LENGTH_LONG).show();
                }

            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(ListaMisLibros.this,error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override

            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put(KEY_EMAIL,id);


                return params;
            }

        };

        requestQueue.add(stringRequest);



    }

}
