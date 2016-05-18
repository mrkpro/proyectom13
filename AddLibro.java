package com.example.alumne.changebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddLibro extends AppCompatActivity {
    ImageView mostrarFotos;
    Button searchFotos;
    static String pathimage;
    private CheckBox checkBoxA, checkBoxB, checkBoxC;
    Button subirDatos;
    EditText etname,eteditorial,etgenero,etprecio;
    static Context x;
    public static final String KEY_USERNAME="name";
    public static final String KEY_GENERO="genero";
    public static final String KEY_EDITORIAL="editorial";
    public static final String KEY_IMAGE="image";
    public static final String KEY_PRECIO="precio";
    public static final String SHARED_PREF_NAME = "compartido";
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String KEY_EMAIL = "coduser";
    String email;
    private static final String AñadirLibro = "http://changebook.esy.es/Addlibro.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_libro);
        x=this;

        etname=(EditText)findViewById(R.id.etname);
        eteditorial=(EditText)findViewById(R.id.eteditorial);
        etgenero=(EditText)findViewById(R.id.etgenero);
        etprecio=(EditText)findViewById(R.id.etprecio);

        mostrarFotos = (ImageView) findViewById(R.id.ivShow);
        searchFotos = (Button) findViewById(R.id.btgallery);
        searchFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        subirDatos = (Button)findViewById(R.id.btcargar);
        subirDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etname.getText().toString().equals("") && eteditorial.getText().toString().equals("")
                        && etgenero.getText().toString().equals("") && etprecio.getText().toString().equals("")
                        && mostrarFotos==null){
                    Toast toast = Toast.makeText(x, "Hay campos vacios", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    SubirDatos();
                    Intent i = new Intent(x,Perfil.class);
                    startActivity(i);
                }

            }
        });


        checkBoxA = (CheckBox) findViewById(R.id.cbinter);
        checkBoxB = (CheckBox) findViewById(R.id.cbventa);
        checkBoxC = (CheckBox) findViewById(R.id.cbambos);
    }

    private void selectImage() {

        final CharSequence[] options = {"Hacer una foto", "Elegir una foto de la galeria", "Cancelar "};


        AlertDialog.Builder builder = new AlertDialog.Builder(AddLibro.this);
        builder.setTitle("Añadir una foto!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Hacer una foto"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Elegir una foto de la galeria"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);


                } else if (options[item].equals("Cancelar")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;

                        break;

                    }

                }

                try {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, 200, 200, matrix, true);
                    pathimage = BitMapToString(bitmap);
                    mostrarFotos.setImageBitmap(bitmap);

                    String path = android.os.Environment.getExternalStorageDirectory() +
                            File.separator + "Phoenix" + File.separator + "default";

                    f.delete();

                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {

                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                        outFile.flush();
                        outFile.close();

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2) {


                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = Bitmap.createScaledBitmap(thumbnail, 200, 200, false);

                pathimage = BitMapToString(thumbnail);

                mostrarFotos.setImageBitmap(thumbnail);

            }

        }


    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    private void SubirDatos(){
        //Coje los parametros del EditText para subir el libro
        final String name = etname.getText().toString().trim();
        final String editorial = eteditorial.getText().toString().trim();
        final String genero = etgenero.getText().toString().trim();
        final String precio = etprecio.getText().toString().trim();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        email=sharedPreferences.getString(EMAIL_SHARED_PREF,"Not available");
        //Hago una peticion al servidor
        //El string añadirLibro es la url del fichero php que se encuentra en el servidor.
       StringRequest stringRequest = new StringRequest(Request.Method.POST, AñadirLibro, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddLibro.this, response, Toast.LENGTH_LONG).show();


            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(AddLibro.this,error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            //Le envio los parametros
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put(KEY_USERNAME,name);
                params.put(KEY_EDITORIAL,editorial);
                params.put(KEY_GENERO,genero);
                params.put(KEY_PRECIO,precio);
                params.put(KEY_IMAGE,pathimage);
                params.put(KEY_EMAIL,email);
                return params;
            }

        };
        //Envio la petición
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.cbinter:
                if (checked) {
                    checkBoxB.setChecked(false);
                    checkBoxC.setChecked(false);

                } else {

                }
                // Remove the meat
                break;
            case R.id.cbventa:
                if (checked) {
                    checkBoxA.setChecked(false);
                    checkBoxC.setChecked(false);

                }
                // Cheese me
                else {

                }
                break;
            case R.id.cbambos:
                if (checked) {

                    checkBoxA.setChecked(false);
                    checkBoxB.setChecked(false);

                }
                // Cheese me
                else {

                }
                // I'm lactose intolerant
                break;

        }

    }
}
