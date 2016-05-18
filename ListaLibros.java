package com.example.alumne.changebook;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

public class ListaLibros extends AppCompatActivity {
    ListView listalibros;
    ArrayAdapter<String> adapter;
    ArrayList<Libros> items = new ArrayList<>();
    String selectlibros = "http://changebook.esy.es/librosarray.php";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_libros);

        context = this;
        listalibros = (ListView) findViewById(R.id.lvlibros);
        listalibros.setAdapter(new ArrayAdapterLibros2(context, items));


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(selectlibros, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray json) {

            //Leo los libros en json y los convierto y los añado a una listview
                for (int i = 0; i < json.length(); i++) {
                    try {

                        JSONObject jsonObject = json.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String nombres = jsonObject.getString("name");
                        String editoriales = jsonObject.getString("editorial");
                        String generos = jsonObject.getString("genero");
                        String precios = jsonObject.getString("precio");
                        String imagenes = jsonObject.getString("image");
                        String coduser = jsonObject.getString("coduser");
                        Libros l = new Libros(id,nombres, editoriales, generos, precios, imagenes,coduser);
                        items.add(l);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ((ArrayAdapterLibros2) listalibros.getAdapter()).notifyDataSetChanged();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array"+volleyError.getMessage());
            }
        });
        // add json array request to the request queue
        requestQueue.add(jsonArrayRequest);

    }
}